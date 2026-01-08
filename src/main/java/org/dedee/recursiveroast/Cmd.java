package org.dedee.recursiveroast;

public class Cmd {

    private final int id;

    private int[] values;

    public Cmd(int id) {
        this.id = id;
    }

    public Cmd(int id, int[] values) {
        this.id = id;
        this.values = values;
    }

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
