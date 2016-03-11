# ReactDb
This is a collection of Java classes for parsing reaction data from an Attribute-Value data file into a relational database, while generating a log of the entire process.

##Attribute-Value File Summary
Attribute-value files store records as lines of text. Each line consists of a key-value pair separated by a _space-dash-space_ character sequence. Individual records are separated by a line preceded by the **//** character sequence. Comments are preceded by a **#**.

##Operations Outline
The **FileParser** class reads in data from the data file and writes it into a records buffer, instance of the **Buffer** interface (which is availed as a singleton by the **BufferSingletons** class), while recording its progress to a log buffer. The **DBWriter** class concurrently pulls the records from the records buffer and writes them to an SQL database, while also recording its progress to a log buffer. The **LogWriter** class retrieves log messages from the log buffer and writes them to a text file.

##Sample Code
```java
    @Test
    public void integratedTest() throws Exception {
        String filePath = "data/reactions.dat";

        String logFileName = "logs/logFile-"
                + (new Date()).toString()
                + ".txt";

        Buffer<DbRecord> recordBuffer = BufferSingletons.getDbRecordBuffer();
        Buffer<String> logBuffer = BufferSingletons.getStringLogBuffer();

        MyDbWriter myDbWriter = new MyDbWriter(Constants.MYSQL_DRIVER_NAME,
                Constants.MYSQL_URL,
                Constants.MYSQL_USERNAME,
                Constants.MYSQL_PASSWORD,
                Constants.MYSQL_TABLE_NAME);

        DBWriter writer = new DBWriter(recordBuffer, myDbWriter, logBuffer);

        FileParser fileParser = new FileParser(filePath,
                recordBuffer, logBuffer);

        LogWriter logWriter = new LogWriter(logBuffer, logFileName);

        Thread fileParserThread = new Thread(fileParser);
        Thread dbWriterThread = new Thread(writer);
        Thread logWriterThread = new Thread(logWriter);

        fileParserThread.run();
        logWriterThread.run();
        dbWriterThread.run();
    }
```
