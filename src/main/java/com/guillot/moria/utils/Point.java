package com.guillot.moria.utils;


public class Point {

    public int x;

    public int y;

    public Point() {
        this(0, 0);
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this();

        if (point != null) {
            this.x = point.x;
            this.y = point.y;
        }
    }

    public boolean is(int x, int y) {
        return this.x == x && this.y == y;
    }

    public boolean is(Point point) {
        return point != null && is(point.x, point.y);
    }

    public double distanceFrom(Point point) {
        if (point != null) {
            int dy = point.y - y;
            int dx = point.x - x;
            return Math.sqrt(dy * dy + dx * dx);
        }

        return 0;
    }

    public void incrementX() {
        incrementX(1);
    }

    public void incrementX(int value) {
        x += value;
    }

    public void decrementX() {
        decrementX(1);
    }

    public void decrementX(int value) {
        x -= value;
    }

    public void incrementY() {
        incrementY(1);
    }

    public void incrementY(int value) {
        y += value;
    }

    public void decrementY() {
        decrementY(1);
    }

    public void decrementY(int value) {
        y -= value;
    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }
}
