package com.fullmob.jiraboard.utils;

import com.fullmob.jiraboard.data.Column;

public class QRPointUtils {

    public static int minX(Column col) {
        int min = -1;
        for (int i = 0; i < col.points.length; i++) {
            if (min == -1) {
                min = (int) col.points[i].x;
            }
            min = (int) ((col.points[i].x > -1) ? Math.min(min, col.points[i].x) : min);
        }

        return min;
    }

    public static int maxX(Column col) {
        int max = -1;
        for (int i = 0; i < col.points.length; i++) {
            max = (int) Math.max(max, col.points[i].x);
        }

        return max;
    }

    public static int minY(Column col) {
        int min = -1;
        for (int i = 0; i < col.points.length; i++) {
            if (min == -1) {
                min = (int) col.points[i].y;
            }
            min = (int) ((col.points[i].y > -1) ? Math.min(min, col.points[i].y) : min);
        }

        return min;
    }

    public static int maxY(Column col) {
        int max = -1;
        for (int i = 0; i < col.points.length; i++) {
            max = (int) Math.max(max, col.points[i].y);
        }

        return max;
    }

}
