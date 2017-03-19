package com.fullmob.printable;


import android.graphics.Color;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by shehabic on 19/03/2017.
 */
public class Element {

    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String CENTER = "center";
    @StringDef({LEFT, CENTER, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextHAlign {}

    public static final String TOP = "top";
    public static final String MIDDLE = "middle";
    public static final String BOTTOM = "bottom";
    @StringDef({TOP, MIDDLE, BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextVAlign {}

    public static final String TYPE_QR = "qr";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_PARENT = "parent";
    public static final String TYPE_CUSTOM = "custom";
    @StringDef({TYPE_QR, TYPE_IMAGE, TYPE_TEXT, TYPE_PARENT, TYPE_CUSTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ElementType {}

    public Element below = null;
    public Element above = null;
    public Element toLeftOf = null;
    public Element toRightOf = null;
    public Element centerVerticalIn = null;
    public Element centerHorizontalIn = null;
    public Element alignBottom = null;
    public Element alignRight = null;
    public Element alignLeft = null;
    public Element alignTop = null;
    public Element textSizeMultiplier = null;
    private boolean fixedHeight = false;
    private boolean fixedWidth = false;
    public int left = -1;
    public int right = -1;
    public int top = -1;
    public int bottom = -1;
    public int width = -1;
    public int height = -1;
    public int marginLeft = 0;
    public int marginRight = 0;
    public int marginTop = 0;
    public int marginBottom = 0;
    public @TextHAlign String textHAlign = CENTER;
    public @TextVAlign String textVAlign = MIDDLE;
    public float textSize = 20;
    public int textColor = Color.BLACK;
    public final @ElementType String type;
    public String content;
    public String id;
    private Element parent;

    public Element(@ElementType String type) {
        this(type, null);
    }

    public Element(@ElementType String type, String content) {
        this.type = type;
        this.content = content;
    }

    public void setFixedWith(int width) {
        this.width = width;
        fixedWidth = true;
    }

    public void setFixedHeight(int height) {
        this.height = height;
        fixedHeight = true;
    }

    public void setMargins(int margin) {
        marginLeft = marginBottom = marginTop = marginRight = margin;
    }

    public boolean hasFixedHeight() {
        return fixedHeight;
    }

    public boolean hasFixedWidth() {
        return fixedWidth;
    }

    public boolean isImage() {
        return type.equals(TYPE_IMAGE);
    }

    public boolean isQr() {
        return type.equals(TYPE_QR);
    }

    public boolean isText() {
        return type.equals(TYPE_TEXT);
    }

    public boolean isParent() {
        return type.equals(TYPE_PARENT);
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public Element getParent() {
        return parent;
    }

    public void setAttribute(String attribute, Object val) {
        switch (attribute) {
            case "id":
                id = (String) val;
                break;

            case "width":
                width = getIntVal(val);
                break;

            case "height":
                height = getIntVal(val);
                break;

            case "marginLeft":
                marginLeft = getIntVal(val);
                break;

            case "marginTop":
                marginTop = getIntVal(val);
                break;

            case "marginRight":
                marginRight = getIntVal(val);
                break;

            case "marginBottom":
                marginBottom = getIntVal(val);
                break;

            case "textSize":
                textSize = getFloatVal(val);
                break;

            case "textColor":
                textColor = getIntVal(val);
                break;

            case "alignLeft":
                alignLeft = (Element) val;
                break;

            case "alignRight":
                alignRight = (Element) val;
                break;

            case "alignBottom":
                alignBottom = (Element) val;
                break;

            case "alignTop":
                alignTop = (Element) val;
                break;

            case "below":
                below = (Element) val;
                break;

            case "above":
                above = (Element) val;
                break;

            case "toLeftOf":
                toLeftOf = (Element) val;
                break;

            case "toRightOf":
                toRightOf = (Element) val;
                break;

            case "content":
                content = (String) val;
                break;

            case "centerHorizontalIn":
                centerHorizontalIn = (Element) val;
                break;

            case "centerVerticalIn":
                centerVerticalIn = (Element) val;
                break;

            case "textHAlign":
                textHAlign = (String) val;
                break;

            case "textVAlign":
                textVAlign = (String) val;
                break;

            case "type":
                //ignore here
                break;

            default:
                throw new RuntimeException("Unsupported Printable Element attribute: " + attribute);

        }
    }

    private int getIntVal(Object val) {
        return Integer.valueOf(String.valueOf(val));
    }

    private float getFloatVal(Object val) {
        return Float.valueOf(String.valueOf(val));
    }
}
