package com.fullmob.jiraboard.printing;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class ImageTicketGenerator extends AbstractPrintableGenerator<Bitmap> {

    public Bitmap createPrintable(Printable printable, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawPrintableInCanvas(printable, width, height, canvas);

        return bitmap;
    }
}