package checkpoint.andela.utility;

import checkpoint.andela.config.Constants;
import checkpoint.andela.db.DbRecord;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Utility {

    public static void removeIllegalAttributes(DbRecord record){
        Map<String, List<String>> columns = record.getAllColumns();

        for (String columnName : columns.keySet()) {
            if (findColumnName(columnName) == -1) {
                columns.remove(columnName);
            }
        }
    }

    private static int findColumnName(String columnName)  {
        Arrays.sort(Constants.ALLOWED_ATTRIBUTES);
        return Arrays.binarySearch(Constants.ALLOWED_ATTRIBUTES, columnName);
    }
}
