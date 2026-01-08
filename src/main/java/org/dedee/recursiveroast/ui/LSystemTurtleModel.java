package org.dedee.recursiveroast.ui;

public interface LSystemTurtleModel {
    double getDegrees();

    int getNumberOfCommands();

    int getCommand(int index);

    String getLSystemAsString();

    int getRecursionLevel();

    double getInitialAngle();
}
