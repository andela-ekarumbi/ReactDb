package checkpoint.andela.db;


import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.utility.Utility;

import java.util.List;

public class DBWriter implements Runnable {
    private Buffer<DbRecord> dbRecordBuffer;

    private MyDbWriter dbWriter;

    private final int MAX_POLLS = 100;

    private int pollCount = 0;

    private String bufferTrackingKey;

    public DBWriter(Buffer<DbRecord> buffer, MyDbWriter writer) {
        this.dbRecordBuffer = buffer;
        this.dbWriter = writer;

        bufferTrackingKey = buffer.registerClientForTracking();
    }

    @Override
    public void run() {
        startWorking();
    }

    private void startWorking() {
        while (pollCount <= MAX_POLLS) {
            try {
                Thread.sleep(50L);
                getRecordsFromBuffer();
                pollCount++;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void getRecordsFromBuffer() {
        List<DbRecord> latestRecords
                = dbRecordBuffer.getLatestData(bufferTrackingKey);
        writeRecordsToDatabase(latestRecords);
    }

    private void writeRecordsToDatabase(List<DbRecord> latestRecords) {
        for (DbRecord record : latestRecords) {
            dbWriter.addNewDbRecord(record);
        }
    }
}
