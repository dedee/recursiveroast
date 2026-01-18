package org.dedee.recursiveroast.tests;

import org.dedee.recursiveroast.Commands;
import org.dedee.recursiveroast.LSystemModel;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLSystemModel {

    public static final String[] SIMPLE = new String[]{"rektiefe=1",
            "winkel=0", "r=F", "0: B= r", "1: r = r+r",};
    public static final String[] KOCHDREIECK = new String[]{"winkel=60",
            "r=F", "0: B=r", "1: r=r-r++r-r",};


    public static File createTempFile(String[] lines) throws IOException {
        File file = File.createTempFile("testcaserecursiveroast", "test");
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
        Commands commands = new Commands();
        File file = createTempFile(SIMPLE);
        LSystemModel wpm = new LSystemModel(commands);
        wpm.load(new BufferedReader(new FileReader(file)));

        assertEquals(1, wpm.getRecursionDepth());
        assertEquals(0, wpm.getAngle());
        // assertEquals("B", wpm.getRecursionData(0).get(0).what);
        // assertEquals("r", wpm.getRecursionData(0).get(0).value);
        // assertEquals("r", wpm.getRecursionData(1).get(0).name);
        // assertEquals("r+r", wpm.getRecursionData(1).get(0).value);
    }
}
