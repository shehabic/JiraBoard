package com.fullmob.printable;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.support.annotation.NonNull;
import android.support.annotation.XmlRes;

/**
 * Created by shehabic on 19/03/2017.
 */
public class PrintableInflator {

    private Context context;
    private static final String FLOAT_MATCHER = "^(\\d*\\.?\\d+f?)$";

    public static PrintableInflator from(Context context) {
        return new PrintableInflator(context);
    }

    private PrintableInflator(Context context) {
        this.context = context.getApplicationContext();
    }

    public PrintableGroup inflate(@XmlRes int printableXml) {
        XmlResourceParser xrp = context.getApplicationContext().getResources().getXml(printableXml);
        PrintableGroup printableGroup = null;
        try {
            xrp.next();
            if (xrp.getEventType() == XmlResourceParser.START_DOCUMENT) {
                xrp.next();
                if (xrp.getName().equals("com.fullmob.printable.PrintableGroup")) {
                    printableGroup = new PrintableGroup();
                } else {
                    throw new PrintableException("Invalid document");
                }
            } else {
                throw new PrintableException("Invalid document");
            }
            inflatePrintables(printableGroup, xrp);
        } catch (Exception e) {
            throw new PrintableException(e);
        } finally {
            xrp.close();
        }

        return printableGroup;
    }

    private void inflatePrintables(@NonNull PrintableGroup printableGroup, @NonNull XmlResourceParser xrp)
        throws Exception {

        Printable printable = null;
        while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
            xrp.next();
            String nodeName = xrp.getName();
            if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                if (isPrintable(nodeName)) {
                    printable = new Printable();
                    initPrintable(printable, xrp);
                } else if (isElement(nodeName)) {
                    inflateElement(xrp, printable);
                }
            }
            if (xrp.getEventType() == XmlResourceParser.END_TAG) {
                if (isPrintable(nodeName)) {
                    printableGroup.addPrintable(printable);
                    printable = null;
                }
            }
        }
    }

    private void inflateElement(XmlResourceParser xrp, Printable printable) {
        String id = xrp.getAttributeValue(null, "id");
        @Element.ElementType String type = xrp.getAttributeValue(null, "type");
        String content = xrp.getAttributeValue(null, "content");
        Element element = new Element(type, content);
        element.id = id;
        printable.addElement(element);
        processElementAttributes(element, xrp, printable);
    }

    private void processElementAttributes(Element element, XmlResourceParser xrp, Printable printable) {
        for (int i = 0; i < xrp.getAttributeCount(); i++) {
            String attrib = xrp.getAttributeName(i);
            Object val = xrp.getAttributeValue(i);
            element.setAttribute(attrib, getAttributeValue((String) val, printable, element));
        }
    }

    private Object getAttributeValue(String val, Printable printable, Element currentElement) {
        String resValue = val.contains("/") ? val.split("/")[1] : val;
        if (val.startsWith("@+id/")) {
            return resValue.toLowerCase().equals("self") ? currentElement : printable.findElementById(resValue);
        } else if (val.startsWith("@dimen/")) {
            return context.getResources().getDimension(getResId(val));
        } else if (val.startsWith("@color/")) {
            return context.getResources().getColor(getResId(val));
        } else if (val.startsWith("@drawable/")) {
            return context.getResources().getDrawable(getResId(val));
        } else if (val.startsWith("@string/")) {
            return context.getString(getResId(val));
        } else if (val.matches(FLOAT_MATCHER)) {
            return Float.valueOf(val);
        }

        return val;
    }

    private int getResId(String resource) {
        return context.getResources().getIdentifier(resource.substring(1), null, context.getPackageName());
    }

    private boolean isElement(String nodeName) {
        return nodeName.equals("com.fullmob.printable.Element");
    }

    private boolean isPrintable(String nodeName) {
        return nodeName.equals("com.fullmob.printable.Printable");
    }

    private void initPrintable(Printable printable, XmlResourceParser xrp) {
        for (int i = 0; i < xrp.getAttributeCount(); i++) {
            String attrib = xrp.getAttributeName(i);
            Object val = xrp.getAttributeValue(i);
            printable.setAttribute(attrib, val);
        }
    }
}
