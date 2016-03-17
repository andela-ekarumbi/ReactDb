/**
 * This class packages logic for writing data from a list of {@code DbRecord}
 * into an SQL database.
 */

package checkpoint.andela.db;

import checkpoint.andela.models.Reaction;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class DbHelper {

    private String dbUrl;

    private String dbDiverName;

    private String dbUsername;

    private String dbPassword;

    private String dbTableName;

    private Connection connection;

    private boolean isDriverRegistered = false;

    /**
     * Creates a new {@code DbHelper}.
     * @param dbDriverName the package name of the database driver to be used.
     * @param dbUrl the url of the database to be accessed.
     * @param dbUsername the username of the database account to be used.
     * @param dbPassword the password of the database account to be used.
     * @param dbTableName the name of the database table where records are
     * going to be written to.
     * */

    public DbHelper(String dbDriverName,
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
     * @param reactions the list of {@code DbRecord} objects to be added.
     * @return true for a successful operation, or false otherwise.
     * */

    public boolean writeReactions(List<Reaction> reactions) {
        boolean success = false;

        try {
            List<String> sqlStatements = getInsertStatements(reactions);

            connection = DriverManager.getConnection(dbUrl,
                    dbUsername,
                    dbPassword);

            for (String sql : sqlStatements) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.execute();
                statement.close();
            }

            success = true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }

        return success;
    }

    private List<String> getInsertStatements(List<Reaction> reactions) {
        List<String> statements = new ArrayList<>();
        try {
            for (Reaction reaction : reactions) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("INSERT INTO ")
                        .append(dbTableName)
                        .append(" (")
                        .append(getColumnString(reaction, true))
                        .append(") VALUES (")
                        .append(getColumnString(reaction, false))
                        .append(");");
                statements.add(stringBuilder.toString());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return statements;
    }

    private void setFieldAccessible(Field field) {
        field.setAccessible(true);
    }

    private Field[] getFieldsArray(Reaction reaction) {
        Class classObject = reaction.getClass();
        return classObject.getDeclaredFields();
    }

    private String getColumnString(Reaction reaction,
                                   boolean expectsColumnNames)
            throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();

        Field[] fields = getFieldsArray(reaction);
        int fieldLength = fields.length;

        for (int i = 0; i < fieldLength; i++) {
            Field field = fields[i];
            setFieldAccessible(field);
            if (expectsColumnNames) {
                String fieldName = field.getName();
                stringBuilder.append(generateColumnNameString(fieldName));
            } else {
                String fieldValue = (String)field.get(reaction);
                stringBuilder.append(generateColumnValueString(fieldValue));
            }
            if (i != fieldLength - 1) {
                stringBuilder.append(", ");
            }
        }

        return stringBuilder.toString();
    }

    private String generateColumnNameString(String fieldName) {
        return "`" + fieldName + "`";
    }

    private String generateColumnValueString(String fieldValue) {
        return "'" + fieldValue + "'";
    }

    private void registerDbDriver() {
        try {
            if (!isDriverRegistered) {
                Class.forName(dbDiverName).newInstance();
                isDriverRegistered = true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
