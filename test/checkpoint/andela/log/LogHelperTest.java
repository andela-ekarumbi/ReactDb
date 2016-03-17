package checkpoint.andela.log;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferSingletons;
import checkpoint.andela.models.Reaction;
import checkpoint.andela.parser.FileParseHelper;
import checkpoint.andela.parser.FileParser;
import org.junit.Test;

import java.io.File;
import java.util.Date;

import static org.junit.Assert.*;

public class LogHelperTest {

    @Test
    public void testStartLogging() throws Exception {
        String logFileName = "logs/logFile-"
                + (new Date()).toString()
                + ".txt";

        File logFile = new File(logFileName);
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        long lengthBeforeLog = logFile.length();

        Buffer<Reaction> reactionBuffer = BufferSingletons.getReactionBuffer();
        Buffer<String> logBuffer = BufferSingletons.getStringLogBuffer();

        FileParseHelper fileParseHelper
                = new FileParseHelper("data/react.dat",
                reactionBuffer, logBuffer);

        fileParseHelper.parseFile();

        LogHelper logHelper = new LogHelper(logBuffer, logFileName);

        logHelper.startLogging();

        long lengthAfterLog = logFile.length();

        assertTrue(lengthAfterLog > lengthBeforeLog);
    }
}