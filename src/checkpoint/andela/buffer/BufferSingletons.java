/**
 * This class provides two buffer objects as singletons, to simplify access to
 * them throughout the entire application.
 */

package checkpoint.andela.buffer;

import checkpoint.andela.models.Reaction;

public class BufferSingletons {

    private static Buffer<String> stringLogBuffer;

    private static Buffer<Reaction> reactionBuffer;

    /**
     * Returns a Buffer of String objects.
     * @return {@code Buffer<String>}
     * */

    public static Buffer<String> getStringLogBuffer() {
        if (stringLogBuffer == null) {
            stringLogBuffer = new SynchronizedBuffer<>();
        }
        return stringLogBuffer;
    }

    /**
     * Returns a Buffer of DbEntry objects.
     * @return {@code Buffer<DbEntry>}
     * */

    public static Buffer<Reaction> getReactionBuffer() {
        if (reactionBuffer == null) {
            reactionBuffer = new SynchronizedBuffer<>();
        }
        return reactionBuffer;
    }
}
