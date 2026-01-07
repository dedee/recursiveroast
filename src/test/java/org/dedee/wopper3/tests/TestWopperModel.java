package org.dedee.wopper3.tests;

import org.dedee.wopper3.WopperModel;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestWopperModel {

	public static final String[] SIMPLE = new String[] { "rektiefe=1",
			"winkel=0", "r=F", "0: B= r", "1: r = r+r", };
	public static final String[] KOCHDREIECK = new String[] { "winkel=60",
			"r=F", "0: B=r", "1: r=r-r++r-r", };


	public static File createTempFile(String[] lines) throws IOException {
		File file = File.createTempFile("testcasewopper", "test");
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		for (String line : lines) {
			out.write(line);
			out.newLine();
		}
		out.close();
		return file;
	}

	@Test
	public void testOne() throws Exception {
		File file = createTempFile(SIMPLE);
		WopperModel wpm = new WopperModel();
		wpm.load(new BufferedReader(new FileReader(file)));

		assertEquals(1, wpm.getRecursionDepth());
		assertEquals(0, wpm.getAngle());
		// assertEquals("B", wpm.getRecursionData(0).get(0).what);
		// assertEquals("r", wpm.getRecursionData(0).get(0).value);
		// assertEquals("r", wpm.getRecursionData(1).get(0).name);
		// assertEquals("r+r", wpm.getRecursionData(1).get(0).value);
	}
}
