package com.fullmob.jiraboard.data;

import java.util.ArrayList;
import java.util.List;

public class Column {
    public int minX = 0;
    public int maxX = 0;
    public int centerX = 0;
    public String text;
    public Point[] points;
    public List<Column> tickets = new ArrayList<>();
}
