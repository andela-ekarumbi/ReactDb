package checkpoint.andela.utility;

import checkpoint.andela.config.Constants;
import checkpoint.andela.db.DbRecord;

import java.sql.*;
import java.util.Arrays;
import java.util.Date;

public class Utility {

    private static Connection connection;

    private static Statement statement;

    public static boolean isValidColumn(String columnName) {
        Arrays.sort(Constants.ALLOWED_ATTRIBUTES);
        int foundPosition
                = Arrays.binarySearch(Constants.ALLOWED_ATTRIBUTES, columnName);
        return foundPosition >= 0;
    }

    public static String generateLogMessage(DbRecord record,
                                            String termination,
                                            String threadName) {
        String currentTime = (new Date()).toString();
        String recordUniqueId
                = record.getAllColumns().get("UNIQUE-ID").get(0);
        String currentThreadId = Long.toString(Thread.currentThread().getId());
        return threadName
                + " #"
                + currentThreadId
                + " at "
                + currentTime
                + ": Got UNIQUE-ID "
                + recordUniqueId
                + termination;
    }

    public static int getDbRecordCount() {
        String sql = "SELECT COUNT(*) FROM REACTIONS;";

        loadDbDrivers();

        loadResources();
        ResultSet resultSet = null;
        int count = 0;
        try {
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            count = resultSet.getInt("count(*)");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        finalizeResources();
        return count;
    }

    private static void finalizeResources() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void loadResources() {
        try {
            connection = DriverManager.getConnection(
                    Constants.MYSQL_URL,
                    Constants.MYSQL_USERNAME,
                    Constants.MYSQL_PASSWORD
            );
            statement = connection.createStatement();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void loadDbDrivers() {
        try {
            Class.forName(Constants.MYSQL_DRIVER_NAME);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
