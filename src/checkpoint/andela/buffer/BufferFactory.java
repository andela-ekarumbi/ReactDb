package checkpoint.andela.buffer;

import checkpoint.andela.db.DbRecord;

public class BufferFactory {

    private static SynchronizedBuffer<String> stringLogBuffer;

    private static SynchronizedBuffer<DbRecord> dbRecordBuffer;

    public static SynchronizedBuffer<String> getStringLogBuffer() {
        if (stringLogBuffer == null) {
            stringLogBuffer = new SynchronizedBuffer<>();
        }
        return stringLogBuffer;
    }

    public static SynchronizedBuffer<DbRecord> getDbRecordBuffer() {
        if (dbRecordBuffer == null) {
            dbRecordBuffer = new SynchronizedBuffer<>();
        }
        return dbRecordBuffer;
    }
}
