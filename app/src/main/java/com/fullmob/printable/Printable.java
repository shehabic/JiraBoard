package com.fullmob.printable;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.fullmob.printable.exceptions.PrintableException;
import com.fullmob.printable.generators.AbstractPrintableGenerator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shehabic on 19/03/2017.
 */
public class Printable {

    public static final int PRINTABLE_ORIENTATION_PORTRAIT = 1;
    public static final int PRINTABLE_ORIENTATION_LANDSCAPE = 2;
    @IntDef({PRINTABLE_ORIENTATION_PORTRAIT, PRINTABLE_ORIENTATION_LANDSCAPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PrintableOrientation { }
    public @ColorInt int background = Color.TRANSPARENT;
    public final Element PARENT;
    public final Map<String, Element> elementsById = new HashMap<>();
    public final List<Element> elements;
    public @PrintableOrientation int orientation = PRINTABLE_ORIENTATION_LANDSCAPE;
    public String id;
    public String sizeString;

    public Printable(String id) {
        this.id = id;
        elements = new ArrayList<>();
        PARENT = new Element(Element.TYPE_PARENT, "");
    }

    public Printable() {
        this(null);
    }

    public void setSize(String size) {
        int[] sizes = AbstractPrintableGenerator.findSize(size, orientation == PRINTABLE_ORIENTATION_LANDSCAPE);
        PARENT.width = sizes[0];
        PARENT.height = sizes[1];
    }

    private void setTextOrientation(String orientation) {
        switch (orientation) {
            case "portrait":
                this.orientation = PRINTABLE_ORIENTATION_PORTRAIT;

            default:
                this.orientation = PRINTABLE_ORIENTATION_LANDSCAPE;
        }
    }

    public void addElement(@NonNull Element element) {
        System.out.println(element.toString());
        if (element.id == null) {
            throw new PrintableException("`id` of a printable element cannot be null");
        }
        element.setParent(PARENT);
        if (elementsById.containsKey(element.id)) {
            throw new PrintableException("Duplicate element `id`: " + element.id);
        }
        elementsById.put(element.id, element);
        elements.add(element);
    }

    public Element findElementById(String id) {
        if (id == null) {
            throw new PrintableException("`id` cannot be null");
        }

        return id.toLowerCase().equals(Element.TYPE_PARENT) ? PARENT : elementsById.get(id);
    }

    public void setAttribute(String attribute, Object val) {
        switch (attribute) {
            case "size":
                setSize((String) val);
                sizeString = (String) val;
                break;

            case "id":
                id = (String) val;
                break;

            case "orientation":
                setTextOrientation((String) val);
                break;

            case "background":
                background = (val instanceof String) ? Color.parseColor((String) val) : (int) val;
                break;

            default:
                throw new PrintableException("Unsupported attribute " + attribute);
        }
    }
}
