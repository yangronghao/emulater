package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rectangle {
    int x1,x2,y1,y2;//x1<x2 ,y1<y2

    public Rectangle(int x1, int x2, int y1, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }
/*
x,y坐标点是障碍物的左下点，返回true
 */
    public static boolean leftlow(List<Rectangle> rectangles, int x, int y) {

        Map<Integer, List<Integer>> map = new HashMap<>();//存放矩形左下角点的x->y
        for (int i = 0; i < rectangles.size(); i++) {
            int x1 = rectangles.get(i).x1;
            int y1 = rectangles.get(i).y1;
            if (map.containsKey(x1)) {
                map.get(x1).add(y1);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(y1);
                map.put(x1, list);
            }
        }

        if (!(map.containsKey(x)
                && map.get(x).contains(y))) {
            return false;
        }
        return true;
    }


    public static boolean rightlow(List<Rectangle> rectangles, int x, int y) {

        Map<Integer, List<Integer>> map = new HashMap<>();//存放矩形左下角点的x->y
        for (int i = 0; i < rectangles.size(); i++) {
            int x1 = rectangles.get(i).x2;
            int y1 = rectangles.get(i).y1;
            if (map.containsKey(x1)) {
                map.get(x1).add(y1);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(y1);
                map.put(x1, list);
            }
        }

        if (!(map.containsKey(x)
                && map.get(x).contains(y))) {
            return false;
        }
        return true;
    }

    public static boolean rightup(List<Rectangle> rectangles, int x, int y) {

        Map<Integer, List<Integer>> map = new HashMap<>();//存放矩形左下角点的x->y
        for (int i = 0; i < rectangles.size(); i++) {
            int x1 = rectangles.get(i).x2;
            int y1 = rectangles.get(i).y2;
            if (map.containsKey(x1)) {
                map.get(x1).add(y1);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(y1);
                map.put(x1, list);
            }
        }

        if (!(map.containsKey(x)
                && map.get(x).contains(y))) {
            return false;
        }
        return true;
    }

    public static boolean leftup(List<Rectangle> rectangles, int x, int y) {

        Map<Integer, List<Integer>> map = new HashMap<>();//存放矩形左下角点的x->y
        for (int i = 0; i < rectangles.size(); i++) {
            int x1 = rectangles.get(i).x1;
            int y1 = rectangles.get(i).y2;
            if (map.containsKey(x1)) {
                map.get(x1).add(y1);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(y1);
                map.put(x1, list);
            }
        }

        if (!(map.containsKey(x)
                && map.get(x).contains(y))) {
            return false;
        }
        return true;
    }
}
