package org.dedee.wopper3.ui;

public interface WopperTurtleModel {
    double getDegrees();

    int getNumberOfCommands();

    int getCommand(int index);

    String getLSystemAsString();

    int getRecursionLevel();

    double getInitialAngle();
}
