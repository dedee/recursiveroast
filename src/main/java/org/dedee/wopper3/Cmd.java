package org.dedee.wopper3;

public class Cmd {

	private int id;

	private int[] values;

	public Cmd(int id) {
		this.id = id;
	}

	public Cmd(int id, int[] values) {
		this.id = id;
		this.values = values;
	}

	public int getId() {
		return id;
	}

	public int[] getValues() {
		return values;
	}

	public static int getId(int value) {
		return value;
	}

}
