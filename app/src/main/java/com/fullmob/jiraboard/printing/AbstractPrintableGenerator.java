package com.fullmob.jiraboard.printing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashSet;

abstract class AbstractPrintableGenerator<T> implements PrintableTicketGenerator<T> {

    float scale = 1f;

    HashSet<Printable.Element> drawn = new HashSet<>();

    public T createPrintable(Printable printable, PaperSize paperSize) {
        int h = 0, w = 0;
        switch (paperSize) {
            case A6:
                h = 4130;
                w = 5830;
                break;

            case A7:
                h = 2910;
                w = 4130;
                break;

            case A8:
                h = 2050;
                w = 2910;
                break;

            case A9:
                h = 1460;
                w = 2050;
                break;

            case A10:
                h = 1020;
                w = 1460;
                break;
        }

        return createPrintable(printable, w / 2, h / 2);
    }

    public void prepareSectionDimension(Printable.Element section) {
        if (section.toLeftOf != null) {
            if (section.toLeftOf.isMeasured) {
                section.right = section.toLeftOf.left - section.marginRight;
            }
        }
        if (section.toRightOf != null) {
            if (section.toRightOf.isMeasured) {
                section.left = section.toRightOf.right + section.marginLeft;
            }
        }
        if (section.below != null) {
            if (section.below.isMeasured) {
                section.top = section.below.bottom + section.marginTop;
            }
        }
        if (section.above != null) {
            if (section.above.isMeasured) {
                section.bottom = section.above.top;
            }
        }
        if (section.alignBottom != null) {
            if (section.alignBottom.isMeasured) {
                section.bottom = section.alignBottom.bottom - section.marginBottom;
            }
        }
        if (section.alignTop != null) {
            if (section.alignTop.isMeasured) {
                section.top = section.alignTop.top + section.marginTop;
            }
        }
        if (section.alignLeft != null) {
            if (section.alignLeft.isMeasured) {
                section.left = section.alignLeft.left + section.marginLeft;
            }
        }
        if (section.alignRight != null) {
            if (section.alignRight.isMeasured) {
                section.right = section.alignRight.right - section.marginRight;
            }
        }

        if (section.centerHorizontalOf != null) {
            if (section.centerHorizontalOf.isMeasured) {
                if (section.width > -1) {
                    section.left = ((section.centerHorizontalOf.left + section.centerHorizontalOf.right) / 2);
                    section.left -= (section.width / 2);
                    section.left += (section.marginLeft - section.marginRight);
                } else {
                    // TODO: determine how to measure width and get left
                }
            }
        }
        if (section.centerVerticalOf != null) {
            if (section.centerVerticalOf.isMeasured) {
                if (section.height > -1) {
                    section.top = ((section.centerVerticalOf.top + section.centerVerticalOf.bottom) / 2);
                    section.top -= (section.height / 2);
                    section.top += (section.marginTop - section.marginBottom);
                } else {
                    // TODO: determine how to measure width and get left
                }
            }
        }
        if (section.width != -1) {
            if (section.right == -1) {
                section.right = section.left + section.width;
            } else if (section.left == -1) {
                section.left = section.right - section.width;
            }
        } else if (section.right != -1 && section.left != -1) {
            section.width = section.right - section.left;
        }
        if (section.height != -1) {
            if (section.bottom == -1) {
                section.bottom = section.top + section.height;
            } else if (section.top == -1) {
                section.top = section.bottom - section.height;
            }
        } else if (section.top != -1 && section.bottom != -1) {
            section.height = section.bottom - section.top;
        }
        if (section.height > -1 && section.width > -1) {
            section.isMeasured = true;
        }
    }

    @Override
    public void prepareParent(Printable printable, int width, int height) {
        printable.PARENT.left = 0;
        printable.PARENT.top = 0;
        printable.PARENT.width = width;
        printable.PARENT.height = height;
        printable.PARENT.right = width;
        printable.PARENT.bottom = height;
        printable.PARENT.isMeasured = true;
    }

    private void prepareQRSection(Printable.Element qr, Printable.Element parent) {
        qr.height = qr.width = (int) (parent.height * 0.7f);
    }

    private void drawTextSection(Canvas canvas, Printable.Element section) {
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(section.textColor);
        textPaint.setTextSize(
            section.textSize * (section.textSizeMultiplier != null ? section.textSizeMultiplier.width : 1.f)
        );
        textPaint.setTextAlign(
            section.textHAlign == Printable.TextHAlign.CENTER
                ? Paint.Align.CENTER
                : section.textHAlign == Printable.TextHAlign.LEFT
                    ? Paint.Align.LEFT
                    : Paint.Align.RIGHT
        );
        textPaint.setAntiAlias(true);
        StaticLayout textLayout = new StaticLayout(
            section.content,
            textPaint,
            section.width,
            Layout.Alignment.ALIGN_NORMAL,
            1,
            0,
            false
        );
        int actualTop = section.top;
        float actualTextHeight = textLayout.getHeight();
        if (section.textVAlign.equals(Printable.TextVAlign.MIDDLE)) {
            actualTop = ((section.top + section.bottom) / 2) - (int) (actualTextHeight / 2);
        } else if (section.textVAlign.equals(Printable.TextVAlign.BOTTOM)) {
            actualTop = section.bottom - (int) actualTextHeight;
        }

        int actualLeft = section.left;
        if (section.textHAlign.equals(Printable.TextHAlign.CENTER)) {
            actualLeft = (section.left + section.right) / 2;
        } else if (section.textHAlign.equals(Printable.TextHAlign.RIGHT)) {
            actualLeft = section.right;
        }

        canvas.save();
        canvas.translate(actualLeft, actualTop);
        textLayout.draw(canvas);
        canvas.restore();
    }


    void drawSection(Canvas canvas, Printable.Element section) {
        if (section.isQr()) {
            drawQR(canvas, section);
            drawn.add(section);
        } else if (section.isText()) {
            drawTextSection(canvas, section);
            drawn.add(section);
        }
    }

    private void drawQR(Canvas canvas, Printable.Element section) {
        try {
            Bitmap qrBmp = encodeAsBitmap(section.content, section.width);
            Paint paintOverlay = new Paint(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(qrBmp, section.left, section.top, paintOverlay);
        } catch (Exception e) {

        }
    }

    private Bitmap encodeAsBitmap(String str, int side) throws WriterException {
        BitMatrix result;
        Bitmap bitmap = null;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, side, side, null);
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[1 + (w * h)];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);

        } catch (Exception iae) {
            iae.printStackTrace();
        }

        return bitmap;
    }

    void drawPrintableInCanvas(Printable printable, int width, int height, Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, width, height, paint);
        prepareParent(printable, width, height);
        boolean allDrawn = false;
        while (!allDrawn) {
            allDrawn = true;
            for (Printable.Element section : printable.elements) {
                allDrawn &= drawn.contains(section);
                if (!drawn.contains(section)) {
                    if (section.isQr()) {
                        prepareQRSection(section, printable.PARENT);
                    }
                    if (!section.isMeasured) {
                        prepareSectionDimension(section);
                    }
                    if (section.isMeasured) {
                        drawSection(canvas, section);
                    }
                }
            }
        }
        drawn.clear();
    }

    public void resetMeasures(Printable printable) {
        for (Printable.Element element : printable.elements) {
            if (!element.isParent()) {
                if (!element.hasFixedHeight()) {
                    element.height = -1;
                    element.isMeasured = false;
                }
                if (!element.hasFixedWidth()) {
                    element.width = -1;
                    element.isMeasured = false;
                }
                element.left = -1;
                element.right = -1;
                element.top = -1;
                element.bottom = -1;
            }
        }
    }
}
