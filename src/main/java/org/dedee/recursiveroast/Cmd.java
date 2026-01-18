package org.dedee.recursiveroast;

/**
 * Represents a command in an L-System with an ID and optional parameter values.
 */
public class Cmd {

    private final int id;
    private final int[] values;

    public Cmd(int id) {
        this.id = id;
        this.values = null;
    }

    public Cmd(int id, int[] values) {
        this.id = id;
        this.values = values;
    }

    /**
     * Extracts the command ID from a value. Currently a pass-through,
     * but kept for future extensibility (e.g., encoding parameters in the value).
     * 
     * @param value the encoded command value
     * @return the command ID
     */
    public static int getId(int value) {
        return value;
    }

    public int getId() {
        return id;
    }

    public int[] getValues() {
        return values;
    }

}
