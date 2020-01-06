package com.company.MST;

import com.company.ALGraph;

public class MSTContext {
    private MST mst;

    public MST getMst() {
        return mst;
    }

    public void setMst(MST mst) {
        this.mst = mst;
    }

    public void getMST(ALGraph alGraph) {
        mst.getMST(alGraph);

    }
}
