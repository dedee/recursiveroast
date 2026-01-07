package org.dedee.wopper3;

import java.util.HashMap;

/**
 * <pre>
 *  F                Vorwaerts mit Zeichnen
 * 	f                Vorwaerts ohne Zeichnen
 * 	+                Linksdrehung (in math. pos. Richtung)
 * 	-                Rechtsdrehung (in math. neg. Richtung)
 *  |                180 Turn around
 * 	[                Push die Schildkroete
 * 	]                Pop die Schildkroete
 * 	w(aa)            Drehwinkelaenderung
 * 	+(xx,aa,bb)      Um zufaelligen Winkel aa oder bb drehen, xx=WS [%]
 * 	-(xx,aa,bb)      Um zufaelligen Winkel aa oder bb drehen, xx=WS [%]
 * 	x(xx,mm,nn)      Zufaellig zwischen zwei Befehlsstrings auswaehlen
 * </pre>
 */
public class Commands {

    public final static int ID_BASE = 0;
    public final static int ID_FWD_PAINT = 1;
    public final static int ID_FWD_MOVE = 2;
    public final static int ID_TURN_LEFT = 3;
    public final static int ID_TURN_RIGHT = 4;
    public final static int ID_PUSH = 5;
    public final static int ID_POP = 6;
    public final static int ID_TURNAROUND = 7;
    public final static int ID_NOP = 8;
    public final static int ID_USERDEFINED_MIN = 9;
    public final static int ID_USERDEFINED_MAX = 15;

    private static Commands instance = new Commands();

    private final Cmd[] commands;
    private final HashMap<Character, Cmd> nameMap;
    private final HashMap<Integer, Character> nameMapReverse;
    private int numberOfUserDefinedCommands;

    private Commands() {
        commands = new Cmd[ID_USERDEFINED_MAX + 1];

        commands[ID_BASE] = new Cmd(ID_BASE);
        commands[ID_FWD_PAINT] = new Cmd(ID_FWD_PAINT);
        commands[ID_FWD_MOVE] = new Cmd(ID_FWD_MOVE);
        commands[ID_TURN_LEFT] = new Cmd(ID_TURN_LEFT);
        commands[ID_TURN_RIGHT] = new Cmd(ID_TURN_RIGHT);
        commands[ID_PUSH] = new Cmd(ID_PUSH);
        commands[ID_POP] = new Cmd(ID_POP);
        commands[ID_TURNAROUND] = new Cmd(ID_TURNAROUND);
        commands[ID_NOP] = new Cmd(ID_NOP);

        nameMap = new HashMap<>();
        nameMap.put('B', commands[ID_BASE]);
        nameMap.put('F', commands[ID_FWD_PAINT]);
        nameMap.put('f', commands[ID_FWD_MOVE]);
        nameMap.put('-', commands[ID_TURN_LEFT]);
        nameMap.put('+', commands[ID_TURN_RIGHT]);
        nameMap.put('[', commands[ID_PUSH]);
        nameMap.put(']', commands[ID_POP]);
        nameMap.put('|', commands[ID_TURNAROUND]);
        nameMap.put('.', commands[ID_NOP]);

        nameMapReverse = new HashMap<>();
        nameMapReverse.put(ID_BASE, 'B');
        nameMapReverse.put(ID_FWD_PAINT, 'F');
        nameMapReverse.put(ID_FWD_MOVE, 'f');
        nameMapReverse.put(ID_TURN_LEFT, '-');
        nameMapReverse.put(ID_TURN_RIGHT, '+');
        nameMapReverse.put(ID_PUSH, '[');
        nameMapReverse.put(ID_POP, ']');
        nameMapReverse.put(ID_TURNAROUND, '|');
        nameMapReverse.put(ID_NOP, '.');
    }

    public static Commands getInstance() {
        return instance;
    }

    public void reset() {
        // ugly ugly stuff ;)
        instance = new Commands();
    }

    public Cmd createUserDefinedCommand(char name, String value) {
        value = value.trim();
        int[] subcmds = new int[value.length()];
        for (int i = 0; i < value.length(); i++) {
            subcmds[i] = get(value.charAt(i)).getId();
        }

        int idx = ID_USERDEFINED_MIN + numberOfUserDefinedCommands;
        commands[idx] = new Cmd(idx, subcmds);

        nameMap.put(name, commands[idx]);
        nameMapReverse.put(idx, name);
        numberOfUserDefinedCommands++;
        return commands[idx];
    }

    public Cmd get(int id) {
        return commands[Cmd.getId(id)];
    }

    public Cmd get(char name) {
        return nameMap.get(name);
    }

    public Character idToChar(int id) {
        return nameMapReverse.get(Cmd.getId(id));
    }

}
