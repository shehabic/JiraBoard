package com.fullmob.jiraboard.printing;

public interface PrintableTicketGenerator<T> {
    void prepareParent(Printable printable, int width, int height);

    enum PaperSize {A6, A7, A8, A9, A10}

    T createPrintable(Printable printable, PaperSize paperSize);

    T createPrintable(Printable printable, int width, int height);

    void prepareSectionDimension(Printable.Element section);
}
