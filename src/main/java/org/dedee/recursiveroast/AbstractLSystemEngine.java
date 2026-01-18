package org.dedee.recursiveroast;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Abstract base class for L-System engines.
 * Implements common logic shared between different engine implementations.
 */
public abstract class AbstractLSystemEngine implements ILSystemEngine {
    private static final Logger logger = LoggerFactory.getLogger(AbstractLSystemEngine.class);

    protected final LSystemModel model;
    protected int recursionLevel;

    protected AbstractLSystemEngine(@NotNull LSystemModel model) {
        this.model = model;
        this.recursionLevel = 0;
    }

    @Override
    public void calculateNext() {
        recursionLevel++;

        List<CmdReplacement> recursionData = model.getRecursionData(recursionLevel);

        if (recursionData == null) {
            // If no rule defined for this level, use last one
            int i = recursionLevel - 1;
            while (recursionData == null && i >= 0) {
                recursionData = model.getRecursionData(i--);
            }
        }

        applyReplacements(recursionData);
        logger.debug("Free memory after recursion: {} kB", Runtime.getRuntime().freeMemory() / 1000);
    }

    @Override
    public void calculatePrevious() {
        int level = recursionLevel;
        reset();
        for (int i = 0; i < level - 1; i++) {
            calculateNext();
        }
    }

    @Override
    public int getRecursionLevel() {
        return recursionLevel;
    }

    /**
     * Resets the engine to initial state.
     */
    protected abstract void reset();

    /**
     * Applies the replacement rules to the current command list.
     *
     * @param replacements the replacement rules to apply
     */
    protected abstract void applyReplacements(List<CmdReplacement> replacements);
}
