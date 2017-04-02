package com.fullmob.jiraboard.data;

import java.util.ArrayList;
import java.util.List;

public class Column implements Cloneable {
    public int minX = -1;
    public int maxX = -1;
    public int midX = -1;
    public int minY = -1;
    public int maxY = -1;
    public int midY = -1;
    public String text;
    public List<Point> points = new ArrayList<>();
    public List<Ticket> tickets = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        return text.equals(column.text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }

    @Override
    public Column clone() {
        try {
            return (Column) super.clone();
        } catch (Exception e) {
            return null;
        }

    }
}
