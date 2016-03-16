package checkpoint.andela.log;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferSingletons;
import checkpoint.andela.models.Reaction;
import checkpoint.andela.parser.FileParser;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogWriterTest {

    @Test
    public void testRun() throws Exception {
        String logFileName = "logs/logFile-"
                + (new Date()).toString()
                + ".txt";

        Buffer<Reaction> reactionBuffer = BufferSingletons.getReactionBuffer();
        Buffer<String> logBuffer = BufferSingletons.getStringLogBuffer();

        FileParser fileParser
                = new FileParser("data/react.dat", reactionBuffer, logBuffer);
        LogWriter logWriter = new LogWriter(logBuffer, logFileName);

        Thread fileParserThread = new Thread(fileParser);

        fileParserThread.run();

        ScheduledExecutorService scheduledExecutorService
                = Executors.newScheduledThreadPool(1);
        scheduledExecutorService
                .scheduleAtFixedRate(logWriter, 0, 10, TimeUnit.MILLISECONDS);
    }
}