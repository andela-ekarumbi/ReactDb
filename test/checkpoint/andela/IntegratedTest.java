package checkpoint.andela;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferSingletons;
import checkpoint.andela.config.Constants;
import checkpoint.andela.db.DBWriter;
import checkpoint.andela.db.DbRecord;
import checkpoint.andela.db.MyDbWriter;
import checkpoint.andela.log.LogWriter;
import checkpoint.andela.parser.FileParser;

import checkpoint.andela.utility.Utility;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class IntegratedTest {
    @Test
    public void integratedTest() throws Exception {
        String filePath = "data/reactions.dat";

        String logFileName = "logs/logFile-"
                + (new Date()).toString()
                + ".txt";

        int countBeforeWrite = Utility.getDbRecordCount();

        Buffer<DbRecord> recordBuffer = BufferSingletons.getDbRecordBuffer();
        Buffer<String> logBuffer = BufferSingletons.getStringLogBuffer();

        MyDbWriter myDbWriter = new MyDbWriter(Constants.MYSQL_DRIVER_NAME,
                Constants.MYSQL_URL,
                Constants.MYSQL_USERNAME,
                Constants.MYSQL_PASSWORD,
                Constants.MYSQL_TABLE_NAME);

        DBWriter writer = new DBWriter(recordBuffer, myDbWriter, logBuffer);

        FileParser fileParser = new FileParser(filePath,
                recordBuffer, logBuffer);

        LogWriter logWriter = new LogWriter(logBuffer, logFileName);

        Thread fileParserThread = new Thread(fileParser);
        Thread dbWriterThread = new Thread(writer);
        Thread logWriterThread = new Thread(logWriter);

        fileParserThread.run();
        logWriterThread.run();
        dbWriterThread.run();

        int countAfterWrite = Utility.getDbRecordCount();

        assertTrue(countAfterWrite > countBeforeWrite);
    }
}
