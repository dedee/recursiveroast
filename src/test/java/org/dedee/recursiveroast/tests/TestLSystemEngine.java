package org.dedee.recursiveroast.tests;

import org.dedee.recursiveroast.CmdList;
import org.dedee.recursiveroast.LSystemEngine;
import org.dedee.recursiveroast.LSystemModel;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLSystemEngine {

    @Test
    public void testSimple() throws Exception {
        File file = TestLSystemModel.createTempFile(TestLSystemModel.SIMPLE);
        LSystemModel wpm = new LSystemModel();
        wpm.load(new BufferedReader(new FileReader(file)));

        LSystemEngine wpe = new LSystemEngine(wpm);

        CmdList cl = wpe.normalize();
        assertEquals("F", cl.toString());

        wpe.calculateNext();
        cl = wpe.normalize();
        assertEquals("F+F", cl.toString());

        wpe.calculateNext();
        cl = wpe.normalize();
        assertEquals("F+F+F+F", cl.toString());
    }

    @Test
    public void testKoch() throws Exception {
        File file = TestLSystemModel.createTempFile(TestLSystemModel.KOCHDREIECK);
        LSystemModel wpm = new LSystemModel();
        wpm.load(new BufferedReader(new FileReader(file)));

        LSystemEngine wpe = new LSystemEngine(wpm);

        CmdList cl = wpe.normalize();
        assertEquals("F", cl.toString());

        wpe.calculateNext();
        cl = wpe.normalize();
        assertEquals("F-F++F-F", cl.toString());
    }

    @Test
    public void testDrake() throws Exception {
        LSystemModel wpm = new LSystemModel();
        try (var in = getClass().getResourceAsStream("/files/drachenkurve.txt")) {
            if (in != null) {
                wpm.load(new BufferedReader(new java.io.InputStreamReader(in)));
            }
        }
        LSystemEngine wpe = new LSystemEngine(wpm);

        CmdList cl = wpe.normalize();
        assertEquals("F", cl.toString());

        for (int i = 0; i < 5; i++)
            wpe.calculateNext();

        CmdList l = wpe.normalize();
        System.out.println(l.length());
        System.out.println(l);
    }

}
