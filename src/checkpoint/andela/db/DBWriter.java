package checkpoint.andela.db;


import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.utility.Utility;

import java.util.List;

public class DBWriter implements Runnable {
    private Buffer<DbRecord> dbRecordBuffer;
    private Buffer<String> logBuffer;

    private MyDbWriter dbWriter;

    private String bufferTrackingKey;

    public DBWriter(Buffer<DbRecord> buffer,
                    MyDbWriter writer,
                    Buffer<String> logBuffer) {
        this.dbRecordBuffer = buffer;
        this.dbWriter = writer;
        this.logBuffer = logBuffer;

        bufferTrackingKey = buffer.registerClientForTracking();
    }

    @Override
    public void run() {
        startWorking();
    }

    private void startWorking() {
        while (!dbRecordBuffer.isInputEnded()) {
            try {
                Thread.sleep(50L);
                getRecordsFromBuffer();
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
            writeLog(record);
        }
        logBuffer.setInputEnded(true);
    }

    private void writeLog(DbRecord record) {
        String messageTermination = " from buffer and wrote to database.";
        String logMessage
                = Utility.generateLogMessage(record,messageTermination);
        logBuffer.addToBuffer(logMessage);
    }
}
