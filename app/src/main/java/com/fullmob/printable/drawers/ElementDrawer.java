package com.fullmob.printable.drawers;

import android.graphics.Canvas;

import com.fullmob.printable.Element;

/**
 * Created by shehabic on 19/03/2017.
 */
public interface ElementDrawer {
    void draw(Canvas canvas, Element element);
    void layout(Element element);
}
