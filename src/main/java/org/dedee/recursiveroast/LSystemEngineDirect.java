package org.dedee.recursiveroast;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * L-System engine using DirectByteBuffer for command storage.
 * Optimized for extreme use-cases with > 100K commands (reduced GC pressure).
 */
public class LSystemEngineDirect extends AbstractLSystemEngine {
    private static final Logger logger = LoggerFactory.getLogger(LSystemEngineDirect.class);

    private CmdListDirect value;

    public LSystemEngineDirect(@NotNull LSystemModel model) {
        super(model);
        reset();
    }

    @Override
    protected void reset() {
        recursionLevel = 0;
        if (model.getRecursionData(0) != null)
            value = new CmdListDirect(model.getRecursionData(0).get(0).getWith());
    }

    @Override
    protected void applyReplacements(List<CmdReplacement> replacements) {
        CmdListDirect oldValue = value;
        value = value.replace(replacements);
        // Clean up old direct buffer
        if (oldValue != null) {
            oldValue.dispose();
        }
    }

    @Override
    @NotNull
    public NormalizedResult normalize() {
        if (value == null) {
            // Return empty result
            return new CmdListDirectResult(new CmdListDirect(0));
        }

        // Pre-calculate better capacity estimate to avoid reallocations
        int estimatedSize = (int) (value.length() * 1.2);
        CmdListDirect newList = new CmdListDirect(estimatedSize);

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
        return new CmdListDirectResult(newList);
    }

    /**
     * Wrapper for CmdListDirect to implement NormalizedResult interface.
     */
    private static class CmdListDirectResult implements NormalizedResult {
        private final CmdListDirect cmdList;

        CmdListDirectResult(CmdListDirect cmdList) {
            this.cmdList = cmdList;
        }

        @Override
        public int length() {
            return cmdList.length();
        }

        @Override
        public int get(int index) {
            return cmdList.get(index);
        }

        @Override
        public String toString(Commands commands) {
            return cmdList.toString(commands);
        }
    }

}
