package checkpoint.andela.parser;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferFactory;
import checkpoint.andela.db.DbRecord;

import java.io.*;

public class FileParser implements Runnable {

    private File dataFile;

    private FileReader fileReader;

    private Buffer<DbRecord> dbRecordBuffer;

    private BufferedReader bufferedReader;

    public FileParser(String filePath) {
        this.dataFile = new File(filePath);
    }

    public void parseFile() {
        try {
            fileReader = new FileReader(dataFile);
            dbRecordBuffer = BufferFactory.getDbRecordBuffer();
            bufferedReader = new BufferedReader(fileReader);
            startLineByLineTraversal();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void startLineByLineTraversal() {
        try {
            String currentLine = bufferedReader.readLine();
            while (!isNull(currentLine)) {
                if (!isDelimiter(currentLine)) {
                    if (!isComment(currentLine)) {
                        addRecordToBuffer(currentLine);
                    }
                }
                currentLine = bufferedReader.readLine();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void addRecordToBuffer(String currentLine) {
        try {
            DbRecord dbRecord = new DbRecord();
            while (!isNull(currentLine) && !isDelimiter(currentLine)) {
                if (!isComment(currentLine)) {
                    extractRecordFromLine(currentLine, dbRecord);
                }
                currentLine = bufferedReader.readLine();
            }
            dbRecordBuffer.addToBuffer(dbRecord);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
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
        dbRecord.addColumn(key, value);
    }

    @Override
    public void run() {
        parseFile();
    }
}
