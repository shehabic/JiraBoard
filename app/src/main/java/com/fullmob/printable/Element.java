package com.fullmob.printable;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.StringDef;
import android.support.v4.util.ArrayMap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by shehabic on 19/03/2017.
 */
public class Element {
    // Horizontal alignment
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String CENTER = "center";
    @StringDef({LEFT, CENTER, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextHAlign { }

    // Vertical alignment
    public static final String TOP = "top";
    public static final String MIDDLE = "middle";
    public static final String BOTTOM = "bottom";
    @StringDef({TOP, MIDDLE, BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextVAlign { }

    // Element types
    public static final String TYPE_QR = "qr";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_PARENT = "parent";
    public static final String TYPE_CUSTOM = "custom";
    @StringDef({TYPE_QR, TYPE_IMAGE, TYPE_TEXT, TYPE_PARENT, TYPE_CUSTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ElementType { }

    // Relative dimensions
    public static final String RELATIVE_FIELD_WIDTH = "width";
    public static final String RELATIVE_FIELD_HEIGHT = "height";
    @StringDef({RELATIVE_FIELD_WIDTH, RELATIVE_FIELD_HEIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RelativeField { }


    public String subType = null;
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
    public Bitmap imageBitmap = null;
    public Uri drawableUri;
    ArrayMap<String, RelativeDimen> relativeDimenRules;

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
        this(type, null, null);
    }

    public Element(@ElementType String type, String content) {
        this(type, null, content);
    }

    public Element(@ElementType String type, String customType, String content) {
        if (type.equals(TYPE_CUSTOM) && null == customType) {
            throw new PrintableException(
                "When using element type 'custom' type, you must supply customType='YourCustomType' "
            );
        }
        this.type = type;
        this.content = content;
        this.subType = customType;
        this.relativeDimenRules = new ArrayMap<>();
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

    public boolean isCustom() {
        return type.equals(TYPE_CUSTOM);
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
                validateRelativity((Element) val);
                break;

            case "alignRight":
                alignRight = (Element) val;
                validateRelativity((Element) val);
                break;

            case "alignBottom":
                alignBottom = (Element) val;
                validateRelativity((Element) val);
                break;

            case "alignTop":
                alignTop = (Element) val;
                validateRelativity((Element) val);
                break;

            case "below":
                below = (Element) val;
                validateRelativity((Element) val);
                break;

            case "above":
                above = (Element) val;
                validateRelativity((Element) val);
                break;

            case "toLeftOf":
                toLeftOf = (Element) val;
                validateRelativity((Element) val);
                break;

            case "toRightOf":
                toRightOf = (Element) val;
                validateRelativity((Element) val);
                break;

            case "content":
                content = (String) val;
                break;

            case "centerHorizontalIn":
                centerHorizontalIn = (Element) val;
                validateRelativity((Element) val);
                break;

            case "centerVerticalIn":
                centerVerticalIn = (Element) val;
                validateRelativity((Element) val);
                break;

            case "textHAlign":
                textHAlign = (String) val;
                break;

            case "textVAlign":
                textVAlign = (String) val;
                break;

            case "textSizeMultiplier":
                textSizeMultiplier = (Element) val;
                break;

            case "type":
                //ignore here
                break;

            //FixMe: This is memory inefficient, change to uri
            case "imageBitmap":
                imageBitmap = (Bitmap) val;
                break;

            case "relativeWidthTarget":
                if (!relativeDimenRules.containsKey(RELATIVE_FIELD_WIDTH)) {
                    relativeDimenRules.put(RELATIVE_FIELD_WIDTH, new RelativeDimen());
                }
                relativeDimenRules.get(RELATIVE_FIELD_WIDTH).target = (Element) val;
                validateRelativity(relativeDimenRules.get(RELATIVE_FIELD_WIDTH), RELATIVE_FIELD_WIDTH);
                break;

            case "relativeWidthField":
                if (!relativeDimenRules.containsKey(RELATIVE_FIELD_WIDTH)) {
                    relativeDimenRules.put(RELATIVE_FIELD_WIDTH, new RelativeDimen());
                }
                relativeDimenRules.get(RELATIVE_FIELD_WIDTH).field = (String) val;
                validateRelativity(relativeDimenRules.get(RELATIVE_FIELD_WIDTH), RELATIVE_FIELD_WIDTH);
                break;

            case "relativeWidthValue":
                if (!relativeDimenRules.containsKey(RELATIVE_FIELD_WIDTH)) {
                    relativeDimenRules.put(RELATIVE_FIELD_WIDTH, new RelativeDimen());
                }
                relativeDimenRules.get(RELATIVE_FIELD_WIDTH).value = (float) val;
                validateRelativity(relativeDimenRules.get(RELATIVE_FIELD_WIDTH), RELATIVE_FIELD_WIDTH);
                break;

            case "relativeHeightTarget":
                if (!relativeDimenRules.containsKey(RELATIVE_FIELD_HEIGHT)) {
                    relativeDimenRules.put(RELATIVE_FIELD_HEIGHT, new RelativeDimen());
                }
                relativeDimenRules.get(RELATIVE_FIELD_HEIGHT).target = (Element) val;
                validateRelativity(relativeDimenRules.get(RELATIVE_FIELD_HEIGHT), RELATIVE_FIELD_HEIGHT);
                break;

            case "relativeHeightField":
                if (!relativeDimenRules.containsKey(RELATIVE_FIELD_HEIGHT)) {
                    relativeDimenRules.put(RELATIVE_FIELD_HEIGHT, new RelativeDimen());
                }
                relativeDimenRules.get(RELATIVE_FIELD_HEIGHT).field = (String) val;
                validateRelativity(relativeDimenRules.get(RELATIVE_FIELD_HEIGHT), RELATIVE_FIELD_HEIGHT);
                break;

            case "relativeHeightValue":
                if (!relativeDimenRules.containsKey(RELATIVE_FIELD_HEIGHT)) {
                    relativeDimenRules.put(RELATIVE_FIELD_HEIGHT, new RelativeDimen());
                }
                relativeDimenRules.get(RELATIVE_FIELD_HEIGHT).value = (float) val;
                validateRelativity(relativeDimenRules.get(RELATIVE_FIELD_HEIGHT), RELATIVE_FIELD_HEIGHT);
                break;


            default:
                throw new PrintableException("Unsupported Printable Element attribute: " + attribute);

        }
    }

    private void validateRelativity(Element relativeElement) {
        if (alignLeft == this) {
            throw new PrintableException(
                String.format("Element with %s id cannot be measured relative to itself", id)
            );
        }
    }

    private void validateRelativity(RelativeDimen relativeDimen, @RelativeField  String field) {
        if (relativeDimen.target == this && relativeDimen.field != null && relativeDimen.equals(field)) {
            throw new PrintableException(
                String.format("Element with %s id cannot be measured relative to itself", id)
            );
        }
    }

    private int getIntVal(Object val) {
        return Integer.valueOf(String.valueOf(val));
    }

    private float getFloatVal(Object val) {
        return Float.valueOf(String.valueOf(val));
    }

    public static class RelativeDimen {
        public Element target;
        public @RelativeField String field;
        public float value;
    }
}
