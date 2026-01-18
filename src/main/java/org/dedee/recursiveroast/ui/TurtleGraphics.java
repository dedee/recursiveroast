package org.dedee.recursiveroast.ui;

import org.dedee.recursiveroast.Cmd;
import org.dedee.recursiveroast.Commands;
import org.dedee.recursiveroast.ILSystemEngine;
import org.dedee.recursiveroast.LSystemEngineFactory;
import org.dedee.recursiveroast.LSystemModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main application class for the L-System turtle graphics viewer.
 * Provides a GUI for loading, viewing, and manipulating L-System fractals.
 */
public class TurtleGraphics {
    private static final Logger logger = LoggerFactory.getLogger(TurtleGraphics.class);

    static Commands commands;
    static ILSystemEngine.NormalizedResult cmdList;
    static LSystemModel wpm;
    static ILSystemEngine wpe;
    static List<String> files;
    static String actualFile;
    private static TurtleCanvas canvas;
    private static Choice choice;
    private static TextArea textArea;

    public static void main(String[] args) throws Exception {

        files = listFiles();

        // Create Commands instance (Dependency Injection)
        commands = new Commands();

        Frame f = new Frame("TurtleGraphics");
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        BorderLayout layout = new BorderLayout();
        layout.setHgap(10);
        layout.setVgap(10);
        f.setLayout(layout);

        wpm = new LSystemModel(commands);
        //wpe = LSystemEngineFactory.createStandard(wpm);
        wpe = LSystemEngineFactory.createDirect(wpm);

        canvas = new TurtleCanvas(new LSystemTurtleModel() {
            @Override
            public double getDegrees() {
                return wpm.getAngle();
            }

            @Override
            public int getNumberOfCommands() {
                if (cmdList == null) return 0;
                return cmdList.length();
            }

            @Override
            public int getCommand(int index) {
                return Cmd.getId(cmdList.get(index));
            }

            @Override
            public String getLSystemAsString() {
                return wpm.getLSystemAsString();
            }

            @Override
            public int getRecursionLevel() {
                return wpe.getRecursionLevel();
            }

            @Override
            public double getInitialAngle() {
                return wpm.getInitialAngle();
            }
        });
        f.add(canvas, BorderLayout.CENTER);

        choice = new Choice();
        for (String file : files)
            choice.add(file);
        choice.addItemListener(e -> {
            logger.debug("Selected file: {}", e);
            String filename = (String) e.getItem();
            loadFile(filename);
        });
        Panel pn = new Panel(new FlowLayout());

        Button btnPreviousFile = new Button("<");
        btnPreviousFile.addActionListener(e -> {
            int index = files.indexOf(actualFile) - 1;
            if (index < 0)
                index = files.size() - 1;
            loadFile(files.get(index));
        });

        Button btnNextFile = new Button(">");
        btnNextFile.addActionListener(e -> {
            int index = files.indexOf(actualFile) + 1;
            if (index >= files.size())
                index = 0;
            loadFile(files.get(index));
        });

        Button btnMinus = new Button("-");
        btnMinus.addActionListener(e -> {
            wpe.calculatePrevious();
            cmdList = wpe.normalize();
            canvas.repaint();
        });
        Button btnPlus = new Button("+");
        btnPlus.addActionListener(e -> {
            wpe.calculateNext();
            cmdList = wpe.normalize();
            canvas.repaint();
        });

        pn.add(btnPreviousFile);
        pn.add(btnMinus);
        pn.add(choice);
        pn.add(btnPlus);
        pn.add(btnNextFile);
        f.add(pn, BorderLayout.NORTH);

        Panel east = new Panel(new BorderLayout());
        textArea = new TextArea();
        textArea.setPreferredSize(new Dimension(300, 200));
        east.add(textArea, BorderLayout.CENTER);
        Button refresh = new Button("Refresh");
        refresh.addActionListener(ae -> loadFromText(textArea.getText()));
        east.add(refresh, BorderLayout.SOUTH);
        f.add(east, BorderLayout.EAST);

        f.pack();
        f.setVisible(true);

        if (!files.isEmpty()) {
            loadFile(files.get(0));
        }
    }

    private static List<String> listFiles() {
        List<String> files = new ArrayList<>();
        try (InputStream in = TurtleGraphics.class.getResourceAsStream("/files/index.txt")) {
            if (in == null) {
                logger.error("Could not find /files/index.txt in resources");
                return Collections.emptyList();
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        files.add(line);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error reading index.txt: {}", e.getMessage());
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(files);
    }

    private static void loadFile(String filename) {
        logger.info("Loading L-System file: {}", filename);
        try (InputStream in = TurtleGraphics.class.getResourceAsStream("/files/" + filename)) {
            if (in == null) {
                logger.error("File not found in resources: {}", filename);
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                load(reader);
                actualFile = filename;
                if (choice != null) {
                    choice.select(files.indexOf(filename));
                }
            }
        } catch (IOException e1) {
            logger.error("Error loading file {}: {}", filename, e1.getMessage());
        }
    }

    private static void loadFromText(String text) {
        logger.info("Reloading L-System from text editor");
        try {
            BufferedReader in = new BufferedReader(new StringReader(text));
            load(in);
        } catch (IOException e1) {
            logger.error("Error loading from text: {}", e1.getMessage());
        }
    }

    private static void load(BufferedReader in) throws IOException {
        // Create new Commands instance for fresh L-System
        commands = new Commands();
        wpm = new LSystemModel(commands);
        wpm.load(in);

        textArea.setText(wpm.getLSystemAsString());
        wpe = LSystemEngineFactory.createStandard(wpm);

        for (int i = 0; i < wpm.getRecursionDepth(); i++)
            wpe.calculateNext();

        cmdList = wpe.normalize();

        if (canvas != null)
            canvas.repaint();
    }

}
