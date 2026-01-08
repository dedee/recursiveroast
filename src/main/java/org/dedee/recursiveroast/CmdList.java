package org.dedee.recursiveroast;

import java.util.List;

public class CmdList {

    private static final float REALLOC_PERCENTAGE = 0.4f;
    private static final int INITIAL_SIZE = 3000;
    int[] cmdIds;
    int length;

    public CmdList(int initialLength) {
        cmdIds = new int[initialLength];
        length = 0;
    }

    public CmdList(int[] cmdIds) {
        this.cmdIds = cmdIds;
        this.length = cmdIds.length;
    }

    private void ensure(int cnt) {
        if (cnt + length > cmdIds.length) {
            int[] newCmdIds = new int[(int) (cmdIds.length + cnt + cmdIds.length
                    * REALLOC_PERCENTAGE)];
            System.arraycopy(cmdIds, 0, newCmdIds, 0, length);
            cmdIds = newCmdIds;
        }
    }

    public void append(int id) {
        ensure(1);
        cmdIds[length++] = id;
    }

    public void append(int[] ids) {
        ensure(ids.length);
        System.arraycopy(ids, 0, cmdIds, length, ids.length);
        length += ids.length;
    }

    public CmdList replace(List<CmdReplacement> replacements) {
        CmdList newList = new CmdList(INITIAL_SIZE);

        for (int i = 0; i < length; i++) {

            int k = Cmd.getId(cmdIds[i]);
            if (k >= Commands.ID_USERDEFINED_MIN
                    && k <= Commands.ID_USERDEFINED_MAX) {

                for (CmdReplacement rep : replacements) {
                    if (rep.what == k) {
                        newList.append(rep.with);
                        break;
                    }
                }
            } else {
                int cmd = cmdIds[i];
                newList.append(cmd);
            }
        }

        // System.out.println(this + "----> " + newList);

        return newList;
    }

    public int get(int i) {
        return cmdIds[i];
    }

    public int length() {
        return length;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(Commands.getInstance().idToChar(Cmd.getId(cmdIds[i])));
        }
        return sb.toString();
    }

}
