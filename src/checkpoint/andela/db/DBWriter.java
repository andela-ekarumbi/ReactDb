package checkpoint.andela.db;


import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.utility.Utility;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DBWriter implements Runnable {
    private ScheduledWrite scheduledWrite;

    private Timer timer;

    public DBWriter(Buffer<DbRecord> buffer,
                    MyDbWriter writer,
                    Buffer<String> logBuffer) {
        scheduledWrite = new ScheduledWrite(buffer, writer, logBuffer);
    }

    @Override
    public void run() {
        timer = new Timer();
        timer.schedule(scheduledWrite, 0, 10);
    }



    private class ScheduledWrite extends TimerTask {
        private Buffer<DbRecord> dbRecordBuffer;
        private Buffer<String> logBuffer;

        private MyDbWriter dbWriter;

        private String bufferTrackingKey;

        public ScheduledWrite(Buffer<DbRecord> buffer,
                              MyDbWriter writer,
                              Buffer<String> logBuffer) {
            this.dbRecordBuffer = buffer;
            this.dbWriter = writer;
            this.logBuffer = logBuffer;

            bufferTrackingKey = dbRecordBuffer.registerClientForTracking();
        }

        @Override
        public void run() {
            startWorking();
        }

        private void startWorking() {
            try {
                getRecordsFromBuffer();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        private void getRecordsFromBuffer() {
            if (dbRecordBuffer.isThereNewData(bufferTrackingKey)) {
                List<DbRecord> latestRecords
                        = dbRecordBuffer.getLatestData(bufferTrackingKey);
                writeRecordsToDatabase(latestRecords);
            }
        }

        private void writeRecordsToDatabase(List<DbRecord> latestRecords) {
            dbWriter.addRecords(latestRecords);
            for (DbRecord record : latestRecords) {
                writeLog(record);
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
