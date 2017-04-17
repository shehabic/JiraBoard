package com.fullmob.printable.generators;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.fullmob.printable.Printable;

/**
 * Created by shehabic on 19/03/2017.
 */
public class PrintableImageGenerator extends AbstractPrintableGenerator<Bitmap> {

    public PrintableImageGenerator(Context context) {
        super(context);
    }

    public Bitmap createPrintable(Printable printable, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);

        drawPrintableInCanvas(printable, width, height, canvas);

        return bitmap;
    }
}