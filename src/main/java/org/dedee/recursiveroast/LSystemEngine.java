package org.dedee.recursiveroast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Engine for computing L-System iterations.
 * Takes an L-System model and generates command sequences at different recursion levels.
 */
public class LSystemEngine {
    private static final Logger logger = LoggerFactory.getLogger(LSystemEngine.class);

    private final LSystemModel model;
    private CmdList value;
    private int recursionLevel;

    public LSystemEngine(@NotNull LSystemModel model) {
        this.model = model;
        reset();
    }

    private void reset() {
        recursionLevel = 0;
        if (model.getRecursionData(0) != null)
            value = new CmdList(model.getRecursionData(0).get(0).getWith());
    }

    @Nullable
    public CmdList normalize() {
        if (value == null)
            return null;

        // Pre-calculate better capacity estimate to avoid reallocations
        int estimatedSize = (int) (value.length() * 1.2);
        CmdList newList = new CmdList(estimatedSize);

        for (int i = 0; i < value.length(); i++) {
            int cmd = value.get(i);
            int id = Cmd.getId(cmd);
            if (id >= Commands.ID_USERDEFINED_MIN
                    && id <= Commands.ID_USERDEFINED_MAX) {
                int[] values = model.getCommands().get(id).getValues();
                newList.append(values);
            } else {
                newList.append(cmd);
            }
        }

        logger.debug("Normalized command list: {}", newList);
        return newList;
    }

    public void calculateNext() {
        if (value == null)
            return;

        recursionLevel++;

        List<CmdReplacement> recursionData = model
                .getRecursionData(recursionLevel);

        if (recursionData == null) {
            // If no rule defined for this level, use last one
            int i = recursionLevel - 1;
            while (recursionData == null && i >= 0) {
                recursionData = model.getRecursionData(i--);
            }
        }

        value = value.replace(recursionData);
        logger.debug("Free memory after recursion: {} kB", Runtime.getRuntime().freeMemory() / 1000);
    }

    public void calculatePrevious() {
        int level = recursionLevel;
        reset();
        for (int i = 0; i < level - 1; i++)
            calculateNext();
    }

    public int getRecursionLevel() {
        return recursionLevel;
    }

}
