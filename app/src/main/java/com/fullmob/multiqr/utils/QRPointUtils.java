package com.fullmob.multiqr.utils;

import com.fullmob.multiqr.data.Column;

public class QRPointUtils {

    public static int minX(Column col) {
        return min(col.points[0].x, col.points[1].x, col.points[2].x);
    }
    public static int maxX(Column col) {
        return max(col.points[0].x, col.points[1].x, col.points[2].x);
    }

    public static int max(float f1, float f2, float f3) {
        return (int) Math.max(f1, Math.max(f2, f3));
    }

    public static int min(float f1, float f2, float f3) {
        return (int) Math.min(f1, Math.min(f2, f3));
    }
}
