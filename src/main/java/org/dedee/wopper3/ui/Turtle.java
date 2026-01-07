package org.dedee.wopper3.ui;

import java.awt.geom.GeneralPath;
import java.util.Stack;

import org.dedee.wopper3.Commands;

public class Turtle {
	private Pos pos;
	private Stack<Pos> stackPositions;
	private GeneralPath gp;
	private WopperTurtleModel turtleModel;
	private Bounds bounds;

	public Turtle(GeneralPath gp, double degrees) {
		this.gp = gp;
		stackPositions = new Stack<Turtle.Pos>();
		bounds = new Bounds();
		pos = new Pos(0, 0, 0);
		gp.moveTo(0, 0);
	}

	public Turtle(GeneralPath gp, WopperTurtleModel turtleModel) {
		this.turtleModel = turtleModel;
		this.gp = gp;
		stackPositions = new Stack<Turtle.Pos>();
		bounds = new Bounds();
		pos = new Pos(0, 0, turtleModel.getInitialAngle());
		gp.moveTo(0, 0);
	}

	public void update() {
		for (int i = 0; i < turtleModel.getNumberOfCommands(); i++) {
			int cmd = turtleModel.getCommand(i);

			switch (cmd) {
			case Commands.ID_NOP:
				break;
			case Commands.ID_FWD_PAINT:
				move(10, true);
				break;
			case Commands.ID_FWD_MOVE:
				move(10, false);
				break;
			case Commands.ID_TURN_LEFT:
				turn(-turtleModel.getDegrees());
				break;
			case Commands.ID_TURN_RIGHT:
				turn(turtleModel.getDegrees());
				break;
			case Commands.ID_PUSH:
				push();
				break;
			case Commands.ID_POP:
				pop();
				break;
			case Commands.ID_TURNAROUND:
				turnaround();
				break;
			default:
				System.err.println("Ignoring command " + cmd);
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

	class Pos {
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

	class Bounds {
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
