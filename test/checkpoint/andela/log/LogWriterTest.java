package checkpoint.andela.log;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferFactory;
import checkpoint.andela.db.DbRecord;
import checkpoint.andela.parser.FileParser;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class LogWriterTest {

    @Test
    public void testRun() throws Exception {
        String logFileName = "logs/logFile-"
                + (new Date()).toString()
                + ".txt";

        Buffer<DbRecord> dbRecordBuffer = BufferFactory.getDbRecordBuffer();
        Buffer<String> logBuffer = BufferFactory.getStringLogBuffer();

        FileParser fileParser
                = new FileParser("data/reactions.dat", dbRecordBuffer, logBuffer);
        LogWriter logWriter = new LogWriter(logBuffer, logFileName);

        Thread fileParserThread = new Thread(fileParser);
        Thread logWriterThread = new Thread(logWriter);

        fileParserThread.run();
        logWriterThread.run();
    }
}