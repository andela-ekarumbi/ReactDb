/**
 * This class gets records from a temporary records buffer and writes them to a
 * database, while writing log messages to a temporary log buffer.
 */

package checkpoint.andela.db;


import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.models.Reaction;
import checkpoint.andela.utility.Utility;

import java.util.List;

import java.util.Timer;
import java.util.TimerTask;

public class DBWriter implements Runnable {

    private ScheduledWrite scheduledWrite;

    private String bufferTrackingKey;

    /**
     * Creates a new {@code DBWriter}.
     * @param reactionBuffer the reactionBuffer containing the records to be written to the
     * database.
     * @param writer the object containing the logic for persisting data to
     * the database.
     * @param logBuffer the reactionBuffer containing log messages.
     * */

    public DBWriter(Buffer<Reaction> reactionBuffer,
                    DbHelper writer,
                    Buffer<String> logBuffer) {

        scheduledWrite = new ScheduledWrite(reactionBuffer, writer, logBuffer);

        bufferTrackingKey = reactionBuffer.registerClientForTracking();
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
        private Buffer<Reaction> reactionBuffer;
        private Buffer<String> logBuffer;

        private DbHelper dbHelper;

        List<Reaction> latestReactions;

        public ScheduledWrite(Buffer<Reaction> reactionBuffer,
                              DbHelper dbHelper,
                              Buffer<String> logBuffer) {
            this.reactionBuffer = reactionBuffer;
            this.dbHelper = dbHelper;
            this.logBuffer = logBuffer;
        }

        @Override
        public void run() {
            startWritingData();
        }

        private void startWritingData() {
            try {
                getReactionsFromBuffer();
                if (latestReactions != null && latestReactions.size() > 0) {
                    writeReactionsToDatabase();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        private void getReactionsFromBuffer() {
            if (reactionBuffer.isThereNewData(bufferTrackingKey)) {
                latestReactions = reactionBuffer.getLatestData(bufferTrackingKey);
            }
        }

        private void writeReactionsToDatabase() {
            if (latestReactions != null) {
                dbHelper.writeReactions(latestReactions);
                for (Reaction reaction : latestReactions) {
                    writeLog(reaction);
                }
            }
        }

        private void writeLog(Reaction reaction) {
            String messageTermination = " from buffer and wrote to database.";
            String logMessage
                    = Utility.generateLogMessage(reaction, messageTermination,
                    "DBWriter Thread");
            logBuffer.addToBuffer(logMessage);
        }
    }
}
