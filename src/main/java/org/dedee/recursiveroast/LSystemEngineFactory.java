package org.dedee.recursiveroast;

import org.jetbrains.annotations.NotNull;

/**
 * Factory for creating L-System engine instances.
 * Allows choosing between different engine implementations based on requirements.
 */
public class LSystemEngineFactory {

    /**
     * Engine type enum for selecting implementation.
     */
    public enum EngineType {
        /**
         * Standard byte[] implementation.
         * Best for typical use-cases with < 100K commands.
         */
        STANDARD,

        /**
         * DirectByteBuffer implementation.
         * Best for extreme use-cases with > 100K commands (reduced GC pressure).
         */
        DIRECT
    }

    /**
     * Creates an L-System engine with the specified type.
     *
     * @param model the L-System model
     * @param type the engine type
     * @return the engine instance
     */
    @NotNull
    public static ILSystemEngine create(@NotNull LSystemModel model, @NotNull EngineType type) {
        return switch (type) {
            case STANDARD -> new LSystemEngine(model);
            case DIRECT -> new LSystemEngineDirect(model);
        };
    }

    /**
     * Creates a standard L-System engine (byte[] implementation).
     * This is the recommended default for most use-cases.
     *
     * @param model the L-System model
     * @return the engine instance
     */
    @NotNull
    public static ILSystemEngine createStandard(@NotNull LSystemModel model) {
        return new LSystemEngine(model);
    }

    /**
     * Creates a DirectByteBuffer L-System engine.
     * Use only for extreme use-cases with > 100K commands.
     *
     * @param model the L-System model
     * @return the engine instance
     */
    @NotNull
    public static ILSystemEngine createDirect(@NotNull LSystemModel model) {
        return new LSystemEngineDirect(model);
    }

    /**
     * Creates an L-System engine automatically selecting the best implementation
     * based on expected command count.
     *
     * @param model the L-System model
     * @param expectedCommandCount estimated number of commands after recursion
     * @return the engine instance
     */
    @NotNull
    public static ILSystemEngine createAuto(@NotNull LSystemModel model, int expectedCommandCount) {
        // Crossover point is around 40K commands based on benchmarks
        if (expectedCommandCount > 40_000) {
            return new LSystemEngineDirect(model);
        } else {
            return new LSystemEngine(model);
        }
    }

    /**
     * Creates an L-System engine automatically selecting the best implementation
     * based on recursion depth.
     *
     * @param model the L-System model
     * @param recursionDepth the expected recursion depth
     * @return the engine instance
     */
    @NotNull
    public static ILSystemEngine createAutoByDepth(@NotNull LSystemModel model, int recursionDepth) {
        // DirectByteBuffer becomes beneficial at depth > 13 (typically > 40K commands)
        if (recursionDepth > 13) {
            return new LSystemEngineDirect(model);
        } else {
            return new LSystemEngine(model);
        }
    }

    private LSystemEngineFactory() {
        // Utility class, no instantiation
    }
}
