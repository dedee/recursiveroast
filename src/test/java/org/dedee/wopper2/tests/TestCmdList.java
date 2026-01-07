package org.dedee.wopper2.tests;

import org.dedee.wopper2.CmdList;
import org.dedee.wopper2.CmdReplacement;
import org.dedee.wopper2.Commands;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCmdList {

    @Test
    public void testAddOneInt() {
        CmdList cmdList = new CmdList(1);
        int MAX = 1000000;
        for (int i = 0; i < MAX; i++)
            cmdList.append(i);

        for (int i = 0; i < MAX; i++)
            assertEquals(i, cmdList.get(i));
    }

    @Test
    public void testAddIntArrays() {

        int[] foo = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        CmdList cmdList = new CmdList(1);
        int MAX = 100000;

        for (int i = 0; i < MAX; i += 10) {
            cmdList.append(foo);
        }

        for (int i = 0; i < MAX; i += 10) {
            for (int k = 0; k < 10; k++)
                assertEquals(k, cmdList.get(i + k));
        }
    }

    @Test
    public void testReplace() {
        Commands.getInstance().createUserDefinedCommand('R', "F");

        CmdList cmdList = new CmdList(1);
        cmdList.append(Commands.ID_FWD_MOVE);
        cmdList.append(Commands.ID_FWD_MOVE);
        cmdList.append(Commands.ID_TURN_LEFT);
        cmdList.append(Commands.ID_USERDEFINED_MIN);
        cmdList.append(Commands.ID_FWD_MOVE);
        cmdList.append(Commands.ID_TURN_LEFT);

        List<CmdReplacement> replacements = new ArrayList<>();
        replacements.add(new CmdReplacement(Commands.ID_USERDEFINED_MIN,
                new int[]{Commands.ID_FWD_PAINT, Commands.ID_TURN_LEFT,}));

        cmdList = cmdList.replace(replacements);

        assertEquals(Commands.ID_FWD_MOVE, cmdList.get(0));
        assertEquals(Commands.ID_FWD_MOVE, cmdList.get(1));
        assertEquals(Commands.ID_TURN_LEFT, cmdList.get(2));
        assertEquals(Commands.ID_FWD_PAINT, cmdList.get(3));
        assertEquals(Commands.ID_TURN_LEFT, cmdList.get(4));
        assertEquals(Commands.ID_FWD_MOVE, cmdList.get(5));
        assertEquals(Commands.ID_TURN_LEFT, cmdList.get(6));

        cmdList.replace(replacements);
    }
}
