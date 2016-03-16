package checkpoint.andela.db;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferSingletons;
import checkpoint.andela.config.Constants;
import checkpoint.andela.models.Reaction;
import checkpoint.andela.parser.FileParser;

import checkpoint.andela.utility.Utility;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DBWriterTest {

    private FileParser fileParser;

    private DBWriter dbWriter;

    @Before
    public void beforeTestRun() {
        String filePath = "data/react.dat";

        Buffer<Reaction> reactionBuffer = BufferSingletons.getReactionBuffer();
        Buffer<String> logBuffer = BufferSingletons.getStringLogBuffer();

        DbHelper dbHelper = new DbHelper(Constants.MYSQL_DRIVER_NAME,
                Constants.MYSQL_URL,
                Constants.MYSQL_USERNAME,
                Constants.MYSQL_PASSWORD,
                Constants.DATABASE_NAME,
                Constants.MYSQL_TABLE_NAME);

        dbWriter = new DBWriter(reactionBuffer, dbHelper, logBuffer);

        fileParser = new FileParser(filePath, reactionBuffer, logBuffer);
    }

    @Test
    public void testRun() throws Exception {
        Thread fileParserThread = new Thread(fileParser);
        Thread dbWriterThread = new Thread(dbWriter);

        int countBeforeWrite = Utility.getDbRecordCount();

        fileParserThread.start();
        dbWriterThread.start();

        int countAfterWrite = Utility.getDbRecordCount();

        assertTrue(countAfterWrite > countBeforeWrite);
    }
}