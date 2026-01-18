package org.dedee.recursiveroast.ui;

import org.dedee.recursiveroast.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.GeneralPath;
import java.util.Stack;

/**
 * A turtle graphics implementation for rendering L-Systems.
 * The turtle moves on a 2D plane, executing commands like forward, turn, push/pop position.
 */
public class Turtle {
    private static final Logger logger = LoggerFactory.getLogger(Turtle.class);
    private static final double DEFAULT_DISTANCE = 10.0;

    private final Stack<Pos> stackPositions;
    private final GeneralPath gp;
    private final Bounds bounds;
    private Pos pos;
    private final LSystemTurtleModel turtleModel;

    public Turtle(GeneralPath gp, LSystemTurtleModel turtleModel) {
        this.turtleModel = turtleModel;
        this.gp = gp;
        stackPositions = new Stack<>();
        bounds = new Bounds();
        pos = new Pos(0, 0, turtleModel.getInitialAngle());
        gp.moveTo(0, 0);
    }

    public void update() {
        for (int i = 0; i < turtleModel.getNumberOfCommands(); i++) {
            int cmd = turtleModel.getCommand(i);

            switch (cmd) {
                case Commands.ID_NOP -> {
                    // No operation
                }
                case Commands.ID_FWD_PAINT -> move(DEFAULT_DISTANCE, true);
                case Commands.ID_FWD_MOVE -> move(DEFAULT_DISTANCE, false);
                case Commands.ID_TURN_LEFT -> turn(-turtleModel.getDegrees());
                case Commands.ID_TURN_RIGHT -> turn(turtleModel.getDegrees());
                case Commands.ID_PUSH -> push();
                case Commands.ID_POP -> pop();
                case Commands.ID_TURNAROUND -> turnaround();
                default -> logger.warn("Ignoring unknown command: {}", cmd);
            }
        }
    }

    public void move(double distance, boolean drawLine) {
        pos.x = pos.x + Math.cos(pos.angle * Math.PI / 180) * distance;
        pos.y = pos.y - Math.sin(pos.angle * Math.PI / 180) * distance;
        bounds.check(pos); // check bounds
        if (drawLine)
            gp.lineTo(pos.x, pos.y);
        else
            gp.moveTo(pos.x, pos.y);
    }

    public void turn(double degrees) {
        this.pos.angle += degrees;
    }

    public void push() {
        stackPositions.push(pos.clony());
    }

    public void pop() {
        pos = stackPositions.pop().clony();
    }

    private void turnaround() {
        pos.angle += 180;
    }

    static class Pos {
        double x;
        double y;
        double angle;

        public Pos(double x, double y, double angle) {
            this.x = x;
            this.y = y;
            this.angle = angle;
        }

        public Pos clony() {
            return new Pos(x, y, angle);
        }
    }

	static class Bounds {
        double xmin;
        double xmax;
        double ymin;
        double ymax;

        public void check(Pos pos) {
            double x = pos.x;
            double y = pos.y;
            if (x < xmin)
                xmin = x;
            if (x > xmax)
                xmax = x;
            if (y < ymin)
                ymin = y;
            if (y > ymax)
                ymax = y;
        }
    }
}
