package checkpoint.andela;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferSingletons;
import checkpoint.andela.config.Constants;
import checkpoint.andela.db.DBWriter;
import checkpoint.andela.db.DbHelper;
import checkpoint.andela.log.LogHelper;
import checkpoint.andela.log.LogWriter;
import checkpoint.andela.models.Reaction;
import checkpoint.andela.parser.FileParseHelper;
import checkpoint.andela.parser.FileParser;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReactDbMain {

    public void main() {
        String logFileName = "logs/logFile "
                + (new Date()).toString()
                + ".txt";

        Buffer<Reaction> reactionBuffer = BufferSingletons.getReactionBuffer();
        Buffer<String> logBuffer = BufferSingletons.getStringLogBuffer();


        FileParseHelper fileParseHelper = new FileParseHelper("data/reactions.dat",
                reactionBuffer,
                logBuffer);
        FileParser fileParser = new FileParser(fileParseHelper);

        DbHelper dbHelper = new DbHelper(Constants.MYSQL_DRIVER_NAME,
                Constants.MYSQL_URL,
                Constants.MYSQL_USERNAME,
                Constants.MYSQL_PASSWORD,
                Constants.MYSQL_TABLE_NAME);

        DBWriter dbWriter = new DBWriter(reactionBuffer, dbHelper, logBuffer);

        LogHelper logHelper = new LogHelper(logBuffer, logFileName);
        LogWriter logWriter = new LogWriter(logHelper);

        Thread fileParserThread = new Thread(fileParser);

        ScheduledExecutorService scheduledExecutorService
                = Executors.newScheduledThreadPool(2);

        scheduledExecutorService
                .scheduleAtFixedRate(dbWriter, 0, 10, TimeUnit.MILLISECONDS);

        scheduledExecutorService
                .scheduleAtFixedRate(logWriter, 0, 10, TimeUnit.MILLISECONDS);

        fileParserThread.start();
    }
}
