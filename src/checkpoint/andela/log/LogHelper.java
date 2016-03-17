/**
 * This class fetches log messages from a log buffer and writes them to a file.
 */

package checkpoint.andela.log;

import checkpoint.andela.buffer.Buffer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LogHelper {

    private Buffer<String> logBuffer;

    private BufferedWriter writer;

    private String logFilePath;

    private String bufferTrackingKey;

    /**
     * Creates a new {@code LogWriter}
     * @param logBuffer a String buffer containing the log messages.
     * @param logFilePath the path of the log file to be written to.
     */

    public LogHelper(Buffer<String> logBuffer, String logFilePath) {
        this.logBuffer = logBuffer;
        this.logFilePath = logFilePath;
        bufferTrackingKey = logBuffer.registerClientForTracking();
    }

    public void startLogging() {
        initializeWriter();

        if (logBuffer.isThereNewData(bufferTrackingKey)) {
            List<String> latestLogs
                    = logBuffer.getLatestData(bufferTrackingKey);
            for (String log : latestLogs) {
                writeLogEntry(log);
            }
        }

        finalizeWriter();
    }

    private void writeLogEntry(String log) {
        try {
            log = "\n" + log;
            writer.write(log);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void initializeWriter() {
        try {
            File file = new File(logFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file, true);
            writer = new BufferedWriter(fileWriter);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void finalizeWriter() {
        try {
            writer.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
