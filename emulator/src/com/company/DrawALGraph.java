package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DrawALGraph extends JPanel {
    ALGraph alGraph;

    public DrawALGraph(ALGraph alGraph) {
        this.alGraph = alGraph;
    }
    public void paint(Graphics g) {
        //空心矩形的坐标及其长宽

        List<VNode> vNodeList = this.alGraph.vertices;
        for (int i = 0; i < vNodeList.size(); i++) {
            int x = vNodeList.get(i).data.x;
            int y = vNodeList.get(i).data.y;
            g.drawOval( x*10-2,y*10-2,4,4);

            ArcNode arcNode = vNodeList.get(i).firstarc;
            while (arcNode != null) {

                int x1 = vNodeList.get(alGraph.locatevex(arcNode.adjvex)).data.x;
                int y1 = vNodeList.get(alGraph.locatevex(arcNode.adjvex)).data.y;
                g.drawLine(x*10, y*10, x1*10, y1*10);
                arcNode = arcNode.nextarc;
            }
        }




    }
}
