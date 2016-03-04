package checkpoint.andela.parser;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferFactory;
import checkpoint.andela.utility.DbRecord;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FileParserTest {

    @Before
    public void beforeTestParseFile() {
        String filePath = "data/reactions.dat";
        FileParser fileParser = new FileParser(filePath);

        (new Thread(fileParser)).run();
    }

    @Test
    public void testParseFile() throws Exception {
        Buffer<DbRecord> dbRecordBuffer = BufferFactory.getDbRecordBuffer();
        String issuedKey = dbRecordBuffer.registerClientForTracking();

        assertTrue(dbRecordBuffer.checkIfNewData(issuedKey));

        List<DbRecord> records = dbRecordBuffer.getLatestData(issuedKey);
        assertNotNull(records);
        assertTrue(records.size() > 0);

        DbRecord dbRecord = records.get(0);
        assertNotNull(dbRecord);
        assertTrue(dbRecord.getAllColumns().containsKey("UNIQUE-ID"));

        assertFalse(dbRecordBuffer.checkIfNewData(issuedKey));
    }
}