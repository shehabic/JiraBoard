package com.fullmob.printable;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shehabic on 19/03/2017.
 */
public class Printable {

    public final Element PARENT;

    final Map<String, Element> elementsById = new HashMap<>();
    final List<Element> elements;
    private String id;

    public Printable(String id) {
        this.id = id;
        elements = new ArrayList<>();
        PARENT = new Element(Element.TYPE_PARENT, "");
    }

    public Printable() {
        this(null);
    }

    public void setSize(String size) {
        int[] sizes = AbstractPrintableGenerator.findSize(size, true);
        PARENT.width = sizes[0];
        PARENT.height = sizes[1];
    }

    public void addElement(@NonNull Element element) {
        if (element.id == null) {
            throw new RuntimeException("`id` of a printable element cannot be null");
        }
        element.setParent(PARENT);
        elements.add(element);
        elementsById.put(element.id, element);
    }

    public Element findElementById(String id) {
        if (id == null) {
            throw new RuntimeException("`id` cannot be null");
        }

        return id.toLowerCase().equals(Element.TYPE_PARENT) ? PARENT : elementsById.get(id);
    }

    public void setAttribute(String attribute, Object val) {
        switch (attribute) {
            case "size":
                setSize((String) val);
                break;

            case "id":
                id = (String) val;
                break;
        }
    }
}
