package org.dedee.recursiveroast.ui;

/**
 * Interface for accessing L-System data for turtle graphics rendering.
 */
public interface LSystemTurtleModel {
    double getDegrees();

    int getNumberOfCommands();

    int getCommand(int index);

    String getLSystemAsString();

    int getRecursionLevel();

    double getInitialAngle();
}
