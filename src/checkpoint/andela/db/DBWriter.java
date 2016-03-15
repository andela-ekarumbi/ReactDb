/**
 * This class gets records from a temporary records buffer and writes them to a
 * database, while writing log messages to a temporary log buffer.
 */

package checkpoint.andela.db;


import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.utility.Utility;

import java.util.List;

import java.util.Timer;
import java.util.TimerTask;

public class DBWriter implements Runnable {

    private ScheduledWrite scheduledWrite;

    private String bufferTrackingKey;

    /**
     * Creates a new {@code DBWriter}.
     * @param buffer the buffer containing the records to be written to the
     * database.
     * @param writer the object containing the logic for persisting data to
     * the database.
     * @param logBuffer the buffer containing log messages.
     * */

    public DBWriter(Buffer<DbRecord> buffer,
                    MyDbWriter writer,
                    Buffer<String> logBuffer) {

        scheduledWrite = new ScheduledWrite(buffer, writer, logBuffer);

        bufferTrackingKey = buffer.registerClientForTracking();
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        timer.schedule(scheduledWrite, 0, 10);
    }

    /**
     * This private class packages the logic for class DBWriter as an operation
     * that may be called repeatedly at specified time intervals.
     * */

    private class ScheduledWrite extends TimerTask {
        private Buffer<DbRecord> dbRecordBuffer;
        private Buffer<String> logBuffer;

        private MyDbWriter dbWriter;

        List<DbRecord> latestRecords;

        public ScheduledWrite(Buffer<DbRecord> buffer,
                              MyDbWriter writer,
                              Buffer<String> logBuffer) {
            this.dbRecordBuffer = buffer;
            this.dbWriter = writer;
            this.logBuffer = logBuffer;
        }

        @Override
        public void run() {
            startWritingData();
        }

        private void startWritingData() {
            try {
                getRecordsFromBuffer();
                writeRecordsToDatabase();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        private void getRecordsFromBuffer() {
            if (dbRecordBuffer.isThereNewData(bufferTrackingKey)) {
                latestRecords= dbRecordBuffer.getLatestData(bufferTrackingKey);
            }
        }

        private void writeRecordsToDatabase() {
            if (latestRecords != null) {
                dbWriter.writeRecords(latestRecords);
                for (DbRecord record : latestRecords) {
                    writeLog(record);
                }
            }
        }

        private void writeLog(DbRecord record) {
            String messageTermination = " from buffer and wrote to database.";
            String logMessage
                    = Utility.generateLogMessage(record, messageTermination,
                    "DBWriter Thread");
            logBuffer.addToBuffer(logMessage);
        }
    }
}
