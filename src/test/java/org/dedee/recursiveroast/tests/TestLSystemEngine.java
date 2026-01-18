package org.dedee.recursiveroast.tests;

import org.dedee.recursiveroast.CmdList;
import org.dedee.recursiveroast.Commands;
import org.dedee.recursiveroast.LSystemEngine;
import org.dedee.recursiveroast.LSystemModel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLSystemEngine {
    private static final Logger logger = LoggerFactory.getLogger(TestLSystemEngine.class);

    @Test
    public void testSimple() throws Exception {
        Commands commands = new Commands();
        File file = TestLSystemModel.createTempFile(TestLSystemModel.SIMPLE);
        LSystemModel wpm = new LSystemModel(commands);
        wpm.load(new BufferedReader(new FileReader(file)));

        LSystemEngine wpe = new LSystemEngine(wpm);

        CmdList cl = wpe.normalize();
        assertEquals("F", cl.toString(commands));

        wpe.calculateNext();
        cl = wpe.normalize();
        assertEquals("F+F", cl.toString(commands));

        wpe.calculateNext();
        cl = wpe.normalize();
        assertEquals("F+F+F+F", cl.toString(commands));
    }

    @Test
    public void testKoch() throws Exception {
        Commands commands = new Commands();
        File file = TestLSystemModel.createTempFile(TestLSystemModel.KOCHDREIECK);
        LSystemModel wpm = new LSystemModel(commands);
        wpm.load(new BufferedReader(new FileReader(file)));

        LSystemEngine wpe = new LSystemEngine(wpm);

        CmdList cl = wpe.normalize();
        assertEquals("F", cl.toString(commands));

        wpe.calculateNext();
        cl = wpe.normalize();
        assertEquals("F-F++F-F", cl.toString(commands));
    }

    @Test
    public void testDrake() throws Exception {
        Commands commands = new Commands();
        LSystemModel wpm = new LSystemModel(commands);
        try (var in = getClass().getResourceAsStream("/files/drachenkurve.txt")) {
            if (in != null) {
                wpm.load(new BufferedReader(new java.io.InputStreamReader(in)));
            }
        }
        LSystemEngine wpe = new LSystemEngine(wpm);

        CmdList cl = wpe.normalize();
        assertEquals("F", cl.toString(commands));

        for (int i = 0; i < 5; i++)
            wpe.calculateNext();

        CmdList l = wpe.normalize();
        logger.info("Result length: {}", l.length());
        logger.info("Result: {}", l.toString(commands));
    }

}
