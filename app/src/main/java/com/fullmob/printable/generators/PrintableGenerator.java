package com.fullmob.printable.generators;

import com.fullmob.printable.Element;
import com.fullmob.printable.Printable;
import com.fullmob.printable.drawers.ElementDrawer;

/**
 * Created by shehabic on 19/03/2017.
 */
public interface PrintableGenerator<T> {
    void prepareParent(Printable printable, int width, int height);

    enum PaperSize {A5, A6, A7, A8, A9, A10}

    void addDrawer(String type, ElementDrawer drawer);

    T createPrintable(Printable printable, PaperSize paperSize);

    T createPrintable(Printable printable, int width, int height);

    void measureElement(Element section);
}
