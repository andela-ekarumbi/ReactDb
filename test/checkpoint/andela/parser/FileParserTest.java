package checkpoint.andela.parser;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferSingletons;

import checkpoint.andela.models.Reaction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FileParserTest {

    private FileParser fileParser;

    @Before
    public void beforeTestRun() {
        String filePath = "data/reactions.dat";
        Buffer<Reaction> reactionBuffer = BufferSingletons.getReactionBuffer();
        Buffer<String> logBuffer = BufferSingletons.getStringLogBuffer();

        fileParser = new FileParser(filePath, reactionBuffer, logBuffer);
    }

    @Test
    public void testRun() throws Exception {
        Buffer<Reaction> reactionBuffer = BufferSingletons.getReactionBuffer();
        String issuedKey = reactionBuffer.registerClientForTracking();

        Thread fileParserThread = new Thread(fileParser);
        fileParserThread.run();


        assertTrue(reactionBuffer.isThereNewData(issuedKey));

        List<Reaction> reactions = reactionBuffer.getLatestData(issuedKey);
        assertNotNull(reactions);
        assertTrue(reactions.size() > 0);

        Reaction reaction = reactions.get(0);
        assertNotNull(reaction);

        assertFalse(reactionBuffer.isThereNewData(issuedKey));
    }
}