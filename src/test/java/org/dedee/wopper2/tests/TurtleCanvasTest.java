package org.dedee.wopper2.tests;

import org.dedee.wopper2.Cmd;
import org.dedee.wopper2.Commands;
import org.dedee.wopper2.ui.TurtleCanvas;
import org.dedee.wopper2.ui.WopperTurtleModel;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TurtleCanvasTest {
    public static void main(String[] args) throws Exception {
        Frame f = new Frame("TurtleCanvas");
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        final Cmd[] cmds = new Cmd[7];
        cmds[0] = new Cmd(Commands.ID_FWD_PAINT);
        cmds[1] = new Cmd(Commands.ID_TURN_RIGHT);
        cmds[2] = new Cmd(Commands.ID_FWD_PAINT);
        cmds[3] = new Cmd(Commands.ID_TURN_RIGHT);
        cmds[4] = new Cmd(Commands.ID_FWD_PAINT);
        cmds[5] = new Cmd(Commands.ID_TURN_RIGHT);
        cmds[6] = new Cmd(Commands.ID_FWD_PAINT);

        TurtleCanvas canvas = new TurtleCanvas(new WopperTurtleModel() {
            @Override
            public double getDegrees() {
                return 30;
            }

            @Override
            public int getNumberOfCommands() {
                return cmds.length;
            }

            @Override
            public int getCommand(int index) {
                return cmds[index].getId();
            }

            @Override
            public String getLSystemAsString() {
                return "";
            }

            @Override
            public int getRecursionLevel() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public double getInitialAngle() {
                // TODO Auto-generated method stub
                return 0;
            }

        });

        f.add(canvas, BorderLayout.CENTER);
        f.setSize(200, 200);
        f.setVisible(true);
    }
}
