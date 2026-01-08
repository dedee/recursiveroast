package org.dedee.recursiveroast.ui;

import java.awt.*;
import java.awt.geom.GeneralPath;


public class TurtleCanvas extends BaseCanvas {


    LSystemTurtleModel turtleModel;

    public TurtleCanvas(LSystemTurtleModel turtleModel) {
        this.turtleModel = turtleModel;
    }

    @Override
    public void paint(GeneralPath gp) {
        Turtle turtle = new Turtle(gp, turtleModel);
        turtle.update();
    }

    @Override
    public void paintLSystemText(Graphics2D g2) {
        g2.drawString(turtleModel.getLSystemAsString(), 10, 30);
    }

    @Override
    public void paintRecursionLevel(Graphics2D g2) {
        g2.drawString("Recursion: " + turtleModel.getRecursionLevel(), 10, 10);
    }

}
