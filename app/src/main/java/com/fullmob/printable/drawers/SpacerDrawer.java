package com.fullmob.printable.drawers;

import android.graphics.Canvas;

import com.fullmob.printable.Element;

/**
 * Created by shehabic on 19/03/2017.
 */
public class SpacerDrawer implements ElementDrawer {

    @Override
    public void onDraw(Canvas canvas, Element element) {
        // Do nothing, this is just to adjust measurement but doesn't really draw anything
    }

    @Override
    public void requestLayout(Element element) {
    }
}
