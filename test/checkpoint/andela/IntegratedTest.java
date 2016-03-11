package checkpoint.andela;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferFactory;
import checkpoint.andela.config.Constants;
import checkpoint.andela.db.DBWriter;
import checkpoint.andela.db.DbRecord;
import checkpoint.andela.db.MyDbWriter;
import checkpoint.andela.log.LogWriter;
import checkpoint.andela.parser.FileParser;

import org.junit.Test;

import java.util.Date;

public class IntegratedTest {
    @Test
    public void integratedTest() throws Exception {
        String filePath = "data/reactions.dat";

        String logFileName = "logs/logFile-"
                + (new Date()).toString()
                + ".txt";

        Buffer<DbRecord> recordBuffer = BufferFactory.getDbRecordBuffer();
        Buffer<String> logBuffer = BufferFactory.getStringLogBuffer();

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
    }
}
