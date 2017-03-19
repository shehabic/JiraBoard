package com.fullmob.printable;

public interface PrintableGenerator<T> {
    void prepareParent(Printable printable, int width, int height);

    enum PaperSize {A5, A6, A7, A8, A9, A10}

    T createPrintable(Printable printable, PaperSize paperSize);

    T createPrintable(Printable printable, int width, int height);

    void prepareSectionDimension(Element section);
}
