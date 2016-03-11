/**
 * This class packages logic for writing data from a list of {@code DbRecord}
 * into an SQL database.
 * */

package checkpoint.andela.db;

import java.sql.*;
import java.util.*;

public class MyDbWriter {

    private String dbUrl;

    private String dbDiverName;

    private String dbUsername;

    private String dbPassword;

    private String dbTableName;

    private Connection connection;

    private Statement statement;

    /**
     * Creates a new {@code MyDbWriter}.
     * @param dbDriverName the package name of the database driver to be used.
     * @param dbUrl the url of the database to be accessed.
     * @param dbUsername the username of the database account to be used.
     * @param dbPassword the password of the database account to be used.
     * @param dbTableName the name of the database table where records are
     * going to be written to.
     * */

    public MyDbWriter(String dbDriverName,
                      String dbUrl,
                      String dbUsername,
                      String dbPassword,
                      String dbTableName) {

        this.dbDiverName = dbDriverName;
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbTableName = dbTableName;

        registerDbDriver();
    }

    /**
     * Adds a list of records to the database.
     * @param dbRecords the list of {@code DbRecord} objects to be added.
     * @return true for a successful operation, or false otherwise.
     * */

    public boolean addRecords(List<DbRecord> dbRecords) {
        boolean success = false;

        try {
            String sqlString = getInsertStatement(dbRecords);
            System.out.println(sqlString);

            initializeResources();

            statement.execute(sqlString);

            success = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            finalizeResources();
        }

        return success;
    }

    private void initializeResources() {
        try {
            connection = DriverManager.getConnection(dbUrl,
                    dbUsername,
                    dbPassword);
            statement = connection.createStatement();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void finalizeResources() {
        try {
            statement.close();
            connection.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String getInsertStatement(List<DbRecord> records) {
        StringBuilder stringBuilder = new StringBuilder();
        for (DbRecord record : records) {
            Map<String, List<String>> columns = record.getAllColumns();
            stringBuilder.append("\nINSERT INTO ")
                    .append(dbTableName)
                    .append(" (")
                    .append(getColumnNameString(columns.keySet()))
                    .append(") VALUES (")
                    .append(getColumnValuesString(columns))
                    .append(");");
        }
        return stringBuilder.toString();
    }

    private String getColumnNameString(Set<String> columnNames) {
        StringBuilder stringBuilder = new StringBuilder();
        int count = columnNames.size();

        Object[] columnArray = columnNames.toArray();

        for (int i = 0; i < count; i++) {
            String columnName = (String)columnArray[i];
            stringBuilder.append("`")
                    .append(columnName)
                    .append("`");

            if (i < count - 1) {
                stringBuilder.append(", ");
            }
        }

        return stringBuilder.toString();
    }

    private String getColumnValuesString(Map<String, List<String>> columns) {
        StringBuilder stringBuilder = new StringBuilder();

        Object[] columnNameArray = columns.keySet().toArray();
        int count = columnNameArray.length;

        for (int i = 0; i < count; i++) {
            List<String> columnValues = columns.get(columnNameArray[i]);
            appendColumnValues(stringBuilder, columnValues);

            if (i < count - 1) {
                stringBuilder.append(", ");
            }
        }

        return stringBuilder.toString();
    }

    private void appendColumnValues(StringBuilder stringBuilder,
                                   List<String> columnValues) {
        int valuesCount = columnValues.size();

        if (valuesCount == 0) {
            stringBuilder.append("NULL");
        } else {
            stringBuilder.append("'");

            for (int j = 0; j < valuesCount; j++) {
                stringBuilder.append(columnValues.get(j));

                if (valuesCount > 1) {
                    if (j < valuesCount - 1) {
                        stringBuilder.append("+del");
                    }
                }
            }

            stringBuilder.append("'");
        }
    }

    private void registerDbDriver() {
        try {
            Class.forName(dbDiverName);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
