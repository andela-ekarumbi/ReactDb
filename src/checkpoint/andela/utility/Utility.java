package checkpoint.andela.utility;

import checkpoint.andela.config.Constants;
import checkpoint.andela.db.DbRecord;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Utility {

   public static boolean isValidColumn(String columnName)  {
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
}
