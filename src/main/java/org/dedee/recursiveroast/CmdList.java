package org.dedee.recursiveroast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A dynamic array of command IDs with efficient replacement operations.
 * Used to store and manipulate L-System command sequences.
 */
public class CmdList {
    private static final Logger logger = LoggerFactory.getLogger(CmdList.class);

    private static final float REALLOC_PERCENTAGE = 0.4f;
    private static final int INITIAL_SIZE = 3000;
    private int[] cmdIds;
    private int length;

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
        // Build a HashMap for O(1) lookup instead of O(m) linear search
        Map<Integer, int[]> replacementMap = new HashMap<>(replacements.size());
        int totalReplacementSize = 0;

        for (CmdReplacement rep : replacements) {
            replacementMap.put(rep.getWhat(), rep.getWith());
            if (rep.getWith() != null) {
                totalReplacementSize += rep.getWith().length;
            }
        }

        // Estimate new size: assume average expansion factor
        int estimatedSize = Math.max(INITIAL_SIZE, (int) (length * 1.5 + totalReplacementSize));
        CmdList newList = new CmdList(estimatedSize);

        for (int i = 0; i < length; i++) {
            int k = Cmd.getId(cmdIds[i]);

            if (k >= Commands.ID_USERDEFINED_MIN && k <= Commands.ID_USERDEFINED_MAX) {
                int[] replacement = replacementMap.get(k);
                if (replacement != null) {
                    newList.append(replacement);
                } else {
                    // No replacement found, keep original command
                    newList.append(cmdIds[i]);
                }
            } else {
                newList.append(cmdIds[i]);
            }
        }

        logger.debug("Replaced: {} -> {}", this, newList);

        return newList;
    }

    public int get(int i) {
        return cmdIds[i];
    }

    public int length() {
        return length;
    }

    public String toString(Commands commands) {
        StringBuilder sb = new StringBuilder(length);  // Pre-allocate capacity
        for (int i = 0; i < length; i++) {
            sb.append(commands.idToChar(Cmd.getId(cmdIds[i])));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        // For backward compatibility when commands instance is not available
        return "CmdList[length=" + length + "]";
    }

}
