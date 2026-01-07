package org.dedee.wopper3.ui;

import org.dedee.wopper3.Cmd;
import org.dedee.wopper3.CmdList;
import org.dedee.wopper3.WopperEngine;
import org.dedee.wopper3.WopperModel;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TurtleGraphics {
    static CmdList cmdList;
    static WopperModel wpm;
    static WopperEngine wpe;
    static List<File> files;
    static File actualFile;
    private static TurtleCanvas canvas;
    private static Choice choice;
    private static TextArea textArea;

    public static void main(String[] args) throws Exception {

        files = listFiles();

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

        wpm = new WopperModel();
        // load(files.get(0));
        // wpm.load(new File("tests/drachenkurve.txt"));

        wpe = new WopperEngine(wpm);
        // for (int i = 0; i < wpm.getRecursionDepth(); i++)
        // wpe.calculateNext();
        // cmdList = wpe.normalize();
        // System.out.println("=> " + l);

        canvas = new TurtleCanvas(new WopperTurtleModel() {
            @Override
            public double getDegrees() {
                return wpm.getAngle();
            }

            @Override
            public int getNumberOfCommands() {
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

        // Button button = new Button("Next");
        // button.addActionListener(new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent arg0) {
        // wpe.calculateNext();
        // cmdList = wpe.normalize();
        // canvas.repaint();
        // }
        // });
        // f.add(button, BorderLayout.SOUTH);

        choice = new Choice();
        for (File file : files)
            choice.add(file.getPath());
        choice.addItemListener(e -> {
            System.out.println("Selected " + e);
            String filename = (String) e.getItem();
            File f1 = new File(filename);
            load(f1);
        });
        Panel pn = new Panel(new FlowLayout());

        Button btnPreviousFile = new Button("<");
        btnPreviousFile.addActionListener(e -> {
            int index = files.indexOf(actualFile) - 1;
            if (index < 0)
                index = files.size() - 1;
            load(files.get(index));
        });

        Button btnNextFile = new Button(">");
        btnNextFile.addActionListener(e -> {
            int index = files.indexOf(actualFile) + 1;
            if (index >= files.size())
                index = 0;
            load(files.get(index));
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
        refresh.addActionListener(ae -> load(textArea.getText()));
        east.add(refresh, BorderLayout.SOUTH);
        f.add(east, BorderLayout.EAST);

        f.pack();
        f.setVisible(true);

        load(files.get(0));
    }

    private static List<File> listFiles() {
        List<File> files = new ArrayList<>();
        final File dir = new File("tests");
        File[] listFiles = dir.listFiles();
        if (listFiles != null) {
            for (File f2 : listFiles) {
                if (f2.isDirectory())
                    continue;
                files.add(f2);
            }
        }

        // String[] list = dir.list();
        // for (String filename : list) {
        // File f2 = new File(dir, filename);
        // if (f2.isDirectory())
        // continue;
        // else
        // files.add(f2);
        // }
        return files;
    }

    private static void load(File f) {
        System.err.println("Loading " + f);
        try {
            BufferedReader in = new BufferedReader(new FileReader(f));
            load(in);

            actualFile = f;
            if (choice != null)
                choice.select(files.indexOf(f));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static void load(String text) {
        System.err.println("Reloading from text..");
        try {
            BufferedReader in = new BufferedReader(new StringReader(text));
            load(in);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static void load(BufferedReader in) throws IOException {
        wpm = new WopperModel();
        wpm.load(in);

        textArea.setText(wpm.getLSystemAsString());
        wpe = new WopperEngine(wpm);

        for (int i = 0; i < wpm.getRecursionDepth(); i++)
            wpe.calculateNext();

        // wpe.calculateNext();
        cmdList = wpe.normalize();

        if (canvas != null)
            canvas.repaint();

    }

}
