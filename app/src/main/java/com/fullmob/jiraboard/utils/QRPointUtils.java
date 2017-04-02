package com.fullmob.jiraboard.utils;

import com.fullmob.jiraboard.data.Column;
import com.fullmob.jiraboard.data.Point;

public class QRPointUtils {

    public static int minX(Column col) {
        int min = -1;
        for (Point p : col.points) {
            if (min == -1) {
                min = (int) p.x;
            }
            min = (int) Math.min(min, p.x);
        }

        return min;
    }

    public static int maxX(Column col) {
        int max = -1;
        for (Point p : col.points) {
            max = (int) Math.max(max, p.x);
        }

        return max;
    }

    public static int minY(Column col) {
        int min = -1;
        for (Point p : col.points) {
            if (min == -1) {
                min = (int) p.y;
            }
            min = (int) Math.min(min, p.y);
        }

        return min;
    }

    public static int maxY(Column col) {
        int max = -1;
        for (Point p : col.points) {
            max = (int) Math.max(max, p.y);
        }

        return max;
    }

}
