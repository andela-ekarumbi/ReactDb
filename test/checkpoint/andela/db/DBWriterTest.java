package checkpoint.andela.db;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferSingletons;
import checkpoint.andela.config.Constants;
import checkpoint.andela.parser.FileParser;

import checkpoint.andela.utility.Utility;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DBWriterTest {

    private FileParser fileParser;

    private DBWriter writer;

    @Before
    public void beforeTestRun() {
        String filePath = "data/react.dat";

        Buffer<DbRecord> recordBuffer = BufferSingletons.getDbRecordBuffer();
        Buffer<String> logBuffer = BufferSingletons.getStringLogBuffer();

        MyDbWriter myDbWriter = new MyDbWriter(Constants.MYSQL_DRIVER_NAME,
                Constants.MYSQL_URL,
                Constants.MYSQL_USERNAME,
                Constants.MYSQL_PASSWORD,
                Constants.MYSQL_TABLE_NAME);

        writer = new DBWriter(recordBuffer, myDbWriter, logBuffer);

        fileParser = new FileParser(filePath, recordBuffer, logBuffer);
    }

    @Test
    public void testRun() throws Exception {
        Thread fileParserThread = new Thread(fileParser);
        Thread dbWriterThread = new Thread(writer);

        int countBeforeWrite = Utility.getDbRecordCount();

        Thread.currentThread().setName("Test thread");

        fileParserThread.run();
        dbWriterThread.run();

        int countAfterWrite = Utility.getDbRecordCount();

        assertTrue(countAfterWrite > countBeforeWrite);
    }
}