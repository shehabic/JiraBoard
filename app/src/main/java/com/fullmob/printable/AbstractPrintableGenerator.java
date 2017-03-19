package com.fullmob.printable;

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

abstract class AbstractPrintableGenerator<T> implements PrintableGenerator<T> {

    float scale = 1f;

    private HashSet<Element> drawn = new HashSet<>();
    private HashSet<Element> measured = new HashSet<>();

    public T createPrintable(Printable printable, PaperSize paperSize) {
        int[] size = findSize(paperSize, true);

        return createPrintable(printable, size[0] / 2, size[1] / 2);
    }

    public static int[] findSize(String sizeString, boolean landscape) {
        switch (sizeString.toUpperCase()) {
            case "A5":
                return findSize(PaperSize.A5, landscape);
            case "A6":
                return findSize(PaperSize.A6, landscape);
            case "A7":
                return findSize(PaperSize.A7, landscape);
            case "A8":
                return findSize(PaperSize.A8, landscape);
            case "A9":
                return findSize(PaperSize.A9, landscape);
            case "A10":
                return findSize(PaperSize.A10, landscape);
            default:
                throw new RuntimeException("Unsupported paper size: " + sizeString);
        }
    }

    public static int[] findSize(PaperSize paperSize, boolean landscape) {
        int h = 0, w = 0;
        switch (paperSize) {
            // TODO: add the missing sizes

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
        return landscape ? new int[]{w, h} : new int[]{h, w};
    }

    public void prepareSectionDimension(Element section) {
        if (section.toLeftOf != null) {
            if (measured.contains(section.toLeftOf)) {
                section.right = section.toLeftOf.left - section.marginRight;
            }
        }
        if (section.toRightOf != null) {
            if (measured.contains(section.toRightOf)) {
                section.left = section.toRightOf.right + section.marginLeft;
            }
        }
        if (section.below != null) {
            if (measured.contains(section.below)) {
                section.top = section.below.bottom + section.marginTop;
            }
        }
        if (section.above != null) {
            if (measured.contains(section.above)) {
                section.bottom = section.above.top;
            }
        }
        if (section.alignBottom != null) {
            if (measured.contains(section.alignBottom)) {
                section.bottom = section.alignBottom.bottom - section.marginBottom;
            }
        }
        if (section.alignTop != null) {
            if (measured.contains(section.alignTop)) {
                section.top = section.alignTop.top + section.marginTop;
            }
        }
        if (section.alignLeft != null) {
            if (measured.contains(section.alignLeft)) {
                section.left = section.alignLeft.left + section.marginLeft;
            }
        }
        if (section.alignRight != null) {
            if (measured.contains(section.alignRight)) {
                section.right = section.alignRight.right - section.marginRight;
            }
        }

        if (section.centerHorizontalIn != null) {
            if (measured.contains(section.centerHorizontalIn)) {
                if (section.width > -1) {
                    section.left = ((section.centerHorizontalIn.left + section.centerHorizontalIn.right) / 2);
                    section.left -= (section.width / 2);
                    section.left += (section.marginLeft - section.marginRight);
                } else {
                    // TODO: determine how to measure width and get left
                }
            }
        }
        if (section.centerVerticalIn != null) {
            if (measured.contains(section.centerVerticalIn)) {
                if (section.height > -1) {
                    section.top = ((section.centerVerticalIn.top + section.centerVerticalIn.bottom) / 2);
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
            measured.add(section);
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
        measured.add(printable.PARENT);
    }

    private void prepareQRSection(Element qr, Element parent) {
        qr.height = qr.width = (int) (parent.height * 0.7f);
    }

    private void drawTextSection(Canvas canvas, Element section) {
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(section.textColor);
        textPaint.setTextSize(
            section.textSize * (section.textSizeMultiplier != null ? section.textSizeMultiplier.width : 1.f)
        );
        textPaint.setTextAlign(
            section.textHAlign == Element.TextHAlign.CENTER
                ? Paint.Align.CENTER
                : section.textHAlign == Element.TextHAlign.LEFT
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
        if (section.textVAlign.equals(Element.TextVAlign.MIDDLE)) {
            actualTop = ((section.top + section.bottom) / 2) - (int) (actualTextHeight / 2);
        } else if (section.textVAlign.equals(Element.TextVAlign.BOTTOM)) {
            actualTop = section.bottom - (int) actualTextHeight;
        }

        int actualLeft = section.left;
        if (section.textHAlign.equals(Element.TextHAlign.CENTER)) {
            actualLeft = (section.left + section.right) / 2;
        } else if (section.textHAlign.equals(Element.TextHAlign.RIGHT)) {
            actualLeft = section.right;
        }

        canvas.save();
        canvas.translate(actualLeft, actualTop);
        textLayout.draw(canvas);
        canvas.restore();
    }


    private void drawSection(Canvas canvas, Element section) {
        if (section.isQr()) {
            drawQR(canvas, section);
            drawn.add(section);
        } else if (section.isText()) {
            drawTextSection(canvas, section);
            drawn.add(section);
        }
    }

    private void drawQR(Canvas canvas, Element section) {
        try {
            Bitmap qrBmp = encodeAsBitmap(section.content, section.width);
            Paint paintOverlay = new Paint(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(qrBmp, section.left, section.top, paintOverlay);
        } catch (Exception e) {
            e.printStackTrace();
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
            for (Element section : printable.elements) {
                allDrawn &= drawn.contains(section);
                if (!drawn.contains(section)) {
                    if (section.isQr()) {
                        prepareQRSection(section, printable.PARENT);
                    }
                    if (!measured.contains(section)) {
                        prepareSectionDimension(section);
                    }
                    if (measured.contains(section)) {
                        drawSection(canvas, section);
                    }
                }
            }
        }
        drawn.clear();
        measured.clear();
    }
}
