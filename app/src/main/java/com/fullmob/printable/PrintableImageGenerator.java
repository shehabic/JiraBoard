package com.fullmob.printable;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by shehabic on 19/03/2017.
 */
public class PrintableImageGenerator extends AbstractPrintableGenerator<Bitmap> {

    public Bitmap createPrintable(Printable printable, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawPrintableInCanvas(printable, width, height, canvas);

        return bitmap;
    }
}