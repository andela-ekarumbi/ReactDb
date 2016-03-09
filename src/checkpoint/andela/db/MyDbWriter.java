package checkpoint.andela.db;

import checkpoint.andela.config.Constants;

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

    public boolean addNewDbRecord(DbRecord dbRecord) {
        boolean success = false;

        try {
            String sqlString = getInsertStatement(dbRecord);

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

    private String getInsertStatement(DbRecord record) {
        Map<String, List<String>> columns = record.getAllColumns();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO ")
                .append(dbTableName)
                .append(" (")
                .append(getColumnNameString(columns.keySet()))
                .append(") VALUES (")
                .append(getColumnValuesString(columns))
                .append(");");

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
