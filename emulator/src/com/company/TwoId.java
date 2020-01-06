package com.company;

public class TwoId {
    int id;
    int id1;

    @Override
    public boolean equals(Object obj) {
        TwoId obj1 = (TwoId) obj;

        return (id==obj1.id&&id1==obj1.id1)||
                (id1==obj1.id&&id==obj1.id1);
    }

    @Override
    public int hashCode() {
        return this.id+this.id1;
    }

    public TwoId(int id, int id1) {
        this.id = id;
        this.id1 = id1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }
}
