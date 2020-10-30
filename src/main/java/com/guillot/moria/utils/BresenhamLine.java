package com.guillot.moria.utils;

import java.util.ArrayList;

public class BresenhamLine {

    public static ArrayList<Point> compute(int x0, int y0, int x1, int y1) {
        ArrayList<Point> result = new ArrayList<>();

        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        if (steep) {
            int temp = x0;
            x0 = y0;
            y0 = temp;

            temp = x1;
            x1 = y1;
            y1 = temp;
        }
        if (x0 > x1) {
            int temp = x0;
            x0 = x1;
            x1 = temp;

            temp = y0;
            y0 = y1;
            y1 = temp;
        }

        int deltax = x1 - x0;
        int deltay = Math.abs(y1 - y0);
        int error = 0;
        int ystep;
        int y = y0;
        if (y0 < y1)
            ystep = 1;
        else
            ystep = -1;
        for (int x = x0; x <= x1; x++) {
            if (steep)
                result.add(new Point(y, x));
            else
                result.add(new Point(x, y));
            error += deltay;
            if (2 * error >= deltax) {
                y += ystep;
                error -= deltax;
            }
        }

        return result;
    }
}
