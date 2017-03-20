package com.fullmob.printable.drawers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.fullmob.printable.Element;

/**
 * Created by shehabic on 19/03/2017.
 */
public class ImageElementDrawer implements ElementDrawer {

    @Override
    public void onDraw(Canvas canvas, Element element) {
        try {
            Paint paintOverlay = new Paint(Paint.FILTER_BITMAP_FLAG);
            Rect rect = new Rect(element.left, element.top, element.right, element.bottom);
            canvas.drawBitmap(element.imageBitmap, rect, rect, paintOverlay);
        } catch (Exception e) {
            throw new PrintableException("Could not onDraw QR Element");
        }
    }

    @Override
    public void requestLayout(Element element) {
    }
}
