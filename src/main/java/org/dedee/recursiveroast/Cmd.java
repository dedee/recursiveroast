package org.dedee.recursiveroast;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a command in an L-System with an ID and optional parameter values.
 */
public class Cmd {

    private final int id;
    @Nullable
    private final int[] values;

    public Cmd(int id) {
        this.id = id;
        this.values = null;
    }

    public Cmd(int id, @Nullable int[] values) {
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

    /**
     * Returns the values array.
     * Note: The returned array should not be modified by callers.
     *
     * @return the values array, or null if no values are set
     */
    @Nullable
    public int[] getValues() {
        return values;
    }

}
