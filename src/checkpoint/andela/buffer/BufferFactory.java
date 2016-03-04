package checkpoint.andela.buffer;

import checkpoint.andela.utility.DbRecord;

public class BufferFactory {

    private static Buffer<String> stringLogBuffer;

    private static Buffer<DbRecord> dbRecordBuffer;

    public static Buffer<String> getStringLogBuffer() {
        if (stringLogBuffer == null) {
            stringLogBuffer = new Buffer<>();
        }
        return stringLogBuffer;
    }

    public static Buffer<DbRecord> getDbRecordBuffer() {
        if (dbRecordBuffer == null) {
            dbRecordBuffer = new Buffer<>();
        }
        return dbRecordBuffer;
    }
}
