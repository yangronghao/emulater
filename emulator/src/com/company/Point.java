package com.company;

public class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public boolean equals(Object obj) {
        Point obj1 = (Point) obj;

        return obj1.x==this.x&&obj1.y==this.y;
    }

    @Override
    public int hashCode() {
        return this.x + this.y;
    }

}
