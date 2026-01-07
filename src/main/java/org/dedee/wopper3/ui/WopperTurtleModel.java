package org.dedee.wopper3.ui;

public interface WopperTurtleModel {
	public double getDegrees();

	public int getNumberOfCommands();

	public int getCommand(int index);

	public String getLSystemAsString();

	public int getRecursionLevel();

	public double getInitialAngle();
}
