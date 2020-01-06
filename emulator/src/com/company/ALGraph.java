package com.company;

import sun.security.provider.certpath.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ALGraph {
    List<VNode> vertices;
    int vexnum,arcnum;
    int kind;

    public ALGraph() {
        this.vertices = new ArrayList<>();
        this.arcnum = 0;
        this.vexnum = 0;
        this.kind = 0;
    }

    public ALGraph(List<VNode> vertices, int vexnum, int arcnum, int kind) {
        this.vertices = vertices;
        this.vexnum = vexnum;
        this.arcnum = arcnum;
        this.kind = kind;
    }

    public List<VNode> getVertices() {
        return vertices;
    }

    public void setVertices(List<VNode> vertices) {
        this.vertices = vertices;
    }

    public int getVexnum() {
        return vexnum;
    }

    public void setVexnum(int vexnum) {
        this.vexnum = vexnum;
    }

    public int getArcnum() {
        return arcnum;
    }

    public void setArcnum(int arcnum) {
        this.arcnum = arcnum;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
/*
根据坐标找点的位置
 */
    public int locatevex(int x, int y) {
        for (int i = 0; i < this.vertices.size(); i++) {
            if (x == this.vertices.get(i).data.x &&
                    y == this.vertices.get(i).data.y) {
                return i;
            }

        }
        return -1;
    }
    public  int locatevex(int id) {
        for (int i = 0; i < this.vertices.size(); i++) {
            if (id == this.vertices.get(i).data.id ) {
                return i;
            }

        }
        return -1;
    }

    /*
    无向图 连图中两点
     */
    public void connectPoint(VertexType v, VertexType e,double weight) {

        int i = locatevex(v.x, v.y);
        int j = locatevex(e.x, e.y);
        ArcNode cur = this.vertices.get(j).firstarc;
        while (cur != null) {
            int x = locatevex(cur.adjvex);
            if (x == i) {
                return;
            }

            cur = cur.nextarc;
        }
        ArcNode arcNode = new ArcNode(this.vertices.get(j).data.id, this.vertices.get(i).firstarc, weight);
        this.vertices.get(i).firstarc = arcNode;
        arcNode = new ArcNode(this.vertices.get(i).data.id, this.vertices.get(j).firstarc, weight);
        this.vertices.get(j).firstarc = arcNode;
    }



/*
v1是id，根据id找源点 返回两个数组，第一个记录距离，第二个记录路径(路径指加入点集时连的边，vertices[key]点和vertices[value]点相连)，数组序号都与vertices序号对应，
源点路径为-1，距离为0
 */
    public double[][] dijkstra(int v1) {
        int v = this.locatevex(v1);
        if (v < 0 || v >= vexnum)
            throw new ArrayIndexOutOfBoundsException();
        boolean[] st = new boolean[vexnum];// 默认初始为false,与顶点数组对应
        double[] distance = new double[vexnum];// 存放源点到其他点的距离,与顶点数组对应

        double[] pre = new double[vexnum];

        for (int i = 0; i < vexnum; i++) {
            distance[i] = Double.MAX_VALUE;
            pre[i] = -1;
        }
        ArcNode current;
        current = this.vertices.get(v).firstarc;
        while (current != null) {
            distance[this.locatevex(current.adjvex)] = current.weight;
            pre[this.locatevex(current.adjvex)] = v;
            current = current.nextarc;
        }
        distance[v] = 0;
        st[v] = true;

        for (int i = 0; i < vexnum; i++) {
            double min = Double.MAX_VALUE;
            int index = -1;
            // 比较从源点到其余顶点的路径长度
            for (int j = 0; j < vexnum; j++) {
                // 从源点到j顶点的最短路径还没有找到
                if (st[j] == false) {
                    // 从源点到j顶点的路径长度最小
                    if (distance[j] < min) {
                        index = j;
                        min = distance[j];
                    }
                }
            }
            // 找到源点到索引为index顶点的最短路径长度
            if (index != -1) {
                st[index] = true;
                // 更新当前最短路径及距离

                current = this.vertices.get(index).firstarc;
                while (current != null) {
                    int j = locatevex(current.adjvex);
                    if (!st[j]) {
                        if (current.weight + distance[index] < distance[j]) {
                            distance[j] = current.weight + distance[index];
                            pre[j] = index;
                        }
                    }
                    current = current.nextarc;
                }
            }
        }
        double[][] result = new double[2][vexnum];
        for (int i = 0; i < vexnum; i++) {
            result[0][i] = distance[i];
            result[1][i] = pre[i];
        }
        return result;
    }



/*
由图G生成最小生成树，初始化primtree的顶点
 */
    public static void initprimtree(ALGraph G,ALGraph primtree)            /*初始化最小生成树*/
    {
        int i;
        for(i=0;i<G.vexnum;i++)
        {
            primtree.vertices.add(new VNode(G.vertices.get(i).data, null));
        }
        primtree.vexnum = G.vexnum;
        primtree.arcnum = 0;
        primtree.kind = 0;
    }
/*
以点生成图
 */
    public static void initGraph(List<VertexType> P, ALGraph alGraph) {

        for (VertexType p:P
             ) {
            alGraph.vertices.add(new VNode(p, null));
        }
        alGraph.vexnum = P.size();
    }


    /*
 返回两个数组，第一个记录距离(距离指连边时的边长度)，第二个记录路径(路径指加入点集时连的边，vertices[key]点和vertices[value]点相连)，数组序号都与vertices序号对应，
源点路径为-1，距离为0,序号为0
 */
    public static double[][] createprimtree(ALGraph G) {
        boolean visit[] = new boolean[G.vexnum];
        visit[0] = true;
        double[] distance = new double[G.vexnum];// 存放源点到其他点的距离,与顶点数组对应

        double[] pre = new double[G.vexnum];
        for (int i = 0; i < G.vexnum; i++) {
            distance[i] = Double.MAX_VALUE;
            pre[i] = -1;
        }

        ArcNode current;
        current = G.vertices.get(0).firstarc;
        while (current != null) {
            distance[G.locatevex(current.adjvex)] = current.weight;
            pre[G.locatevex(current.adjvex)] = 0;
            current = current.nextarc;
        }
        distance[0] = 0;

        for (int i = 0; i < G.vexnum; i++) {
            double min = Double.MAX_VALUE;
            int index = -1;
            // 比较从源点到其余顶点的路径长度
            for (int j = 0; j < G.vexnum; j++) {
                // 从源点到j顶点的最短路径还没有找到
                if (visit[j] == false) {
                    // 从源点到j顶点的路径长度最小
                    if (distance[j] < min) {
                        index = j;
                        min = distance[j];
                    }
                }
            }
            // 找到源点到索引为index顶点的最短路径长度
            if (index != -1) {
                visit[index] = true;
                // 更新当前最短路径及距离

                current = G.vertices.get(index).firstarc;
                while (current != null) {
                    int j = G.locatevex(current.adjvex);
                    if (!visit[j]) {
                        if (current.weight < distance[j]) {
                            distance[j] = current.weight;
                            pre[j] = index;

                        }
                    }
                    current = current.nextarc;
                }
            }
        }
        double[][] result = new double[2][G.vexnum];
        for (int i = 0; i < G.vexnum; i++) {
            result[0][i] = distance[i];
            result[1][i] = pre[i];
        }
        return result;
    }


    public static void connectByPath(ALGraph alGraph,double[][] path, int id, int id1) {
        int i = alGraph.locatevex(id);
        int j = alGraph.locatevex(id1);
        if (path[1][i] == -1) {
            while (path[1][j] != -1) {

                alGraph.connectPoint(alGraph.vertices.get(j).data, alGraph.vertices.get((int) path[1][j]).data, 0);

                j = (int) path[1][j];
            }
        } else if (path[1][j] == -1) {
            while (path[1][i] != -1) {

                alGraph.connectPoint(alGraph.vertices.get(i).data, alGraph.vertices.get((int) path[1][i]).data, 0);

                i = (int) path[1][i];
            }
        } else {
            System.out.println("ERROR connectByPath");
        }
    }

/*
复制
 */
    public ALGraph copy() {
        ALGraph alGraph = new ALGraph(new ArrayList<>(), this.vexnum, this.arcnum, this.kind);
        for (int i = 0; i < this.vertices.size(); i++) {

            VNode vNode = this.vertices.get(i).copy();
            alGraph.vertices.add(vNode);

        }
        return alGraph;
    }

    /*
    将单独点从图中去掉
     */
    public void deleteVex() {
        for (int i = 0; i < this.vexnum; i++) {
            if (this.vertices.get(i).firstarc == null) {
                this.vertices.remove(i);
                i--;
                this.vexnum--;
            }
        }

    }
/*
//删除图的所有边
 */
    public void deleteArc() {
        for (int i = 0; i < this.vertices.size(); i++) {
            this.vertices.get(i).firstarc = null;
        }
    }

    /*
    根据id判断，如果图中存在该边，返回true
     */
    public boolean existArc(VertexType v, VertexType m) {
        int x = v.x;
        int y = v.y;
        int id = v.id;
        int id1 = m.id;
        int x1 = m.x;
        int y1 = m.y;

        for (int i = 0; i < this.vexnum; i++) {
            if (this.vertices.get(i).data.id == id) {
                ArcNode arcNode = this.vertices.get(i).firstarc;
                while (arcNode != null) {
                    if (arcNode.adjvex == id1) {
                        return true;
                    }
                    arcNode = arcNode.nextarc;
                }
            }
        }
        return false;
    }

    public static void prim(ALGraph alGraph) {
        double[][] prim = ALGraph.createprimtree(alGraph);

        alGraph.deleteArc();

        for (int i = 1; i < prim[1].length; i++) {
            int x = alGraph.vertices.get(i).data.x;
            int x1 = alGraph.vertices.get((int) prim[1][i]).data.x;
            int y = alGraph.vertices.get(i).data.y;
            int y1 = alGraph.vertices.get((int) prim[1][i]).data.y;
            int id = alGraph.vertices.get(i).data.id;
            int id1 = alGraph.vertices.get((int) prim[1][i]).data.id;
            double weight = prim[0][i];
            alGraph.connectPoint(new VertexType(x, y, id), new VertexType(x1, y1, id1), weight);
        }
    }

    public static ALGraph createByEdges(List<Edge> edges) {

        ALGraph alGraph = new ALGraph();
        Set<Point> set = new HashSet<>();
        for (int i = 0; i < edges.size(); i++) {

            set.add(new Point(edges.get(i).x1, edges.get(i).y1));
            set.add(new Point(edges.get(i).x2, edges.get(i).y2));
        }
        int id = 0;
        for (Point p:set
             ) {
            alGraph.vertices.add(new VNode(new VertexType(p.x, p.y, id), null));
            id++;
        }

        alGraph.vexnum = set.size();
        alGraph.arcnum = edges.size();

        for (int i = 0; i < edges.size(); i++) {

            int m = alGraph.locatevex(edges.get(i).x1, edges.get(i).y1);
            int n = alGraph.locatevex(edges.get(i).x2, edges.get(i).y2);
            double weight = edges.get(i).length;
            ArcNode arcNode = alGraph.vertices.get(m).firstarc;
            alGraph.vertices.get(m).firstarc = new ArcNode(alGraph.vertices.get(n).data.id, arcNode, weight);
            arcNode = alGraph.vertices.get(n).firstarc;
            alGraph.vertices.get(n).firstarc = new ArcNode(alGraph.vertices.get(m).data.id, arcNode, weight);
        }
        return alGraph;
    }

}

