package com.fullmob.printable;


import android.graphics.Color;

public class Element {
    public enum TextHAlign {LEFT, CENTER, RIGHT}

    public enum TextVAlign {TOP, MIDDLE, BOTTOM}

    public enum Type {QR, IMAGE, TEXT, PARENT}

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
    public TextHAlign textHAlign = TextHAlign.CENTER;
    public TextVAlign textVAlign = TextVAlign.MIDDLE;
    public float textSize = 20;
    public int textColor = Color.BLACK;
    public final Type type;
    public String content;
    public String id;
    private Element parent;

    public Element(String type) {
        this(extractSecType(type), null);
    }

    public Element(Type type) {
        this(type, null);
    }

    public Element(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    public Element(String type, String content) {
        this(extractSecType(type), content);
    }

    private static Type extractSecType(String type) {
        switch (type.toUpperCase()) {
            case "QR":
                return Type.QR;

            case "IMAGE":
                return Type.IMAGE;

            case "TEXT":
                return Type.TEXT;

            case "PARENT":
                return Type.PARENT;

            default:
                throw new RuntimeException(String.format("Unsupported type '%f' ", (type != null ? type : "empty")));
        }
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
        return type.equals(Type.IMAGE);
    }

    public boolean isQr() {
        return type.equals(Type.QR);
    }

    public boolean isText() {
        return type.equals(Type.TEXT);
    }

    public boolean isParent() {
        return type.equals(Type.PARENT);
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
                textHAlign = getTextHAlign(val);
                break;

            case "textVAlign":
                textVAlign = getTextVAlign(val);
                break;

            case "type":
                //ignore here
                break;

            default:
                throw new RuntimeException("Unsupported Printable Element attribute: " + attribute);

        }
    }

    private TextHAlign getTextHAlign(Object val) {
        switch (((String) val).toUpperCase()) {
            case "CENTER":
                return TextHAlign.CENTER;
            case "LEFT":
                return TextHAlign.LEFT;
            case "RIGHT":
                return TextHAlign.RIGHT;
            default:
                throw new RuntimeException("Unsupported TextHAlign value: " + val);
        }
    }

    private TextVAlign getTextVAlign(Object val) {
        switch (((String) val).toUpperCase()) {
            case "MIDDLE":
                return TextVAlign.MIDDLE;
            case "BOTTOM":
                return TextVAlign.BOTTOM;
            case "TOP":
                return TextVAlign.TOP;
            default:
                throw new RuntimeException("Unsupported TextVAlign value: " + val);
        }
    }

    private int getIntVal(Object val) {
        return Integer.valueOf(String.valueOf(val));
    }

    private float getFloatVal(Object val) {
        return Float.valueOf(String.valueOf(val));
    }
}
