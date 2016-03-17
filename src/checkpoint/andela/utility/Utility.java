package checkpoint.andela.utility;

import checkpoint.andela.config.Constants;
import checkpoint.andela.models.Reaction;

import java.sql.*;
import java.util.Date;

public class Utility {

    private static Connection connection;

    private static Statement statement;

    public static String generateLogMessage(Reaction reaction,
                                            String termination,
                                            String threadName) {
        String currentTime = (new Date()).toString();
        String recordUniqueId
                = reaction.getUniqueId();
        String currentThreadId = Long.toString(Thread.currentThread().getId());
        return threadName
                + " #"
                + currentThreadId
                + " on "
                + currentTime
                + ": Got UNIQUE-ID "
                + recordUniqueId
                + termination;
    }

    public static int getDbRecordCount() {
        String sql = "SELECT COUNT(*) FROM REACTIONS;";

        loadDbDrivers();

        loadResources();
        ResultSet resultSet;
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
