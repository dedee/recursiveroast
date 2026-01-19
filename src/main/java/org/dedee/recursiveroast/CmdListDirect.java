package org.dedee.recursiveroast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Alternative CmdList implementation using DirectByteBuffer for better performance
 * with large command sequences (>100K commands).
 *
 * Uses off-heap memory to reduce GC pressure for deep recursions.
 *
 * Trade-offs:
 * - Better performance for large datasets
 * - Lower GC overhead
 * - More complex memory management
 *
 * Benchmark to decide between this and regular CmdList!
 */
public class CmdListDirect {
    private static final Logger logger = LoggerFactory.getLogger(CmdListDirect.class);

    private static final float REALLOC_PERCENTAGE = 0.4f;
    private static final int INITIAL_SIZE = 3000;

    private ByteBuffer cmdIds;  // Direct buffer (off-heap)
    private int length;

    /**
     * Creates a new CmdList with direct (off-heap) memory allocation.
     * Better for large datasets (>100K commands) as it reduces GC pressure.
     */
    public CmdListDirect(int initialLength) {
        cmdIds = ByteBuffer.allocateDirect(initialLength);
        length = 0;
    }

    /**
     * Creates a CmdList from an array of command IDs.
     */
    public CmdListDirect(int[] cmdIds) {
        this.cmdIds = ByteBuffer.allocateDirect(cmdIds.length);
        for (int i = 0; i < cmdIds.length; i++) {
            this.cmdIds.put(i, (byte) cmdIds[i]);
        }
        this.length = cmdIds.length;
    }

    /**
     * Ensures capacity for additional commands.
     * Resizes the buffer if necessary.
     */
    private void ensure(int cnt) {
        if (cnt + length > cmdIds.capacity()) {
            int newCapacity = (int) (cmdIds.capacity() + cnt + cmdIds.capacity() * REALLOC_PERCENTAGE);
            ByteBuffer newBuffer = ByteBuffer.allocateDirect(newCapacity);

            // Copy existing data
            cmdIds.rewind();
            cmdIds.limit(length);
            newBuffer.put(cmdIds);

            cmdIds = newBuffer;
        }
    }

    /**
     * Appends a single command ID.
     */
    public void append(int id) {
        ensure(1);
        cmdIds.put(length++, (byte) id);
    }

    /**
     * Appends multiple command IDs from an array.
     * Uses bulk put for better performance.
     */
    public void append(int[] ids) {
        ensure(ids.length);

        // Convert to bytes for bulk operation
        byte[] bytes = new byte[ids.length];
        for (int i = 0; i < ids.length; i++) {
            bytes[i] = (byte) ids[i];
        }

        // Bulk put is faster than individual puts
        cmdIds.position(length);
        cmdIds.put(bytes);
        length += ids.length;
    }

    /**
     * Replaces user-defined commands according to replacement rules.
     * Uses HashMap for O(1) lookup.
     */
    public CmdListDirect replace(List<CmdReplacement> replacements) {
        // Build HashMap for O(1) lookup
        Map<Integer, int[]> replacementMap = new HashMap<>(replacements.size());
        int totalReplacementSize = 0;

        for (CmdReplacement rep : replacements) {
            replacementMap.put(rep.getWhat(), rep.getWith());
            if (rep.getWith() != null) {
                totalReplacementSize += rep.getWith().length;
            }
        }

        // Estimate new size
        int estimatedSize = Math.max(INITIAL_SIZE, (int) (length * 1.5 + totalReplacementSize));
        CmdListDirect newList = new CmdListDirect(estimatedSize);

        for (int i = 0; i < length; i++) {
            int k = Cmd.getId(cmdIds.get(i) & 0xFF);

            if (k >= Commands.ID_USERDEFINED_MIN && k <= Commands.ID_USERDEFINED_MAX) {
                int[] replacement = replacementMap.get(k);
                if (replacement != null) {
                    newList.append(replacement);
                } else {
                    newList.append(cmdIds.get(i) & 0xFF);
                }
            } else {
                newList.append(cmdIds.get(i) & 0xFF);
            }
        }

        logger.debug("Replaced: {} -> {}", this, newList);

        return newList;
    }

    /**
     * Gets the command ID at the specified index.
     */
    public int get(int i) {
        return cmdIds.get(i) & 0xFF;
    }

    /**
     * Returns the number of commands in this list.
     */
    public int length() {
        return length;
    }

    /**
     * Converts the command list to a string representation using command names.
     */
    public String toString(Commands commands) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(commands.idToChar(Cmd.getId(cmdIds.get(i) & 0xFF)));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "CmdListDirect[length=" + length + "]";
    }

    /**
     * Explicitly releases the direct buffer.
     * Optional - will be cleaned up by GC eventually, but explicit cleanup
     * can help with memory pressure.
     */
    public void dispose() {
        // DirectByteBuffer cleanup is handled by Cleaner in JDK 9+
        // For JDK 8, would need reflection to call ((DirectBuffer)cmdIds).cleaner().clean()
        cmdIds = null;
    }
}
