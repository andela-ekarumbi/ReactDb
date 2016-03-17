package checkpoint.andela.parser;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferSingletons;
import checkpoint.andela.models.Reaction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FileParseHelperTest {

    private FileParseHelper fileParseHelper;

    @Before
    public void beforeTestParseFile() {
        String filePath = "data/reactions.dat";
        Buffer<Reaction> reactionBuffer = BufferSingletons.getReactionBuffer();
        Buffer<String> logBuffer = BufferSingletons.getStringLogBuffer();

        fileParseHelper
                = new FileParseHelper(filePath, reactionBuffer, logBuffer);
    }

    @Test
    public void testParseFile() throws Exception {
        Buffer<Reaction> reactionBuffer = BufferSingletons.getReactionBuffer();
        String issuedKey = reactionBuffer.registerClientForTracking();

       fileParseHelper.parseFile();


        assertTrue(reactionBuffer.isThereNewData(issuedKey));

        List<Reaction> reactions = reactionBuffer.getLatestData(issuedKey);
        assertNotNull(reactions);
        assertTrue(reactions.size() > 0);

        Reaction reaction = reactions.get(0);
        assertNotNull(reaction);

        assertFalse(reactionBuffer.isThereNewData(issuedKey));
    }
}