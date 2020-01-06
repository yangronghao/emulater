package com.company;

import com.sun.corba.se.impl.orbutil.graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Test {


    public static void main(String[] args) {

        List<Rectangle> rectangles = new ArrayList<>();
        rectangles.add(new Rectangle(4,6,0,12));
        rectangles.add(new Rectangle(10,20,2,8));
        rectangles.add(new Rectangle(0,8,14,16));
        rectangles.add(new Rectangle(10,12,10,18));
        rectangles.add(new Rectangle(15,18,13,20));
        draw(new DrawRectanglePanel(rectangles));

        ALGraph alGraph = new ALGraph(new ArrayList<>(),0,0,0);
        int[][] vertextTypes = {{4,0},{4,12},{6,12},{6,0},{10,2},{10,8},{20,8},{20,2},
                {0,14},{0,16},{8,16},{8,14},{10,10},{10,18},{12,18},{12,10},
                {15,13},{15,20},{18,20},{18,13},
                {2,9},{15,1},{7,22},{14,19}
                ,{7,9},{11,19},{15,9}
                };
        int[][] arcNodes = {{0,1,2},{1,2,2},{2,3,2},{3,0,2},
                {4,5,2},{5,6,2},{7,6,2},{7,4,2},
                {8,9,2},{9,10,2},{10,11,2},{11,8,2},
                {12,13,2},{13,14,2},{14,15,2},{15,12,2},
                {16,17,2},{18,17,2},{18,19,2},{19,16,2}};
        createUDN(alGraph,vertextTypes,arcNodes);


        draw(new DrawALGraph(alGraph));


/*
//span-tree-mapping
        List<VNode> vs = alGraph.vertices;
        Collections.sort(vs, new Comparator<VNode>() {
            public int compare(VNode o1, VNode o2) {
                if (o1.data.x != o2.data.x) {
                    return ((Integer) o1.data.x).compareTo(o2.data.x);
                } else {
                    return ((Integer) o1.data.y).compareTo(o2.data.y);
                }

            }
        });
        edgeConnection(rectangles,vs,alGraph);
        //draw(new DrawALGraph(alGraph));
//
*/



        List<VertexType> P = new ArrayList<>();
        P.add(new VertexType(2,9,20));
        P.add(new VertexType(15,1,21));
        P.add(new VertexType(7,22,22));
        P.add(new VertexType(14,19,23));

        P.add(new VertexType(7,9,24));
        P.add(new VertexType(11,19,25));
        P.add(new VertexType(15,9,26));
        for (int i = 0; i < alGraph.vertices.size(); i++) {
            Collections.sort(alGraph.vertices, new Comparator<VNode>() {
                public int compare(VNode o1, VNode o2) {
                    if (o1.data.x != o2.data.x) {
                        return ((Integer) o1.data.x).compareTo(o2.data.x);
                    } else {
                        return ((Integer) o1.data.y).compareTo(o2.data.y);
                    }

                }
            });
            int x = alGraph.vertices.get(i).data.x;
            int y = alGraph.vertices.get(i).data.y;
            VertexType v = new VertexType(x, y, 0);
            List<VertexType> E2 = oasg_r2(alGraph, rectangles, P, v);
            List<VertexType> E3 = oasg_r3(alGraph, rectangles, P, v);
            List<VertexType> E4 = oasg_r4(alGraph, rectangles, P, v);
            List<VertexType> E1 = oasg_r1(alGraph, rectangles, P, v);

           for (VertexType e:E2
                 ) {
                alGraph.connectPoint(v, e,0);
            }
            for (VertexType e:E3
            ) {
                alGraph.connectPoint(v, e,0);
            }
            for (VertexType e:E4
            ) {
                alGraph.connectPoint(v, e,0);
            }
            for (VertexType e:E1
            ) {
                alGraph.connectPoint(v, e,0);
            }


        }


        ALGraph alGraphcopy = alGraph.copy();

        draw(new DrawALGraph(alGraphcopy));

        computeWeight(alGraph);

        ALGraph alGraphMap = new ALGraph();
        ALGraph.initGraph(P,alGraphMap);
        Map<TwoId, double[][]> map = new HashMap<>();
        for (int i = 0; i < P.size(); i++) {
            for (int j = i + 1; j < P.size(); j++) {
                double[][] a = alGraph.dijkstra(P.get(i).id);
                int j1 = alGraph.locatevex(P.get(j).id);
                map.put(new TwoId(P.get(i).id, P.get(j).id), a);

                double weight = a[0][j1];
                alGraphMap.connectPoint(P.get(i), P.get(j), weight);
            }
        }

        double[][] am = ALGraph.createprimtree(alGraphMap);

        alGraph.deleteArc();

        for (int i = 0; i < am[1].length; i++) {
            if (am[1][i] != -1) {

                int id = P.get(i).id;
                int id1 = P.get((int)am[1][i]).id;
                double path[][] = map.get(new TwoId(id, id1));

                ALGraph.connectByPath(alGraph, path, id, id1);

            }
        }

        alGraph.deleteVex();

//refinement
        for (int i = 0; i < alGraph.vertices.size(); i++) {
            for (int j = i + 1; j < alGraph.vertices.size(); j++) {
                if (alGraphcopy.existArc(alGraph.vertices.get(i).data, alGraph.vertices.get(j).data)) {
                    alGraph.connectPoint(alGraph.vertices.get(i).data,alGraph.vertices.get(j).data,
                            manhattan(alGraph.vertices.get(i).data.x,alGraph.vertices.get(i).data.y
                            ,alGraph.vertices.get(j).data.x,alGraph.vertices.get(j).data.y));
                }
            }
        }


        ALGraph.prim(alGraph);




        Comparator<Edge> cmp = (a, b) -> { //这里是大根堆

            if(b.length > a.length)
            {
                return 1;
            }
            else if(b.length < a.length)
            {
                return -1;
            }
            else
            {
                return 0;
            }
        };
        Queue<Edge> pq = new PriorityQueue<>(cmp);
        List<Edge> set = new ArrayList<>();
        List<Edge> E = new ArrayList<>();

        for (int i = 0; i < alGraph.vertices.size(); i++) {

            ArcNode arcNode = alGraph.vertices.get(i).firstarc;
            int x1 = alGraph.vertices.get(i).data.x;
            int y1 = alGraph.vertices.get(i).data.y;

            while (arcNode != null) {

                int j = alGraph.locatevex(arcNode.adjvex);
                int x2 = alGraph.vertices.get(j).data.x;
                int y2 = alGraph.vertices.get(j).data.y;

                if (!set.contains(new Edge(x1, y1, x2, y2))) {

                    set.add(new Edge(x1, y1, x2, y2));
                }

                arcNode = arcNode.nextarc;

            }
        }

        for (Edge e:set
             ) {
            pq.add(e);
        }

        while (set.size() != 0) {

            Edge edge = pq.poll();
            if (set.contains(edge)) {

                if (!Edge.slant(edge)) {
                    E.addAll(Edge.linearization(edge));
                    set.remove(edge);
                } else {
                    Edge edge1 = Edge.longestNeighbor(set, edge);

                    if (edge1 == null) {
                        E.addAll(Edge.linearization(edge));
                    } else {
                        E.addAll(Edge.linearization(edge, edge1));
                    }

                    set.remove(edge);

//如果edge0和edge无x，y坐标的映射重叠，则只线性化edge0
                    if (edge1 != null) {

                        if (edge1.x1 == edge.x1 && edge1.y1 == edge.y1) {//四种情况


                        } else if (edge1.x1 == edge.x2 && edge1.y1 == edge.y2) {

                            edge = new Edge(edge.x2, edge.y2, edge.x1, edge.y1);
                        } else if (edge1.x2 == edge.x1 && edge1.y2 == edge.y1) {

                            edge1 = new Edge(edge1.x2, edge1.y2, edge1.x1, edge1.y1);
                        } else {

                            edge = new Edge(edge.x2, edge.y2, edge.x1, edge.y1);
                            edge1 = new Edge(edge1.x2, edge1.y2, edge1.x1, edge1.y1);
                        }

                        if (!((edge1.x2 - edge1.x1) * (edge.x2 - edge.x1) <= 0
                                && (edge1.y2 - edge1.y1) * (edge.y2 - edge.y1) <= 0)) {

                            set.remove(edge1);
                        }
                    }

                }
            }

        }

        ALGraph alGraph1 = ALGraph.createByEdges(E);
        draw(new DrawALGraph(alGraph1));


        System.out.println("HH");

/*
        for (int i = 0; i < alGraph.vertices.size(); i++) {
            int x = alGraph.vertices.get(i).data.x;
            int y = alGraph.vertices.get(i).data.y;

            List<VertexType> A2 = new ArrayList<>();//存顶点的r2区域的点
            List<VertexType> A3 = new ArrayList<>();
            List<VertexType> A4 = new ArrayList<>();
            List<VertexType> A1 = new ArrayList<>();

            for (int j = 0; j < alGraph.vertices.size(); j++) {
                int x1 = alGraph.vertices.get(j).data.x;
                int y1 = alGraph.vertices.get(j).data.y;
                if (!(x == x1 && y == y1)) {

                    if (locatedInR2(x1, y1, new VertexType(x, y, 0))) {
                        A2.add(new VertexType(x1, y1, 0));
                    }

                    if (locatedInR3(x1, y1, new VertexType(x, y, 0))) {
                        A3.add(new VertexType(x1, y1, 0));
                    }

                    if (locatedInR4(x1, y1, new VertexType(x, y, 0))) {
                        A4.add(new VertexType(x1, y1, 0));
                    }

                    if (locatedInR1(x1, y1, new VertexType(x, y, 0))) {
                        A1.add(new VertexType(x1, y1, 0));
                    }

                }
            }

            List<VertexType> E2 = new ArrayList<>();
            List<VertexType> E3 = new ArrayList<>();
            List<VertexType> E4 = new ArrayList<>();
            List<VertexType> E1 = new ArrayList<>();




            Neighbor.neighborOfV1(new VertexType(x,y,0),E1,A1);
            Neighbor.neighborOfV2(new VertexType(x,y,0),E2,A2);
            Neighbor.neighborOfV3(new VertexType(x,y,0),E3,A3);
            Neighbor.neighborOfV4(new VertexType(x,y,0),E4,A4);

            E1=Neighbor.filter1(rectangles, E1);
            E2=Neighbor.filter2(rectangles, E2);
            E3=Neighbor.filter3(rectangles, E3);
            E4=Neighbor.filter4(rectangles, E4);



            for (VertexType v:E1
                 ) {
                alGraph.connectPoint(v, new VertexType(x, y, 0), 0);
            }

            for (VertexType v:E2
            ) {
                alGraph.connectPoint(v, new VertexType(x, y, 0), 0);
            }
            for (VertexType v:E3
            ) {
                alGraph.connectPoint(v, new VertexType(x, y, 0), 0);
            }
            for (VertexType v:E4
            ) {
                alGraph.connectPoint(v, new VertexType(x, y, 0), 0);
            }
        }
*/

    }


    public static void computeWeight(ALGraph alGraph) {
        for (int i = 0; i < alGraph.vertices.size(); i++) {
            int x = alGraph.vertices.get(i).data.x;
            int y = alGraph.vertices.get(i).data.y;
            ArcNode arcNode = alGraph.vertices.get(i).firstarc;
            while (arcNode != null) {

                int adj = arcNode.adjvex;
                int j = alGraph.locatevex(adj);
                int x1 = alGraph.vertices.get(j).data.x;
                int y1= alGraph.vertices.get(j).data.y;

                double weight = manhattan(x, y, x1, y1);
                arcNode.weight = weight;

                arcNode = arcNode.nextarc;
            }
        }
    }

/*
span graph tcad
 */
    public static List<VertexType> oasg_r2(ALGraph alGraph, List<Rectangle> o, List<VertexType> P, VertexType v) {

        List<VertexType> E = new ArrayList<>();
        List<VertexType> A = new ArrayList<>();
        List<VertexType> I = new ArrayList<>();//x表示y_min,y表示y_max

        Collections.sort(alGraph.vertices, new Comparator<VNode>() {
            public int compare(VNode o1, VNode o2) {
                if (o1.data.x != o2.data.x) {
                    return ((Integer) o1.data.x).compareTo(o2.data.x);
                } else {
                    return ((Integer) o1.data.y).compareTo(o2.data.y);
                }

            }
        });
        for (int i = 0; i < alGraph.vertices.size(); i++) {
            int x = alGraph.vertices.get(i).data.x;
            int y = alGraph.vertices.get(i).data.y;

            if (x > v.x) {

                break;
            }
            if (belongToP(x, y, P)) {
                if (locatedInR2(x, y, v) && notBlocked(v.y, y, I)) {
                    A.add(new VertexType(x, y, 0));//id暂不需要
                    if (x == v.x && y == v.y) {
                        A.remove(A.size() - 1);
                    }
                }
            }

            if (Rectangle.rightlow(o, x, y)) {
                if (locatedInR2(x, y, v) && notBlocked(v.y, y, I)) {
                    A.add(new VertexType(x, y, 0));//id暂不需要
                    if (x == v.x && y == v.y) {
                        A.remove(A.size() - 1);
                    }
                }

                //i+1个是rightup点
                if (i < alGraph.vertices.size() - 1){
                    int y1 = alGraph.vertices.get(i + 1).data.y;
                    if (x > v.x) {

                        break;
                    }

                    for (int j = 0; j < I.size(); j++) {
                        if (I.get(j).x == y && I.get(j).y == y1) {
                            I.remove(j);
                            break;
                        }
                    }
                    if (!(locatedInR2(x, y, v) && notBlocked(v.y, y, I))
                            &&locatedInR2(x, y1, v) && notBlocked(v.y, y1, I)) {
                        A.add(new VertexType(x, y1, 0));//id暂不需要
                        if (x == v.x && y1 == v.y) {
                            A.remove(A.size() - 1);
                        }
                    }


                    i++;
                }


            }

            if (Rectangle.leftlow(o, x, y)) {
                if (locatedInR2(x, y, v) && notBlocked(v.y, y, I)) {
                    A.add(new VertexType(x, y, 0));//id暂不需要
                    if (x == v.x && y == v.y) {
                        A.remove(A.size() - 1);
                    }
                }
                //i+1个是leftup点
                if (i < alGraph.vertices.size() - 1) {
                    int y1 = alGraph.vertices.get(i + 1).data.y;
                    if (x > v.x) {
                        break;
                    }

                    if (x < v.x) {

                        I.add(new VertexType(y, y1, 0));//id暂不需要
                    }
                        for (int j = 0; j < A.size(); j++) {
                            if (notBlocked(v.y, A.get(j).y, I)) {

                            } else {
                                A.remove(j);
                                j--;
                            }
                        }
                        if (y1 == v.y) {

                            A.add(new VertexType(x, y1, 0));
                            if (x == v.x && y1 == v.y) {
                                A.remove(A.size() - 1);
                            }
                        }
                        if (x == v.x && y1 > v.y && notBlocked(v.y, y1, I)) {

                            A.add(new VertexType(x, y1, 0));
                        }
                        i++;

                    }


            }
        }

        Neighbor.neighborOfV2(v, E, A);
        return E;

    }


    public static List<VertexType> oasg_r1(ALGraph alGraph, List<Rectangle> o, List<VertexType> P, VertexType v) {

        List<VertexType> E = new ArrayList<>();
        List<VertexType> A = new ArrayList<>();
        List<VertexType> I = new ArrayList<>();//x表示y_min,y表示y_max

        Collections.sort(alGraph.vertices, new Comparator<VNode>() {
            public int compare(VNode o1, VNode o2) {
                if (o1.data.x != o2.data.x) {
                    return ((Integer) o1.data.x).compareTo(o2.data.x);
                } else {
                    return ((Integer) o2.data.y).compareTo(o1.data.y);
                }

            }
        });
        for (int i = 0; i < alGraph.vertices.size(); i++) {
            int x = alGraph.vertices.get(i).data.x;
            int y = alGraph.vertices.get(i).data.y;

            if (x > v.x) {

                break;
            }
            if (belongToP(x, y, P)) {
                if (locatedInR1(x, y, v) && notBlocked(y, v.y, I)) {
                    A.add(new VertexType(x, y, 0));//id暂不需要
                    if (x == v.x && y == v.y) {
                        A.remove(A.size() - 1);
                    }
                }
            }

            if (Rectangle.rightup(o, x, y)) {
                if (locatedInR1(x, y, v) && notBlocked(y, v.y, I)) {
                    A.add(new VertexType(x, y, 0));//id暂不需要
                    if (x == v.x && y == v.y) {
                        A.remove(A.size() - 1);
                    }
                }

                //i+1个是rightup点
                if (i < alGraph.vertices.size() - 1){
                    int y1 = alGraph.vertices.get(i + 1).data.y;
                    if (x > v.x) {

                        break;
                    }

                    for (int j = 0; j < I.size(); j++) {
                        if (I.get(j).x == y && I.get(j).y == y1) {
                            I.remove(j);
                            break;
                        }
                    }
                    if (!(locatedInR1(x, y, v) && notBlocked(y, v.y, I))
                            &&locatedInR1(x, y1, v) && notBlocked(y1, v.y, I)) {
                        A.add(new VertexType(x, y1, 0));//id暂不需要
                        if (x == v.x && y1 == v.y) {
                            A.remove(A.size() - 1);
                        }
                    }


                    i++;
                }


            }

            if (Rectangle.leftup(o, x, y)) {
                if (locatedInR1(x, y, v) && notBlocked(y, v.y, I)) {
                    A.add(new VertexType(x, y, 0));//id暂不需要
                    if (x == v.x && y == v.y) {
                        A.remove(A.size() - 1);
                    }
                }
                //i+1个是leftup点
                if (i < alGraph.vertices.size() - 1) {
                    int y1 = alGraph.vertices.get(i + 1).data.y;
                    if (x > v.x) {

                        break;
                    }


                    if (x < v.x) {

                        I.add(new VertexType(y1, y, 0));//id暂不需要
                    }
                    for (int j = 0; j < A.size(); j++) {
                        if (notBlocked(A.get(j).y, v.y, I)) {

                        } else {
                            A.remove(j);
                            j--;
                        }
                    }
                    if (y1 == v.y) {

                        A.add(new VertexType(x, y1, 0));
                        if (x == v.x && y1 == v.y) {
                            A.remove(A.size() - 1);
                        }
                    }
                    if (x == v.x && y1 < v.y && notBlocked(y1, v.y, I)) {

                        A.add(new VertexType(x, y1, 0));
                    }

                    i++;

                }


            }
        }

        Neighbor.neighborOfV1(v, E, A);
        return E;

    }


    public static List<VertexType> oasg_r4(ALGraph alGraph, List<Rectangle> o, List<VertexType> P, VertexType v) {

        List<VertexType> E = new ArrayList<>();
        List<VertexType> A = new ArrayList<>();
        List<VertexType> I = new ArrayList<>();//x表示y_min,y表示y_max

        Collections.sort(alGraph.vertices, new Comparator<VNode>() {
            public int compare(VNode o1, VNode o2) {
                if (o1.data.x != o2.data.x) {
                    return ((Integer) o2.data.x).compareTo(o1.data.x);
                } else {
                    return ((Integer) o2.data.y).compareTo(o1.data.y);
                }

            }
        });
        for (int i = 0; i < alGraph.vertices.size(); i++) {
            int x = alGraph.vertices.get(i).data.x;
            int y = alGraph.vertices.get(i).data.y;

            if (x < v.x) {
                break;
            }
            if (belongToP(x, y, P)) {
                if (locatedInR4(x, y, v) && notBlocked(y, v.y, I)) {

                    A.add(new VertexType(x, y, 0));//id暂不需要
                    if (x == v.x && y == v.y) {
                        A.remove(A.size() - 1);
                    }
                }
            }

            if (Rectangle.leftup(o, x, y)) {
                if (locatedInR4(x, y, v) && notBlocked(y, v.y, I)) {
                    A.add(new VertexType(x, y, 0));//id暂不需要
                    if (x == v.x && y == v.y) {
                        A.remove(A.size() - 1);
                    }
                }

                //i+1个是rightup点
                if (i < alGraph.vertices.size() - 1){
                    int y1 = alGraph.vertices.get(i + 1).data.y;
                    if (x < v.x) {
                        break;
                    }

                    for (int j = 0; j < I.size(); j++) {
                        if (I.get(j).x == y && I.get(j).y == y1) {
                            I.remove(j);
                            break;
                        }
                    }
                    if (!(locatedInR4(x, y, v) && notBlocked(y, v.y, I))
                            &&locatedInR4(x, y1, v) && notBlocked(y1, v.y, I)) {
                        A.add(new VertexType(x, y1, 0));//id暂不需要
                        if (x == v.x && y1 == v.y) {
                            A.remove(A.size() - 1);
                        }
                    }


                    i++;
                }


            }

            if (Rectangle.rightup(o, x, y)) {
                if (locatedInR4(x, y, v) && notBlocked(y, v.y, I)) {
                    A.add(new VertexType(x, y, 0));//id暂不需要
                    if (x == v.x && y == v.y) {
                        A.remove(A.size() - 1);
                    }
                }
                //i+1个是leftup点
                if (i < alGraph.vertices.size() - 1) {
                    int y1 = alGraph.vertices.get(i + 1).data.y;
                    if (x < v.x) {
                        break;
                    }


                    if (x > v.x) {

                        I.add(new VertexType(y1, y, 0));//id暂不需要
                    }
                    for (int j = 0; j < A.size(); j++) {
                        if (notBlocked(A.get(j).y, v.y, I)) {

                        } else {
                            A.remove(j);
                            j--;
                        }
                    }

                    if (y1 == v.y) {

                        A.add(new VertexType(x, y1, 0));
                        if (x == v.x && y1 == v.y) {
                            A.remove(A.size() - 1);
                        }
                    }
                    if (x == v.x && y1 < v.y && notBlocked(y1, v.y, I)) {

                        A.add(new VertexType(x, y1, 0));
                    }

                    i++;

                }


            }
        }

        Neighbor.neighborOfV4(v, E, A);
        return E;

    }

    public static List<VertexType> oasg_r3(ALGraph alGraph, List<Rectangle> o, List<VertexType> P, VertexType v) {

        List<VertexType> E = new ArrayList<>();
        List<VertexType> A = new ArrayList<>();
        List<VertexType> I = new ArrayList<>();//x表示y_min,y表示y_max

        Collections.sort(alGraph.vertices, new Comparator<VNode>() {
            public int compare(VNode o1, VNode o2) {
                if (o1.data.x != o2.data.x) {
                    return ((Integer) o2.data.x).compareTo(o1.data.x);
                } else {
                    return ((Integer) o1.data.y).compareTo(o2.data.y);
                }

            }
        });
        for (int i = 0; i < alGraph.vertices.size(); i++) {
            int x = alGraph.vertices.get(i).data.x;
            int y = alGraph.vertices.get(i).data.y;

            if (x < v.x) {
                break;
            }
            if (belongToP(x, y, P)) {
                if (locatedInR3(x, y, v) && notBlocked(v.y, y, I)) {
                    A.add(new VertexType(x, y, 0));//id暂不需要
                    if (x == v.x && y == v.y) {
                        A.remove(A.size() - 1);
                    }
                }
            }

            if (Rectangle.leftlow(o, x, y)) {
                if (locatedInR3(x, y, v) && notBlocked(v.y, y, I)) {
                    A.add(new VertexType(x, y, 0));//id暂不需要
                    if (x == v.x && y == v.y) {
                        A.remove(A.size() - 1);
                    }
                }

                //i+1个是rightup点
                if (i < alGraph.vertices.size() - 1){
                    int y1 = alGraph.vertices.get(i + 1).data.y;
                    if (x < v.x) {
                        break;
                    }

                    for (int j = 0; j < I.size(); j++) {
                        if (I.get(j).x == y && I.get(j).y == y1) {
                            I.remove(j);
                            break;
                        }
                    }
                    if (!(locatedInR3(x, y, v) && notBlocked(v.y, y, I))
                            &&locatedInR3(x, y1, v) && notBlocked(v.y, y1, I)) {
                        A.add(new VertexType(x, y1, 0));//id暂不需要
                        if (x == v.x && y1 == v.y) {
                            A.remove(A.size() - 1);
                        }
                    }


                    i++;
                }


            }

            if (Rectangle.rightlow(o, x, y)) {
                if (locatedInR3(x, y, v) && notBlocked(v.y, y, I)) {
                    A.add(new VertexType(x, y, 0));//id暂不需要
                    if (x == v.x && y == v.y) {
                        A.remove(A.size() - 1);
                    }
                }
                //i+1个是leftup点
                if (i < alGraph.vertices.size() - 1) {
                    int y1 = alGraph.vertices.get(i + 1).data.y;
                    if (x < v.x) {
                        break;
                    }


                    if (x > v.x) {

                        I.add(new VertexType(y, y1, 0));//id暂不需要
                    }
                    for (int j = 0; j < A.size(); j++) {
                        if (notBlocked(v.y, A.get(j).y, I)) {

                        } else {
                            A.remove(j);
                            j--;
                        }
                    }
                    if (y1 == v.y) {

                        A.add(new VertexType(x, y1, 0));
                        if (x == v.x && y1 == v.y) {
                            A.remove(A.size() - 1);
                        }
                    }
                    if (x == v.x && y1 > v.y && notBlocked(v.y, y1, I)) {

                        A.add(new VertexType(x, y1, 0));
                        if (x == v.x && y1 == v.y) {
                            A.remove(A.size() - 1);
                        }
                    }

                    i++;

                }


            }
        }

        Neighbor.neighborOfV3(v, E, A);
        return E;

    }

/*
A中的点都在v的r2区域，返回其中的neighbors
A vertexf ∈ P ∪C is a neighbor of a vertexv ∈ P ∪C
if no other vertex in P ∪C or obstacle is inside or on the
boundary of the bounding box of v and f.

 */


/*
属于P中的点
 */
    public static boolean belongToP(int x, int y, List<VertexType> P) {
        for (VertexType vertexType:P
             ) {
            if (vertexType.x == x && vertexType.y == y) {

                return true;
            }
        }
        return false;
    }

/*
y<=y1,I中没有范围覆盖到y~y1则返回true
 */
    public static boolean notBlocked(int y, int y1, List<VertexType> I) {
        if (y > y1) {
            int i = y;
            y1 = i;
            y = y1;
        }
        for (int i = 0; i < I.size(); i++) {
            if (y >= I.get(i).y || y1 <= I.get(i).x) {

            } else {
                return false;
            }
        }
        return true;
    }


    public static void edgeConnection(List<Rectangle> rectangles,List<VNode> vss,ALGraph alGraph) {

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

        Set<VNode> a = new HashSet<>();
        for (int i = 0; i < vss.size(); i++) {
            if (!(map.containsKey(vss.get(i).data.x)
                    && map.get(vss.get(i).data.x).contains(vss.get(i).data.y))) {
                a.add(vss.get(i));
            } else {
                if (a.size() != 0) {
                    Set<VNode> as = new HashSet<>();
                    Iterator<VNode> iterator = a.iterator();
                    while (iterator.hasNext()) {
                        VNode v = iterator.next();
                        if (locatedInR2(v, vss.get(i))) {
                            iterator.remove();
                            as.add(v);
                        }
                    }

                    if (as.size() != 0) {

                        VNode vNode = findClosestVex(as, vss.get(i));
                        VNode vNode1 = findClosestVex(as, vss.get(i + 1));
                        addEdgeToGraph(alGraph, vNode, vss.get(i), 2);
                        addEdgeToGraph(alGraph, vNode1, vss.get(i + 1), 2);
                    }
                }
            }

        }
    }

    /*
    将连接两点(点在图中)的弧加入图，该弧原先不存在
     */
    public static void addEdgeToGraph(ALGraph alGraph, VNode vNode, VNode vNode1, double weight) {

        int i = alGraph.locatevex(vNode.data.x, vNode.data.y);
        int j = alGraph.locatevex(vNode1.data.x, vNode1.data.y);

        ArcNode arcNode = new ArcNode(i, alGraph.vertices.get(j).firstarc, weight);
        ArcNode arcNode1 = new ArcNode(j, alGraph.vertices.get(i).firstarc, weight);

        alGraph.vertices.get(j).firstarc = arcNode;
        alGraph.vertices.get(i).firstarc = arcNode1;

    }


    /*
    找出集合as中距离vnode最近的点并返回,没有就返回null
     */
    public static VNode findClosestVex(Set as, VNode vNode) {
        double min=Double.MAX_VALUE;
        VNode minResult = null;
        for (Object v:as
             ) {
            double distance = distance((VNode)v, vNode);
            if (min > distance) {
                min = distance;
                minResult = (VNode) v;
            }
        }
        return minResult;
    }

    /*
    计算两点的距离
     */
    public static double distance(VNode v, VNode vNode) {
        int x = v.data.x;
        int y = v.data.y;
        int x1 = vNode.data.x;
        int y1 = vNode.data.y;
        return Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
    }

    public static double distance(int x, int y, int x1, int y1) {

        return Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
    }

    public static int manhattan(int x, int y, int x1, int y1) {
        return Math.abs(x - x1) + Math.abs(y - y1);
    }

    /*
    如果v在v1的r2区域内，返回true，否则返回false
     */
    public static boolean locatedInR2(VNode v, VNode v1) {
        if (v.data.x <= v1.data.x && v.data.y >= v1.data.y) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean locatedInR2(int x, int y, VertexType v1) {
        if (x <= v1.x && y >= v1.y) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean locatedInR3(int x, int y, VertexType v1) {
        if (x >= v1.x && y >= v1.y) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean locatedInR4(int x, int y, VertexType v1) {
        if (x >= v1.x && y <= v1.y) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean locatedInR1(int x, int y, VertexType v1) {
        if (x <= v1.x && y <= v1.y) {
            return true;
        } else {
            return false;
        }
    }
    /*
    构造无向网,vertextTypes存顶点坐标，arcNodes存弧信息
     */
    public static void createUDN(ALGraph alGraph,int [][] vertextTypes,int[][] arcNodes) {
        //Scanner in = new Scanner(System.in);//定义scanner，等待输入
        //System.out.println("请输入你的vexnum：");
        alGraph.vexnum = vertextTypes.length;
        //System.out.println("请输入你的arcnum：");
        alGraph.arcnum = arcNodes.length;

        for (int i = 0; i < alGraph.vexnum; i++) {
            //System.out.println("请输入你的第"+i+"个点坐标");
            int x = vertextTypes[i][0];
            int y = vertextTypes[i][1];
            VertexType vertexType = new VertexType(x, y, i);
            VNode vNode = new VNode(vertexType, null);
            alGraph.getVertices().add(vNode);
        }

        for (int i = 0; i < alGraph.arcnum; i++) {
            //System.out.println("请输入你的第"+i+"个弧顶点序号和权");
            int x = arcNodes[i][0];
            int y = arcNodes[i][1];
            double z = arcNodes[i][2];

            //弧插入表头
            ArcNode a = alGraph.vertices.get(x).firstarc;
            ArcNode arcNode1 = new ArcNode(y, a, z);
            alGraph.vertices.get(x).firstarc = arcNode1;

            a = alGraph.vertices.get(y).firstarc;
            ArcNode arcNode2 = new ArcNode(x, a, z);
            alGraph.vertices.get(y).firstarc = arcNode2;
        }
    }

    public  static void draw(JPanel jPanel) {
        JFrame rect = new JFrame("绘制图形");

        //rect.remove(0);
        rect.add(jPanel);
        rect.setVisible(true);
        rect.setDefaultCloseOperation(3);

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int width = screenSize.width;
        int hight = screenSize.height;
        rect.setBounds(0, 0, 1000, 1000);

    }

}

