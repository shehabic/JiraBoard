package com.fullmob.jiraboard.printing;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class Printable {

    public final Element PARENT;
    public enum TextHAlign { LEFT, CENTER, RIGHT }
    public enum TextVAlign { TOP, MIDDLE, BOTTOM }
    public enum SecType { QR, IMAGE, TEXT, PARENT }

    public final List<Element> elements;

    public Printable() {
        elements = new ArrayList<>();
        PARENT = new Element(SecType.PARENT, "");
    }

    public void addSection(Element section) {
        elements.add(section);
    }

    public static class Element extends Attributes {
        public final SecType type;
        public final String content;

        public Element(SecType type, String content) {
            this.type = type;
            this.content = content;

        }

        public boolean isImage() {
            return type.equals(SecType.IMAGE);
        }

        public boolean isQr() {
            return type.equals(SecType.QR);
        }

        public boolean isText() {
            return type.equals(SecType.TEXT);
        }

        public boolean isParent() {
            return type.equals(SecType.PARENT);
        }
    }

    public static class Attributes {
        public Element below = null;
        public Element above = null;
        public Element toLeftOf = null;
        public Element toRightOf = null;
        public Element centerVerticalOf = null;
        public Element centerHorizontalOf = null;
        public Element alignBottom = null;
        public Element alignRight = null;
        public Element alignLeft = null;
        public Element alignTop = null;
        public Element textSizeMultiplier = null;
        protected boolean fixedHeight = false;
        protected boolean fixedWidth = false;
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
        public boolean isMeasured = false;
        public TextHAlign textHAlign = TextHAlign.CENTER;
        public TextVAlign textVAlign = TextVAlign.MIDDLE;
        public float textSize = 20;
        public int textColor = Color.BLACK;
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
    }
}
