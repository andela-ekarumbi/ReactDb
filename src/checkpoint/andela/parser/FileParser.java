package checkpoint.andela.parser;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.db.DbRecord;
import checkpoint.andela.utility.Utility;

import java.io.*;
import java.util.Date;

public class FileParser implements Runnable {

    private File dataFile;

    private FileReader fileReader;

    private Buffer<DbRecord> dbRecordBuffer;

    private Buffer<String> logBuffer;

    private BufferedReader bufferedReader;

    public FileParser(String filePath,
                      Buffer<DbRecord> dbRecordBuffer,
                      Buffer<String> logBuffer) {
        this.dataFile = new File(filePath);
        this.dbRecordBuffer = dbRecordBuffer;
        this.logBuffer = logBuffer;
    }

    private void parseFile() {
        try {
            fileReader = new FileReader(dataFile);
            bufferedReader = new BufferedReader(fileReader);
            startLineByLineTraversal();
            reportEndOfInput();
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

    private void reportEndOfInput() {
        dbRecordBuffer.setInputEnded(true);
        logBuffer.setInputEnded(true);
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
            logRecordParsing(dbRecord);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void logRecordParsing(DbRecord dbRecord) {
        String logEntry = generateLogEntry(dbRecord);
        logBuffer.addToBuffer(logEntry);
    }

    private String generateLogEntry(DbRecord dbRecord) {
        String currentTime = (new Date()).toString();
        String recordUniqueId
                = dbRecord.getAllColumns().get("UNIQUE-ID").get(0);
        String currentThreadId = Long.toString(Thread.currentThread().getId());
        return "FileParser Thread #"
                + currentThreadId
                + " at "
                + currentTime
                + ": Wrote UNIQUE-ID "
                + recordUniqueId
                + " to buffer.";
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

    @Override
    public void run() {
        parseFile();
    }
}
