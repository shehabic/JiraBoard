package com.fullmob.printable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shehabic on 19/03/2017.
 */
public class PrintableGroup {

    public PrintableGroup() {}

    private final List<Printable> printables = new ArrayList<>();

    public void addPrintable(Printable printable) {
        printables.add(printable);
    }

    public List<Printable> getPrintables() {
        return printables;
    }
}
