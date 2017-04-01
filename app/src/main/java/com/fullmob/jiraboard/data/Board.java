package com.fullmob.jiraboard.data;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Board {
    private boolean isBoxed = false;
    private final String prefix;
    private Bitmap bitmap;
    private List<Column> columns;
    private HashSet<Column> boxedColumns;
    public int minX = -1;
    public int minY = -1;
    public int maxX = -1;
    public int maxY = -1;

    public Board(String prefix) {
        this.prefix = prefix;
        columns = new ArrayList<>();
        boxedColumns = new HashSet<>();
    }

    public String getPrefix() {
        return prefix;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Collection<Column> getColumns() {
        return isBoxed() ? boxedColumns : columns;
    }

    public boolean isBoxed() {
        return isBoxed;
    }

    public void setBoxed(boolean boxed) {
        isBoxed = boxed;
    }

    public void addColumn(Ticket column) {
        if (isBoxed()) {
            boxedColumns.add(column);
        } else {
            columns.add(column);
        }
    }

    public List<Column> getStackedColumns() {
        return columns;
    }
}
