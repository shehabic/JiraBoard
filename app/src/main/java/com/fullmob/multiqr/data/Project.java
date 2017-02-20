package com.fullmob.multiqr.data;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private final String prefix;
    private Bitmap bitmap;
    private List<Column> columns;

    public Project(String prefix) {
        this.prefix = prefix;
        columns = new ArrayList<>();
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

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
