package com.fullmob.printable.drawers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.fullmob.printable.Element;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by shehabic on 25/03/2017.
 */
public class DrawableDrawer implements ElementDrawer {

    private WeakReference<Context> context;

    public DrawableDrawer(Context context) {
        this.context = new WeakReference<>(context);
    }

    private Drawable getDrawableFromUri(Uri uri) {
        if (context.get() == null) return null;
        try {
            InputStream inputStream = context.get().getContentResolver().openInputStream(uri);
            return Drawable.createFromStream(inputStream, uri.toString() );
        } catch (FileNotFoundException e) {
        }

        return null;
    }

    @Override
    public void onDraw(Canvas canvas, Element element) {
        Drawable drawable = getDrawableFromUri(element.drawableUri);
        if (drawable != null) {
            drawable.setBounds(element.left, element.top, element.right, element.bottom);
            drawable.draw(canvas);
        }
    }

    @Override
    public void requestLayout(Element element) {
    }
}
