package checkpoint.andela.log;

import checkpoint.andela.buffer.Buffer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LogWriter implements Runnable {

    private ScheduledLog scheduledLog;
    private Timer timer;

    public LogWriter(Buffer<String> logBuffer, String logFilePath) {
        scheduledLog = new ScheduledLog(logBuffer,logFilePath);
    }

    @Override
    public void run() {
        timer = new Timer();
        timer.schedule(scheduledLog, 0, 10);
    }

    private class ScheduledLog extends TimerTask {

        private Buffer<String> logBuffer;

        private PrintWriter writer;

        private String logFilePath;

        private String bufferTrackingKey;

        public ScheduledLog(Buffer<String> logBuffer, String logFilePath) {
            this.logBuffer = logBuffer;
            this.logFilePath = logFilePath;
            bufferTrackingKey = logBuffer.registerClientForTracking();
        }

        @Override
        public void run() {
            initializeWriter();
            startLogging();
            writer.close();
        }

        private void startLogging() {
            if (logBuffer.isThereNewData(bufferTrackingKey)) {
                List<String> latestLogs
                        = logBuffer.getLatestData(bufferTrackingKey);
                for (String log : latestLogs) {
                    writeLogEntry(log);
                }
            }
        }

        private void writeLogEntry(String log) {
            log = "\n" + log;
            writer.write(log);
        }

        private void initializeWriter() {
            try {
                File file = new File(logFilePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                writer = new PrintWriter(bufferedWriter);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
