package com.fullmob.printable.drawers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.fullmob.printable.Element;
import com.fullmob.printable.exceptions.PrintableException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by shehabic on 19/03/2017.
 */
public class QRDrawer implements ElementDrawer {

    @Override
    public void onDraw(Canvas canvas, Element element) {
        try {
            Bitmap qrBmp = encodeAsBitmap(element.content, element.width);
            Paint paintOverlay = new Paint(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(qrBmp, element.left, element.top, paintOverlay);
        } catch (Exception e) {
            throw new PrintableException("Could not onDraw QR Element");
        }
    }

    @Override
    public void requestLayout(Element element) {
        element.height = element.width = (int) (element.getParent().height * 0.7f);
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
}
