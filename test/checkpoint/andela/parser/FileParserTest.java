package checkpoint.andela.parser;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferSingletons;
import checkpoint.andela.db.DbRecord;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FileParserTest {

    private FileParser fileParser;

    @Before
    public void beforeTestRun() {
        String filePath = "data/reactions.dat";
        Buffer<DbRecord> recordBuffer = BufferSingletons.getDbRecordBuffer();
        Buffer<String> logBuffer = BufferSingletons.getStringLogBuffer();

        fileParser = new FileParser(filePath, recordBuffer, logBuffer);
    }

    @Test
    public void testRun() throws Exception {
        Buffer<DbRecord> dbRecordBuffer = BufferSingletons.getDbRecordBuffer();
        String issuedKey = dbRecordBuffer.registerClientForTracking();

        Thread fileParserThread = new Thread(fileParser);
        fileParserThread.run();


        assertTrue(dbRecordBuffer.isThereNewData(issuedKey));

        List<DbRecord> records = dbRecordBuffer.getLatestData(issuedKey);
        assertNotNull(records);
        assertTrue(records.size() > 0);

        DbRecord dbRecord = records.get(0);
        assertNotNull(dbRecord);
        assertTrue(dbRecord.getAllColumns().containsKey("UNIQUE-ID"));

        assertFalse(dbRecordBuffer.isThereNewData(issuedKey));
    }
}