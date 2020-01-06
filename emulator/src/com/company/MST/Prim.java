package com.company.MST;

import com.company.ALGraph;
import com.company.VertexType;

public class Prim implements MST{

    @Override
    public void getMST(ALGraph alGraph) {

        double[][] prim = ALGraph.createprimtree(alGraph);

        alGraph.deleteArc();

        for (int i = 1; i < prim[1].length; i++) {
            int x = alGraph.getVertices().get(i).getData().getX();
            int x1 = alGraph.getVertices().get((int) prim[1][i]).getData().getX();
            int y = alGraph.getVertices().get(i).getData().getY();
            int y1 = alGraph.getVertices().get((int) prim[1][i]).getData().getY();
            int id = alGraph.getVertices().get(i).getData().getId();
            int id1 = alGraph.getVertices().get((int) prim[1][i]).getData().getId();
            double weight = prim[0][i];
            alGraph.connectPoint(new VertexType(x, y, id), new VertexType(x1, y1, id1), weight);
        }
    }
}
