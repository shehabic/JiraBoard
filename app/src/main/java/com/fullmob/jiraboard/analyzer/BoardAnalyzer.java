package com.fullmob.jiraboard.analyzer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.fullmob.jiraboard.data.Column;
import com.fullmob.jiraboard.data.Point;
import com.fullmob.jiraboard.data.Board;
import com.fullmob.jiraboard.data.Ticket;
import com.fullmob.jiraboard.utils.Identifier;
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
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

import static com.fullmob.jiraboard.utils.QRPointUtils.maxX;
import static com.fullmob.jiraboard.utils.QRPointUtils.minX;

public class BoardAnalyzer {

    private boolean debug;
    public BoardAnalyzer() {
        this(false);
    }

    public BoardAnalyzer(boolean debug) {
        this.debug = debug;
    }

    public Observable<Board> analyzeProject(final Board project) {
        return Observable.fromCallable(new Callable<Board>() {
            @Override
            public Board call() throws Exception {
                return analyzeProjectFromImage(project);
            }
        });
    }

    public Board analyzeProjectFromImage(Board project) {
        readQRImage(project.getBitmap(), project);
        return project;
    }

    public void readQRImage(Bitmap bMap, Board project) {

        Bitmap mutableBitmap = bMap.copy(Bitmap.Config.ARGB_8888, true);

        int[] intArray = new int[mutableBitmap.getWidth() * mutableBitmap.getHeight()];

        mutableBitmap.getPixels(intArray, 0, mutableBitmap.getWidth(), 0, 0, mutableBitmap.getWidth(), mutableBitmap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(mutableBitmap.getWidth(), mutableBitmap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        List<Column> ticketList = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        QRCodeMultiReader qrCodeMultiReader = new QRCodeMultiReader();
        try {
            Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            Result[] results = qrCodeMultiReader.decodeMultiple(bitmap, hints);
            for (Result r : results) {
                String text = r.getText();
                Ticket t = new Ticket();
                t.text = text;
                t.points = new Point[3];
                for (int i = 0; i < 3; i++) {
                    t.points[i] = new Point(r.getResultPoints()[i].getX(), r.getResultPoints()[i].getY());
                }
                Identifier identifier = new Identifier(project);
                if (identifier.isColumn(text)) {
                    columns.add(t);
                } else {
                    ticketList.add(t);
                }
            }
            project.setColumns(columns);
            orderColumnsAndTickets(project, ticketList, mutableBitmap);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void orderColumnsAndTickets(Board project, List<Column> ticketList, Bitmap mutableBitmap) {
        int ImageWidth = mutableBitmap.getWidth();
        prepareColumns(project, ImageWidth);
        for (int i = 0; i < ticketList.size(); i++) {
            Column ticket = ticketList.get(i);
            int centerX = (minX(ticket) + maxX(ticket)) / 2;
            ticketList.get(i).centerX = centerX;
            for (int j = 0; j < project.getColumns().size(); j++) {
                Column col = project.getColumns().get(j);
                if (centerX > col.minX && centerX <= col.maxX) {
                    col.tickets.add(ticketList.get(i));
                }
            }
        }
        if (isDebug()) {
            drawDebugLines(project, mutableBitmap);
            project.setBitmap(mutableBitmap);
        }
    }

    private void drawDebugLines(Board project, Bitmap mutableBitmap) {
        for (int i = 0; i < project.getColumns().size(); i++) {
            Column col = project.getColumns().get(i);
            drawSeparators(mutableBitmap, col.minX, Color.GREEN);
            drawSeparators(mutableBitmap, col.maxX, Color.GREEN);

            drawSeparators(mutableBitmap, minX(col), Color.RED);
            drawSeparators(mutableBitmap, maxX(col), Color.RED);
        }
    }

    private void prepareColumns(Board project, int imageWidth) {
        Collections.sort(project.getColumns(), new Comparator<Column>() {
            @Override
            public int compare(Column c1, Column c2) {
                return minX(c1) - minX(c2);
            }
        });
        int min;
        int max;
        for (int j = 0; j < project.getColumns().size(); j++) {
            Column col = project.getColumns().get(j);
            Column prevCol = j > 0 ? project.getColumns().get(j - 1) : null;
            Column nextCol = j < (project.getColumns().size() - 1) ? project.getColumns().get(j + 1) : null;
            if (prevCol == null) {
                min = 500;
            } else {
                min = minX(col) - prevCol.maxX;
            }
            if (j == project.getColumns().size() - 1) {
                max = 500;
            } else {
                int maxX = maxX(col);
                max = ((minX(nextCol) + maxX) / 2) - maxX;
            }
            project.getColumns().get(j).minX = Math.max(0, minX(project.getColumns().get(j)) - min);
            project.getColumns().get(j).maxX = Math.min(imageWidth, maxX(project.getColumns().get(j)) + max);
        }
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
        canvas.drawLine(x, 0, x+10, 0, paint);//up
        canvas.drawLine(x, 0, x, endY, paint);//left
        canvas.drawLine(x, endY, x+10, endY, paint);//bottom
        canvas.drawLine(x+10, 0, x+10, endY, paint);//right
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    public boolean isDebug() {
        return debug;
    }
}
