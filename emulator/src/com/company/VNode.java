package com.company;

public class VNode {
    VertexType data;
    ArcNode firstarc;

    public VNode(VertexType data, ArcNode firstarc) {
        this.data = data;
        this.firstarc = firstarc;
    }

    public VertexType getData() {
        return data;
    }

    public void setData(VertexType data) {
        this.data = data;
    }

    public ArcNode getFirstarc() {
        return firstarc;
    }

    public void setFirstarc(ArcNode firstarc) {
        this.firstarc = firstarc;
    }

    public int arcLength() {
        int i = 0;
        ArcNode cur = this.firstarc;
        while (cur != null) {
            i++;
            cur = cur.nextarc;

        }
        return i;
    }

    /*
    返回第i个边，i从0开始
     */
    public ArcNode get(int i) {
        ArcNode cur = this.firstarc;
        while (i-- > 0) {
            cur = cur.nextarc;
        }
        return cur;
    }

    public VNode copy() {
        ArcNode cur = null;
        VertexType vertexType = null;
        if (this.firstarc != null) {

            cur = this.firstarc.copy();
        }

        if (this.data != null) {

            vertexType = this.data.copy();
        }
        return new VNode(vertexType, cur);
    }
}
