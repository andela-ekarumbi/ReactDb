/**
 * This class gets records from a temporary records buffer and writes them to a
 * database, while writing log messages to a temporary log buffer.
 */

package checkpoint.andela.db;


import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.models.Reaction;
import checkpoint.andela.utility.Utility;

import java.util.List;

public class DBWriter implements Runnable {

    private String bufferTrackingKey;

    private Buffer<Reaction> reactionBuffer;

    private Buffer<String> logBuffer;

    private DbHelper dbHelper;

    List<Reaction> latestReactions;

    /**
     * Creates a new {@code DBWriter}.
     *
     * @param reactionBuffer the reactionBuffer containing the records to be written to the
     * database.
     * @param dbHelper the object containing the logic for persisting data to
     * the database.
     * @param logBuffer the reactionBuffer containing log messages.
     */

    public DBWriter(Buffer<Reaction> reactionBuffer,
                    DbHelper dbHelper,
                    Buffer<String> logBuffer) {

        this.reactionBuffer = reactionBuffer;
        this.dbHelper = dbHelper;
        this.logBuffer = logBuffer;

        bufferTrackingKey = reactionBuffer.registerClientForTracking();
    }

    @Override
    public void run() {
        startWritingData();
    }

    private void startWritingData() {
        if (reactionBuffer.isThereNewData(bufferTrackingKey)) {
            try {
                getReactionsFromBuffer();
                if (latestReactions != null && latestReactions.size() > 0) {
                    writeReactionsToDatabase();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void getReactionsFromBuffer() {
        latestReactions = reactionBuffer.getLatestData(bufferTrackingKey);
    }

    private void writeReactionsToDatabase() {
        if (latestReactions != null) {
            for (Reaction reaction : latestReactions) {
                writeLog(reaction);
            }
            dbHelper.writeReactions(latestReactions);
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
