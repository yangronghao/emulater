package com.company;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Neighbor {




//have bug
    public static List<VertexType> filter2(List<Rectangle> rectangles, List<VertexType> E) {
        boolean flag = false;
        int max = -1;
        for (int i = 0; i < E.size(); i++) {
            int x = E.get(i).x;
            int y = E.get(i).y;

            if (flag) {
                E.remove(i);
                i--;
            }
            if (Rectangle.leftlow(rectangles, x, y)) {
                flag = true;
            }
            if (Rectangle.rightup(rectangles, x, y)) {
                max = i;
            }

        }
        if (max > 0) {
            E = E.subList(max,E.size());
        }
        return E;
    }

    public static List<VertexType> filter3(List<Rectangle> rectangles, List<VertexType> E) {
        boolean flag = false;
        int max = -1;
        for (int i = 0; i < E.size(); i++) {
            int x = E.get(i).x;
            int y = E.get(i).y;

            if (flag) {
                E.remove(i);
                i--;
            }
            if (Rectangle.rightlow(rectangles, x, y)) {
                flag = true;
            }
            if (Rectangle.leftup(rectangles, x, y)) {
                max = i;
            }
        }
        if (max > 0) {
            E = E.subList(max,E.size());
        }
        return E;
    }

    public static List<VertexType> filter4(List<Rectangle> rectangles, List<VertexType> E) {
        boolean flag = false;
        int max = -1;
        for (int i = 0; i < E.size(); i++) {
            int x = E.get(i).x;
            int y = E.get(i).y;

            if (flag) {
                E.remove(i);
                i--;
            }
            if (Rectangle.rightup(rectangles, x, y)) {
                flag = true;
            }

            if (Rectangle.leftlow(rectangles, x, y)) {
                max = i;
            }
        }
        if (max > 0) {
            E = E.subList(max,E.size());
        }
        return E;
    }

    public static List<VertexType> filter1(List<Rectangle> rectangles, List<VertexType> E) {
        boolean flag = false;
        int max = -1;
        for (int i = 0; i < E.size(); i++) {
            int x = E.get(i).x;
            int y = E.get(i).y;

            if (flag) {
                E.remove(i);
                i--;
            }
            if (Rectangle.leftup(rectangles, x, y)) {
                flag = true;
            }

            if (Rectangle.rightlow(rectangles, x, y)) {
                max = i;
            }
        }
        if (max > 0) {
            E = E.subList(max,E.size());
        }
        return E;
    }
    /*
E中的点都在v的r2区域，返回其中的neighbors
A vertexf ∈ P ∪C is a neighbor of a vertexv ∈ P ∪C
if no other vertex in P ∪C or obstacle is inside or on the
boundary of the bounding box of v and f.

 */
    public static void neighborOfV2(VertexType v, List<VertexType> E, List<VertexType> A) {
        Collections.sort(A, (o1, o2) -> {
            if (o1.y != o2.y) {
                return ((Integer) o1.y).compareTo(o2.y);
            } else {
                return ((Integer) o2.x).compareTo(o1.x);
            }

        });
        int min=-1;
        for (int i = 0; i < A.size(); i++) {
            if (i > 0 && A.get(i).y == A.get(i - 1).y) {
                continue;
            } else {
                if (min < A.get(i).x) {
                    min = A.get(i).x;
                    E.add(A.get(i));
                }
            }
        }
    }




    public static void neighborOfV3(VertexType v, List<VertexType> E, List<VertexType> A) {
        Collections.sort(A, (o1, o2) -> {
            if (o1.y != o2.y) {
                return ((Integer) o1.y).compareTo(o2.y);
            } else {
                return ((Integer) o1.x).compareTo(o2.x);
            }

        });
        int min=Integer.MAX_VALUE;
        for (int i = 0; i < A.size(); i++) {
            if (i > 0 && A.get(i).y == A.get(i - 1).y) {
                continue;
            } else {
                if (min > A.get(i).x) {
                    min = A.get(i).x;
                    E.add(A.get(i));
                }
            }
        }
    }



    public static void neighborOfV4(VertexType v, List<VertexType> E, List<VertexType> A) {
        Collections.sort(A, (o1, o2) -> {
            if (o1.y != o2.y) {
                return ((Integer) o2.y).compareTo(o1.y);
            } else {
                return ((Integer) o1.x).compareTo(o2.x);
            }

        });
        int min=Integer.MAX_VALUE;
        for (int i = 0; i < A.size(); i++) {
            if (i > 0 && A.get(i).y == A.get(i - 1).y) {
                continue;
            } else {
                if (min > A.get(i).x) {
                    min = A.get(i).x;
                    E.add(A.get(i));
                }
            }
        }
    }



    public static void neighborOfV1(VertexType v, List<VertexType> E, List<VertexType> A) {
        Collections.sort(A, (o1, o2) -> {
            if (o1.y != o2.y) {
                return ((Integer) o2.y).compareTo(o1.y);
            } else {
                return ((Integer) o2.x).compareTo(o1.x);
            }

        });
        int min=-1;
        for (int i = 0; i < A.size(); i++) {
            if (i > 0 && A.get(i).y == A.get(i - 1).y) {
                continue;
            } else {
                if (min < A.get(i).x) {
                    min = A.get(i).x;
                    E.add(A.get(i));
                }
            }
        }
    }
}
