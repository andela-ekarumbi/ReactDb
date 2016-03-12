/**
 * This class parses an attribute-value data file, extracts individual records
 * and writes them to a temporary records buffer, while writing log messages to
 * a temporary log buffer.
 */

package checkpoint.andela.parser;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.db.DbRecord;
import checkpoint.andela.utility.Utility;

import java.io.*;

public class FileParser implements Runnable {

    private File dataFile;

    private FileReader fileReader;

    private Buffer<DbRecord> dbRecordBuffer;

    private Buffer<String> logBuffer;

    private BufferedReader bufferedReader;

    /**
     * Creates a new {@code FileParser}
     * @param filePath the path to the data file.
     * @param dbRecordBuffer the temporary records buffer.
     * @param logBuffer the temporary log buffer.
     * */

    public FileParser(String filePath,
                      Buffer<DbRecord> dbRecordBuffer,
                      Buffer<String> logBuffer) {
        this.dataFile = new File(filePath);
        this.dbRecordBuffer = dbRecordBuffer;
        this.logBuffer = logBuffer;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("FileParser Thread");
        parseFile();
    }

    private void parseFile() {
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
            DbRecord dbRecord = new DbRecord();
            while (!isNull(currentLine) && !isDelimiter(currentLine)) {
                if (!isComment(currentLine)) {
                    extractRecordFromLine(currentLine, dbRecord);
                }
                currentLine = bufferedReader.readLine();
            }
            dbRecordBuffer.addToBuffer(dbRecord);
            writeLog(dbRecord);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void writeLog(DbRecord dbRecord) {
        String entryTermination = " from file and wrote to buffer.";
        String logEntry
                = Utility.generateLogMessage(dbRecord, entryTermination,
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

    private void extractRecordFromLine(String currentLine, DbRecord dbRecord) {
        if (currentLine != null && !isDelimiter(currentLine)) {
            String[] keyValueArray = currentLine.split(" - ");
            if (keyValueArray.length > 1) {
                extractKeyAndValue(keyValueArray, dbRecord);
            }
        }
    }

    private void extractKeyAndValue(String[] keyValueArray, DbRecord dbRecord) {
        String key = keyValueArray[0].trim();
        String value = keyValueArray[1].trim();
        if (Utility.isValidColumn(key)) {
            dbRecord.addColumn(key, value);
        }
    }
}
