package com.fullmob.jiraboard.analyzer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.fullmob.jiraboard.data.Board;
import com.fullmob.jiraboard.data.Column;
import com.fullmob.jiraboard.data.Point;
import com.fullmob.jiraboard.data.Ticket;
import com.fullmob.jiraboard.utils.Identifier;
import com.google.gson.Gson;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

import static com.fullmob.jiraboard.utils.QRPointUtils.maxX;
import static com.fullmob.jiraboard.utils.QRPointUtils.maxY;
import static com.fullmob.jiraboard.utils.QRPointUtils.minX;
import static com.fullmob.jiraboard.utils.QRPointUtils.minY;

public class BoardAnalyzer {

    private boolean debug;

    public BoardAnalyzer() {
        this(false);
    }

    public BoardAnalyzer(boolean debug) {
        this.debug = debug;
    }

    public Observable<Board> analyzeProject(final Board board) {
        return Observable.fromCallable(new Callable<Board>() {
            @Override
            public Board call() throws Exception {
                return analyzeProjectFromImage(board);
            }
        });
    }

    public Board analyzeProjectFromImage(Board project) {
        readQRImage(project.getBitmap(), project);
        return project;
    }

    public void readQRImage(Bitmap bMap, Board board) {

        Bitmap mutableBitmap = bMap.copy(Bitmap.Config.ARGB_8888, true);

        int[] intArray = new int[mutableBitmap.getWidth() * mutableBitmap.getHeight()];

        mutableBitmap.getPixels(intArray, 0, mutableBitmap.getWidth(), 0, 0, mutableBitmap.getWidth(), mutableBitmap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(mutableBitmap.getWidth(), mutableBitmap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeMultiReader qrCodeMultiReader = new QRCodeMultiReader();
        try {
            Identifier identifier = new Identifier(board);
            Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            Result[] results = qrCodeMultiReader.decodeMultiple(bitmap, hints);
            analyzeResults(board, mutableBitmap, identifier, results);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void analyzeResults(Board board, Bitmap mutableBitmap, Identifier identifier, Result[] results) {
        if (isDebug()) {
            Log.d("RESULTS", new Gson().toJson(results));
        }
        Map<String, Column> columnsMap = new HashMap<>();
        List<Column> ticketList = new ArrayList<>();
        board.setBoxed(isBoxedMode(identifier, results));
        boolean isColumn;
        for (Result r : results) {
            isColumn = identifier.isColumn(r.getText());
            if (!isColumn) {
                ticketList.add(createTicket(r));
            } else {
                board.addColumn(createColumn(columnsMap, r));
            }
        }
        orderColumnsAndTickets(board, ticketList, mutableBitmap);
        adjustBoardBoundaries(board);
        cropBoard(board);
        Log.d("RESULTS", new Gson().toJson(board.getColumns()));
    }

    private void cropBoard(Board board) {
        if (board.minX > 0 || board.minY > 0 ||
            board.maxX < board.getBitmap().getWidth() || board.maxY > board.getBitmap().getHeight()
        ) {
            board.setBitmap(
                Bitmap.createBitmap(
                    board.getBitmap(),
                    board.minX,
                    board.minY,
                    board.maxX - board.minX,
                    board.maxY - board.minY
                )
            );
        }
    }

    private void adjustBoardBoundaries(Board board) {
        board.maxX = 0;
        board.maxY = 0;
        board.minX = board.getBitmap().getWidth();
        board.minY = board.getBitmap().getHeight();
        for (Column col : board.getColumns()) {
            board.minX = Math.min(col.minX, board.minX);
            board.minY = Math.min(col.minY, board.minY);
            board.maxX = Math.max(col.maxX, board.maxX);
            board.maxY = Math.max(col.maxY, board.maxY);
        }
    }

    private Ticket createColumn(Map<String, Column> columnsMap, Result r) {
        Ticket column = new Ticket();
        column.text = r.getText();
        column.initPoints(6);
        for (int i = 0; i < 3; i++) {
            column.points[i] = new Point(r.getResultPoints()[i].getX(), r.getResultPoints()[i].getY());
        }
        if (!columnsMap.containsKey(column.text)) {
            columnsMap.put(column.text, column);
        } else {
            columnsMap.get(column.text).points[3] = column.points[0];
            columnsMap.get(column.text).points[4] = column.points[1];
            columnsMap.get(column.text).points[5] = column.points[2];
        }

        return column;
    }

    private Ticket createTicket(Result r) {
        Ticket ticket = new Ticket(r.getText());
        ticket.initPoints(3);
        for (int i = 0; i < 3; i++) {
            ticket.points[i] = new Point(r.getResultPoints()[i].getX(), r.getResultPoints()[i].getY());
        }

        return ticket;
    }

    private boolean isBoxedMode(Identifier identifier, Result[] results) {
        boolean boxedMode = false;
        HashSet<String> colValues = new HashSet<>();
        boolean isColumn;
        for (Result r : results) {
            String str = r.getText();
            isColumn = identifier.isColumn(str);
            if (!boxedMode && isColumn) {
                if (colValues.contains(str)) {
                    boxedMode = true;
                } else {
                    colValues.add(str);
                }
            }
        }

        return boxedMode;
    }

    private void orderColumnsAndTickets(Board board, List<Column> ticketList, Bitmap mutableBitmap) {
        if (board.isBoxed()) {
            prepareBoxedColumns(board);
        } else {
            prepareStackedColumns(board, mutableBitmap.getWidth(), mutableBitmap.getHeight());
        }
        for (int i = 0; i < ticketList.size(); i++) {
            Column ticket = ticketList.get(i);
            ticket.minX = minX(ticket);
            ticket.minY = minY(ticket);
            ticket.maxY = maxY(ticket);
            ticket.maxX = maxX(ticket);
            ticket.midX = (ticket.minX + ticket.maxX) / 2;
            ticket.midY = (ticket.minY + ticket.maxY) / 2;
            for (Column col : board.getColumns()) {
                if (ticket.midX > col.minX && ticket.midX <= col.maxX && ticket.midY > col.minY && ticket.midY <= col.maxY) {
                    col.tickets.add(ticketList.get(i));
                }
            }
            if (isDebug()) {
                drawSquare(mutableBitmap, ticket.minX, ticket.maxX, ticket.minY, ticket.maxY, Color.RED, 255);
            }
        }
        if (isDebug()) {
            drawDebugLines(board, mutableBitmap);
            board.setBitmap(mutableBitmap);
        }
    }

    private void prepareBoxedColumns(Board project) {
        for (Column col : project.getColumns()) {
            col.maxY = maxY(col);
            col.minY = minY(col);
            col.minX = minX(col);
            col.maxX = maxX(col);
        }
    }

    private void drawDebugLines(Board board, Bitmap mutableBitmap) {
        for (Column col : board.getColumns()) {
            if (board.isBoxed()) {
                drawDebugBoxedLines(mutableBitmap, col);
            } else {
                drawDebugLinesForUnboxed(mutableBitmap, col);
            }
        }
    }

    private void drawDebugBoxedLines(Bitmap mutableBitmap, Column col) {
        drawSquare(mutableBitmap, col.minX, col.maxX, col.minY, col.maxY, Color.GREEN, 30);
    }

    private void drawDebugLinesForUnboxed(Bitmap mutableBitmap, Column col) {
        drawSeparators(mutableBitmap, col.minX, Color.GREEN);
        drawSeparators(mutableBitmap, col.maxX, Color.GREEN);

        drawSeparators(mutableBitmap, minX(col), Color.RED);
        drawSeparators(mutableBitmap, maxX(col), Color.RED);
    }

    private void prepareStackedColumns(Board board, int imageWidth, int imageHeight) {
        Collections.sort(board.getStackedColumns(), new Comparator<Column>() {
            @Override
            public int compare(Column c1, Column c2) {
                return minX(c1) - minX(c2);
            }
        });
        int min;
        int max;
        for (int j = 0; j < board.getStackedColumns().size(); j++) {
            Column col = board.getStackedColumns().get(j);
            Column prevCol = j > 0 ? board.getStackedColumns().get(j - 1) : null;
            Column nextCol = j < (board.getStackedColumns().size() - 1) ? board.getStackedColumns().get(j + 1) : null;
            if (prevCol == null) {
                min = 500;
            } else {
                min = minX(col) - prevCol.maxX;
            }
            if (j == board.getStackedColumns().size() - 1) {
                max = 500;
            } else {
                int maxX = maxX(col);
                max = ((minX(nextCol) + maxX) / 2) - maxX;
            }
            board.getStackedColumns().get(j).minX = Math.max(0, minX(board.getStackedColumns().get(j)) - min);
            board.getStackedColumns().get(j).maxX = Math.min(imageWidth, maxX(board.getStackedColumns().get(j)) + max);
            board.getStackedColumns().get(j).minY = 0;
            board.getStackedColumns().get(j).maxY = imageHeight;
        }
    }

    private void drawSquare(Bitmap bitmap, int x1, int x2, int y1, int y2, int color, int alpha) {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        Matrix matrix = new Matrix();
        canvas.drawRect(x1, y1, x2, y2, paint);
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    private void drawSeparators(Bitmap bitmap, float x, int color) {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        Matrix matrix = new Matrix();
        float endY = bitmap.getHeight();
        canvas.drawLine(x, 0, x + 10, 0, paint);//up
        canvas.drawLine(x, 0, x, endY, paint);//left
        canvas.drawLine(x, endY, x + 10, endY, paint);//bottom
        canvas.drawLine(x + 10, 0, x + 10, endY, paint);//right
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    public boolean isDebug() {
        return debug;
    }
}
