package com.fullmob.printable.drawers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.fullmob.printable.Element;

/**
 * Created by shehabic on 19/03/2017.
 */
public class TextElementDrawer implements ElementDrawer {
    @Override
    public void draw(Canvas canvas, Element element) {
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(element.textColor);
        textPaint.setTextSize(
            element.textSize * (element.textSizeMultiplier != null ? element.textSizeMultiplier.width : 1.f)
        );
        textPaint.setTextAlign(
            element.textHAlign.equals(Element.CENTER)
                ? Paint.Align.CENTER
                : element.textHAlign.equals(Element.LEFT)
                    ? Paint.Align.LEFT
                    : Paint.Align.RIGHT
        );
        textPaint.setAntiAlias(true);
        StaticLayout textLayout = new StaticLayout(
            element.content,
            textPaint,
            element.width,
            Layout.Alignment.ALIGN_NORMAL,
            1,
            0,
            false
        );
        int actualTop = element.top;
        float actualTextHeight = textLayout.getHeight();
        if (element.textVAlign.equals(Element.MIDDLE)) {
            actualTop = ((element.top + element.bottom) / 2) - (int) (actualTextHeight / 2);
        } else if (element.textVAlign.equals(Element.BOTTOM)) {
            actualTop = element.bottom - (int) actualTextHeight;
        }

        int actualLeft = element.left;
        if (element.textHAlign.equals(Element.CENTER)) {
            actualLeft = (element.left + element.right) / 2;
        } else if (element.textHAlign.equals(Element.RIGHT)) {
            actualLeft = element.right;
        }

        canvas.save();
        canvas.translate(actualLeft, actualTop);
        textLayout.draw(canvas);
        canvas.restore();
    }

    @Override
    public void layout(Element element) {

    }
}
