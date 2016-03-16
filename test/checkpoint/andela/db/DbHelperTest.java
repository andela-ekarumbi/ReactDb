package checkpoint.andela.db;

import checkpoint.andela.config.Constants;
import checkpoint.andela.models.Reaction;
import checkpoint.andela.utility.Utility;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DbHelperTest {

    private Reaction reaction;
    private String testId;

    private String generateId() {
        return Double.toString(Math.random() * 100000);
    }

    @Before
    public void beforeTestAddRecords() {
        reaction = new Reaction();
        testId = generateId();
        reaction.setUniqueId(testId);
        reaction.setTypes("Small-Molecule-Reactions");
        reaction.setTypes("Chemical-Reactions");
        reaction.setAtomMappings("(:NO-HYDROGEN-ENCODING (0 1 6 5 4"
                + "2 7 3) (((ETOH 0 2) (|Pi| 3 7))"
                + "((CPD-8978 0 6) (WATER 7 7))))");
        reaction.setCredits("SRI");
        reaction.setCredits("|kaipa|");
        reaction.setEcNumber("EC-3.1.3.1");
        reaction.setEnzyMaticReaction("ENZRXNMT2-1088");
        reaction.setLeft("CPD-8978");
        reaction.setLeft("WATER");
        reaction.setOrphan(":NO");
        reaction.setPhysiologicallyRelevant("T");
        reaction.setRight("|Pi|");
        reaction.setRight("ETOH");
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

        List<Reaction> reactions = new ArrayList<>();
        reactions.add(reaction);

        assertTrue(writer.writeReactions(reactions));

        int countAfterWrite = Utility.getDbRecordCount();
        assertTrue(countAfterWrite > countBeforeWrite);
    }
}