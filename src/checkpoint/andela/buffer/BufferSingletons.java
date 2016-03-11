/**
 * This class provides two buffer objects as singletons, to simplify access to
 * them throughout the entire application.
 * */

package checkpoint.andela.buffer;

import checkpoint.andela.db.DbRecord;

public class BufferSingletons {

    private static SynchronizedBuffer<String> stringLogBuffer;

    private static SynchronizedBuffer<DbRecord> dbRecordBuffer;

    /**
     * Returns a Buffer of String objects.
     * @return {@code Buffer<String>}
     * */

    public static SynchronizedBuffer<String> getStringLogBuffer() {
        if (stringLogBuffer == null) {
            stringLogBuffer = new SynchronizedBuffer<>();
        }
        return stringLogBuffer;
    }

    /**
     * Returns a Buffer of DbEntry objects.
     * @return {@code Buffer<DbEntry>}
     * */

    public static SynchronizedBuffer<DbRecord> getDbRecordBuffer() {
        if (dbRecordBuffer == null) {
            dbRecordBuffer = new SynchronizedBuffer<>();
        }
        return dbRecordBuffer;
    }
}
