package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DrawRectanglePanel extends JPanel {
    List<Rectangle> rectangle;
    public DrawRectanglePanel(List<Rectangle> rectangle) {
        this.rectangle = rectangle;
    }
    public void paint(Graphics g) {
        //空心矩形的坐标及其长宽

        for (int i = 0; i < this.rectangle.size(); i++) {
            Rectangle rectangle = this.rectangle.get(i);
            g.drawRect(rectangle.x1*10, rectangle.y1*10, rectangle.x2*10-rectangle.x1*10, rectangle.y2*10-rectangle.y1*10);
        }


    }
}