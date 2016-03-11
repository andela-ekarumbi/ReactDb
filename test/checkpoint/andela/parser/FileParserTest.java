package checkpoint.andela.parser;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferFactory;
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
<<<<<<< HEAD
        fileParser = new FileParser(filePath,
                BufferFactory.getDbRecordBuffer());
=======

        Buffer<DbRecord> recordBuffer = BufferFactory.getDbRecordBuffer();
        Buffer<String> logBuffer = BufferFactory.getStringLogBuffer();

        fileParser = new FileParser(filePath, recordBuffer, logBuffer);
>>>>>>> d7ab59a1f40dc9d663fd872102186e68f4a12b7e
    }

    @Test
    public void testRun() throws Exception {
        Buffer<DbRecord> dbRecordBuffer = BufferFactory.getDbRecordBuffer();
        String issuedKey = dbRecordBuffer.registerClientForTracking();

        Thread fileParserThread = new Thread(fileParser);
        fileParserThread.run();

<<<<<<< HEAD
        assertTrue(dbRecordBuffer.checkIfNewData(issuedKey));
=======
        assertTrue(dbRecordBuffer.isThereNewData(issuedKey));
>>>>>>> d7ab59a1f40dc9d663fd872102186e68f4a12b7e

        List<DbRecord> records = dbRecordBuffer.getLatestData(issuedKey);
        assertNotNull(records);
        assertTrue(records.size() > 0);

        DbRecord dbRecord = records.get(0);
        assertNotNull(dbRecord);
        assertTrue(dbRecord.getAllColumns().containsKey("UNIQUE-ID"));

<<<<<<< HEAD
        assertFalse(dbRecordBuffer.checkIfNewData(issuedKey));
=======
        assertFalse(dbRecordBuffer.isThereNewData(issuedKey));
>>>>>>> d7ab59a1f40dc9d663fd872102186e68f4a12b7e
    }
}