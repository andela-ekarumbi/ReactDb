/**
 * This class parses an attribute-value data file, extracts individual records
 * and writes them to a temporary records buffer, while writing log messages to
 * a temporary log buffer.
 */

package checkpoint.andela.parser;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.models.Reaction;
import checkpoint.andela.utility.ReactionHelper;
import checkpoint.andela.utility.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileParseHelper {

    private File dataFile;

    private FileReader fileReader;

    private Buffer<Reaction> reactionBuffer;

    private Buffer<String> logBuffer;

    private BufferedReader bufferedReader;

    /**
     * Creates a new {@code FileParser}
     *
     * @param filePath       the path to the data file.
     * @param reactionBuffer the temporary records buffer.
     * @param logBuffer      the temporary log buffer.
     */

    public FileParseHelper(String filePath,
                           Buffer<Reaction> reactionBuffer,
                           Buffer<String> logBuffer) {
        this.dataFile = new File(filePath);
        this.reactionBuffer = reactionBuffer;
        this.logBuffer = logBuffer;
    }

    public void parseFile() {
        try {
            fileReader = new FileReader(dataFile);
            bufferedReader = new BufferedReader(fileReader);
            startLineByLineTraversal();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            closeStreams();
        }
    }

    private void closeStreams() {
        try {
            bufferedReader.close();
            fileReader.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void startLineByLineTraversal() {
        try {
            String currentLine = bufferedReader.readLine();
            while (!isNull(currentLine)) {
                if (!isDelimiter(currentLine) && !isComment(currentLine)) {
                    parseRecordStartingFrom(currentLine);
                }
                currentLine = bufferedReader.readLine();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void parseRecordStartingFrom(String currentLine) {
        try {
            Reaction reaction = new Reaction();
            while (!isNull(currentLine) && !isDelimiter(currentLine)) {
                if (!isComment(currentLine)) {
                    extractRecordFromLine(currentLine, reaction);
                }
                currentLine = bufferedReader.readLine();
            }
            if (reactionBuffer.addToBuffer(reaction)) {
                writeLog(reaction);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void writeLog(Reaction reaction) {
        String entryTermination = " from file and wrote to buffer.";
        String logEntry
                = Utility.generateLogMessage(reaction, entryTermination,
                "FileParser Thread");
        logBuffer.addToBuffer(logEntry);
    }

    private boolean isDelimiter(String currentLine) {
        return currentLine.startsWith("//");
    }

    private boolean isComment(String currentLine) {
        return currentLine.startsWith("#");
    }

    private boolean isNull(String currentLine) {
        return currentLine == null;
    }

    private void extractRecordFromLine(String currentLine, Reaction reaction) {
        String[] keyValueArray = currentLine.split(" - ");
        if (keyValueArray.length > 1) {
            extractKeyAndValue(keyValueArray, reaction);
        }
    }

    private void extractKeyAndValue(String[] keyValueArray, Reaction reaction) {
        String key = keyValueArray[0].trim();
        String value = keyValueArray[1].trim();
        ReactionHelper.setReactionProperty(key, value, reaction);
    }
}
