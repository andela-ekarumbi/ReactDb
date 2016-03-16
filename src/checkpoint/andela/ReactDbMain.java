package checkpoint.andela;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferSingletons;
import checkpoint.andela.config.Constants;
import checkpoint.andela.db.DBWriter;
import checkpoint.andela.db.DbHelper;
import checkpoint.andela.db.DbRecord;
import checkpoint.andela.log.LogWriter;
import checkpoint.andela.parser.FileParser;

public class ReactDbMain {

    public static void main(String[] args) {


        /*Buffer<DbRecord> dbRecordBuffer = BufferSingletons.getReactionBuffer();
        Buffer<String> logBuffer = BufferSingletons.getStringLogBuffer();


        FileParser fileParser = new FileParser("data/reactions.dat",
                dbRecordBuffer,
                logBuffer);

        DbHelper dbHelper = new DbHelper(Constants.MYSQL_DRIVER_NAME,
                Constants.MYSQL_URL,
                Constants.MYSQL_USERNAME,
                Constants.MYSQL_PASSWORD,
                Constants.DATABASE_NAME,
                Constants.MYSQL_TABLE_NAME);

        DBWriter dbWriter = new DBWriter(dbRecordBuffer, dbHelper, logBuffer);

        LogWriter logWriter = new LogWriter(logBuffer, "logs/log.txt");

        Thread fileParserThread = new Thread(fileParser);
        Thread dbWriterThread = new Thread(dbWriter);
        Thread logWriterThread = new Thread(logWriter);

        fileParserThread.start();
        dbWriterThread.start();
        logWriterThread.start();*/
    }
}
