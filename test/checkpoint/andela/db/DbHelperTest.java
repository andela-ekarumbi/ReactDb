package checkpoint.andela.db;

import checkpoint.andela.config.Constants;
import checkpoint.andela.utility.Utility;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DbHelperTest {

    DbRecord dbRecord;
    String testId;

    private String generateId() {
        return Double.toString(Math.random() * 100000);
    }

    @Before
    public void beforeTestAddRecords() {
        dbRecord = new DbRecord();
        testId = generateId();
        dbRecord.addColumn("UNIQUE-ID", testId);
        dbRecord.addColumn("TYPES", "Small-Molecule-Reactions");
        dbRecord.addColumn("TYPES", "Chemical-Reactions");
        dbRecord.addColumn("ATOM-MAPPINGS", "(:NO-HYDROGEN-ENCODING (0 1 6 5 4"
                + "2 7 3) (((ETOH 0 2) (|Pi| 3 7))"
                + "((CPD-8978 0 6) (WATER 7 7))))");
        dbRecord.addColumn("CREDITS", "SRI");
        dbRecord.addColumn("CREDITS", "|kaipa|");
        dbRecord.addColumn("EC-NUMBER", "EC-3.1.3.1");
        dbRecord.addColumn("ENZYMATIC-REACTION", "ENZRXNMT2-1088");
        dbRecord.addColumn("LEFT", "CPD-8978");
        dbRecord.addColumn("LEFT", "WATER");
        dbRecord.addColumn("ORPHAN?", ":NO");
        dbRecord.addColumn("PHYSIOLOGICALLY-RELEVANT?", "T");
        dbRecord.addColumn("RIGHT", "|Pi|");
        dbRecord.addColumn("RIGHT", "ETOH");
    }

    @Test
    public void testAddRecords() throws Exception {
        DbHelper writer = new DbHelper(Constants.MYSQL_DRIVER_NAME,
                Constants.MYSQL_URL,
                Constants.MYSQL_USERNAME,
                Constants.MYSQL_PASSWORD,
                Constants.DATABASE_NAME,
                Constants.MYSQL_TABLE_NAME);

        int countBeforeWrite = Utility.getDbRecordCount();

        List<DbRecord> records = new ArrayList<>();
        records.add(dbRecord);

        assertTrue(writer.writeRecords(records));

        int countAfterWrite = Utility.getDbRecordCount();
        assertTrue(countAfterWrite > countBeforeWrite);
    }
}