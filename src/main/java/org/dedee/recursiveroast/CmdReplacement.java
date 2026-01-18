package org.dedee.recursiveroast;

/**
 * Represents a command replacement rule in an L-System.
 * Maps a command ID to an array of replacement command IDs.
 */
public class CmdReplacement {
    private final int what;
    private final int[] with;
    
    public CmdReplacement(int what, int[] with) {
        this.what = what;
        this.with = with;
    }
    
    public int getWhat() {
        return what;
    }
    
    public int[] getWith() {
        return with;
    }
}
