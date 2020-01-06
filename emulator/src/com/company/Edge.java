package com.company;

import java.util.ArrayList;
import java.util.List;

/*
两个点连的边
 */
public class Edge {
    int x1;
    int x2;
    int y1;
    int y2;
    double length;

    public Edge(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.length = Test.distance(x1, y1, x2, y2);
    }
    @Override
    public boolean equals(Object obj) {
        Edge obj1 = (Edge) obj;

        return (x1==obj1.x1&&x2==obj1.x2&&y1==obj1.y1&&y2==obj1.y2)||
                (x1==obj1.x2&&x2==obj1.x1&&y1==obj1.y2&&y2==obj1.y1);
    }

    @Override
    public int hashCode() {
        return this.x2+this.x1+this.y1+this.y2;
    }

    /*
    edges中edge的最长的邻边，不存在则返回null
     */
    public static Edge longestNeighbor(List<Edge> edges,Edge edge) {

        int max = -1;
        double maxlen = 0;
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).x1 == edge.x1 && edges.get(i).y1 == edge.y1
                    || edges.get(i).x1 == edge.x2 && edges.get(i).y1 == edge.y2
                    || edges.get(i).x2 == edge.x1 && edges.get(i).y2 == edge.y1
                    || edges.get(i).x2 == edge.x2 && edges.get(i).y2 == edge.y2) {
                if (edges.get(i) != edge && edges.get(i).length > maxlen) {
                    max = i;
                    maxlen = edges.get(i).length;
                }
            }
        }
        if (max == -1) {
            return null;
        } else {
            return edges.get(max);
        }
    }
/*
倾斜的返回true
 */
    public static boolean slant(Edge edge) {
        return !((edge.x1 - edge.x2) == 0 || (edge.y1 - edge.y2) == 0);
    }

/*
线性化单边
 */
    public static List<Edge> linearization(Edge edge) {


        List<Edge> edges = new ArrayList<>();
        if (slant(edge)) {
            edges.add(new Edge(edge.x1, edge.y1, edge.x2, edge.y1));
            edges.add(new Edge(edge.x2, edge.y2, edge.x2, edge.y1));
        } else {
            edges.add(edge);
        }
        return edges;
    }

    /*
    双边线性化,如果edge0和edge无x，y坐标的映射重叠，则只线性化edge0
     */
    public static List<Edge> linearization(Edge edge0,Edge edge) {
        List<Edge> edges = new ArrayList<>();

        if (edge0.x1 == edge.x1 && edge0.y1 == edge.y1) {//四种情况

            help(edge0, edge, edges);
        } else if(edge0.x1 == edge.x2 && edge0.y1 == edge.y2){

            help(edge0, new Edge(edge.x2, edge.y2, edge.x1, edge.y1), edges);
        } else if (edge0.x2 == edge.x1 && edge0.y2 == edge.y1) {

            help(new Edge(edge0.x2, edge0.y2, edge0.x1, edge0.y1), edge, edges);
        } else {

            help(new Edge(edge0.x2, edge0.y2, edge0.x1, edge0.y1), new Edge(edge.x2, edge.y2, edge.x1, edge.y1), edges);
        }
        return edges;
    }

/*
两边的一号点是同一点时，将生成的线性化边装入edges
edge可能为直边
 */
    public static void help(Edge edge0, Edge edge, List<Edge> edges) {


        if (!slant(edge)) {

            if (edge.x1 == edge.x2) {

                if ((edge0.y2 - edge0.y1) * (edge.y2 - edge0.y1) > 0) {
                    edges.add(new Edge(edge0.x2, edge0.y2, edge0.x1, edge0.y2));
                    edges.add(new Edge(edge0.x1, edge0.y2, edge0.x1, edge0.y1));
                    edges.add(new Edge(edge0.x1, edge.y2, edge0.x1, edge0.y1));
                } else {
                    edges.addAll(linearization(edge0));
                }
            } else {

                if ((edge0.x2 - edge0.x1) * (edge.x2 - edge0.x1) > 0) {
                    edges.add(new Edge(edge0.x2, edge0.y2, edge0.x2, edge0.y1));
                    edges.add(new Edge(edge0.x1, edge0.y1, edge0.x2, edge0.y1));
                    edges.add(new Edge(edge0.x1, edge.y2, edge0.x2, edge0.y1));
                } else {
                    edges.addAll(linearization(edge0));
                }
            }

            return;
        }

        if ((edge0.x2 - edge0.x1) * (edge.x2 - edge.x1) > 0
                && (edge0.y2 - edge0.y1) * (edge.y2 - edge.y1) > 0) {//x,y坐标都有重叠

            int x0;
            int y0;
            if (edge.x2 < edge.x1 && edge.y2 < edge.y1) {

                 x0 = Integer.max(edge.x2, edge0.x2);//四种情况
                 y0 = Integer.max(edge.y2, edge0.y2);
            } else if (edge.x2 > edge.x1 && edge.y2 < edge.y1) {

                x0 = Integer.min(edge.x2, edge0.x2);//四种情况
                y0 = Integer.max(edge.y2, edge0.y2);
            } else if (edge.x2 > edge.x1 && edge.y2 > edge.y1) {

                x0 = Integer.min(edge.x2, edge0.x2);//四种情况
                y0 = Integer.min(edge.y2, edge0.y2);
            } else {

                x0 = Integer.max(edge.x2, edge0.x2);//四种情况
                y0 = Integer.min(edge.y2, edge0.y2);
            }
            edges.add(new Edge(x0, y0, edge.x2, edge.y2));
            edges.add(new Edge(x0, y0, edge0.x2, edge0.y2));
            edges.addAll(Edge.linearization(new Edge(x0, y0, edge.x1, edge.y1)));
        } else if ((edge0.x2 - edge0.x1) * (edge.x2 - edge.x1) > 0
                && (edge0.y2 - edge0.y1) * (edge.y2 - edge.y1) < 0) {//x坐标有映射重叠

            int x0;
            int y0;
            if (edge.x2 > edge.x1) {
                x0 = Integer.min(edge.x2, edge0.x2);//两种情况
                y0 = edge.y1;
            } else {
                x0 = Integer.max(edge.x2, edge0.x2);//两种情况
                y0 = edge.y1;
            }
            edges.add(new Edge(x0, y0, edge.x1, edge.y1));
            edges.addAll(Edge.linearization(new Edge(x0, y0, edge.x2, edge.y2)));
            edges.addAll(Edge.linearization(new Edge(x0, y0, edge0.x2, edge0.y2)));
        } else if ((edge0.x2 - edge0.x1) * (edge.x2 - edge.x1) < 0
                && (edge0.y2 - edge0.y1) * (edge.y2 - edge.y1) > 0) {

            int y0;
            int x0;
            if (edge.y2 > edge.y1) {

                y0 = Integer.min(edge.y2, edge0.y2);

                x0 = edge.x1;
            } else {
                y0 = Integer.max(edge.y2, edge0.y2);

                x0 = edge.x1;
            }
            edges.add(new Edge(x0, y0, edge.x1, edge.y1));
            edges.addAll(Edge.linearization(new Edge(x0, y0, edge.x2, edge.y2)));
            edges.addAll(Edge.linearization(new Edge(x0, y0, edge0.x2, edge0.y2)));
        } else {

            edges.addAll(Edge.linearization(edge0));
        }
    }

}
