package org.dedee.recursiveroast;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a command replacement rule in an L-System.
 * Maps a command ID to an array of replacement command IDs.
 */
public class CmdReplacement {
    private final int what;
    @Nullable
    private final int[] with;
    
    /**
     * Creates a new command replacement rule.
     * Note: The array is stored directly without copying for performance.
     *
     * @param what the command ID to replace
     * @param with the array of replacement command IDs
     */
    public CmdReplacement(int what, @Nullable int[] with) {
        this.what = what;
        this.with = with;
    }
    
    public int getWhat() {
        return what;
    }
    
    /**
     * Returns the replacement values array.
     * Note: The returned array should not be modified by callers.
     *
     * @return the replacement values, or null if not set
     */
    @Nullable
    public int[] getWith() {
        return with;
    }
}
