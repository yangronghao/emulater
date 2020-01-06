package com.company;

public class ArcNode {
    int adjvex;//另一个顶点在图中的id
    ArcNode nextarc;
    double weight;

    public ArcNode(int adjvex, ArcNode nextarc, double weight) {
        this.adjvex = adjvex;
        this.nextarc = nextarc;
        this.weight = weight;
    }

    public int getAdjvex() {
        return adjvex;
    }

    public void setAdjvex(int adjvex) {
        this.adjvex = adjvex;
    }

    public ArcNode getNextarc() {
        return nextarc;
    }

    public void setNextarc(ArcNode nextarc) {
        this.nextarc = nextarc;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public ArcNode copy() {
        ArcNode cur = new ArcNode(this.adjvex, null, this.weight);
        ArcNode res = cur;
        ArcNode first = this.nextarc;
        while (first != null) {

            ArcNode now = new ArcNode(first.adjvex, null, first.weight);
            cur.nextarc = now;
            cur = cur.nextarc;

            first = first.nextarc;
        }
        return res;
    }
}
