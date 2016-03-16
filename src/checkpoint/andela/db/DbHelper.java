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

    private String dbName;

    private Connection connection;

    private Statement statement;

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
                    String dbName,
                    String dbTableName) {

        this.dbDiverName = dbDriverName;
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbTableName = dbTableName;
        this.dbName = dbName;

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
            String sqlString = getInsertStatement(reactions);
            System.out.println(sqlString);

            initializeResources();

            statement.execute(sqlString);

            success = true;
        } catch (SQLException exception) {
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

    private String getInsertStatement(List<Reaction> reactions) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (Reaction reaction : reactions) {
                stringBuilder.append("\nINSERT INTO ")
                        .append("`")
                        .append(dbName)
                        .append("`.`")
                        .append(dbTableName)
                        .append("` (")
                        .append(getColumnNameString(reaction))
                        .append(") VALUES (")
                        .append(getColumnValuesString(reaction))
                        .append(");");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private String getColumnNameString(Reaction reaction) {
        StringBuilder stringBuilder = new StringBuilder();
        Field[] fields = getFieldsArray(reaction);

        int fieldLength = fields.length;

        for (int i = 0; i < fieldLength; i++) {
            Field field = fields[i];
            setFieldAccessible(field);
            stringBuilder.append("`")
                    .append(field.getName())
                    .append("`");
            if (i != fieldLength - 1) {
                stringBuilder.append(", ");
            }
        }

        return stringBuilder.toString();
    }

    private void setFieldAccessible(Field field) {
        field.setAccessible(true);
    }

    private Field[] getFieldsArray(Reaction reaction) {
        Class classObject = reaction.getClass();
        return classObject.getDeclaredFields();
    }

    private String getColumnValuesString(Reaction reaction)
            throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();

        Field[] fields = getFieldsArray(reaction);
        int fieldLength = fields.length;

        for (int i = 0; i < fieldLength; i++) {
            Field field = fields[i];
            setFieldAccessible(field);
            String columnValue = (String)field.get(reaction);
            stringBuilder.append("'")
                    .append(columnValue)
                    .append("'");
            if (i != fieldLength - 1) {
                stringBuilder.append(", ");
            }
        }

        return stringBuilder.toString();
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
