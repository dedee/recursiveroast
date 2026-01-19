package org.dedee.recursiveroast;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for L-System computation engines.
 * Defines the contract for computing L-System iterations and normalizing results.
 */
public interface ILSystemEngine {

    /**
     * Calculates the next iteration of the L-System.
     */
    void calculateNext();

    /**
     * Calculates the previous iteration by resetting and recalculating.
     */
    void calculatePrevious();

    /**
     * Normalizes the current command list by expanding user-defined commands.
     *
     * @return the normalized command list with length information
     */
    @NotNull
    NormalizedResult normalize();

    /**
     * Gets the current recursion level.
     *
     * @return the recursion level (0-based)
     */
    int getRecursionLevel();

    /**
     * Result of normalization containing the command list and its length.
     */
    interface NormalizedResult {
        /**
         * Gets the length of the normalized command list.
         *
         * @return number of commands
         */
        int length();

        /**
         * Gets a command at the specified index.
         *
         * @param index the index
         * @return the command ID
         */
        int get(int index);

        /**
         * Converts the command list to a string representation.
         *
         * @param commands the command registry for name mapping
         * @return string representation
         */
        String toString(Commands commands);
    }
}
