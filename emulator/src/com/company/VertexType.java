package com.company;

public class VertexType {
    int x,y;
    int id;//为了顶点排序后也能保证弧指向不变，唯一标识顶点

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VertexType(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public VertexType copy() {
        return new VertexType(this.x, this.y, this.id);
    }
}
