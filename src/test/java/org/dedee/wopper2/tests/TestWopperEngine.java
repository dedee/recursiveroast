package org.dedee.wopper2.tests;

import org.dedee.wopper2.CmdList;
import org.dedee.wopper2.WopperEngine;
import org.dedee.wopper2.WopperModel;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestWopperEngine {

    @Test
    public void testSimple() throws Exception {
        File file = TestWopperModel.createTempFile(TestWopperModel.SIMPLE);
        WopperModel wpm = new WopperModel();
        wpm.load(new BufferedReader(new FileReader(file)));

        WopperEngine wpe = new WopperEngine(wpm);

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
        File file = TestWopperModel.createTempFile(TestWopperModel.KOCHDREIECK);
        WopperModel wpm = new WopperModel();
        wpm.load(new BufferedReader(new FileReader(file)));

        WopperEngine wpe = new WopperEngine(wpm);

        CmdList cl = wpe.normalize();
        assertEquals("F", cl.toString());

        wpe.calculateNext();
        cl = wpe.normalize();
        assertEquals("F-F++F-F", cl.toString());
    }

    @Test
    public void testDrake() throws Exception {
        WopperModel wpm = new WopperModel();
        try (var in = getClass().getResourceAsStream("/files/drachenkurve.txt")) {
            if (in != null) {
                wpm.load(new BufferedReader(new java.io.InputStreamReader(in)));
            }
        }
        WopperEngine wpe = new WopperEngine(wpm);

        CmdList cl = wpe.normalize();
        assertEquals("F", cl.toString());

        for (int i = 0; i < 5; i++)
            wpe.calculateNext();

        CmdList l = wpe.normalize();
        System.out.println(l.length());
        System.out.println(l);
    }

}
