package checkpoint.andela.utility;

import checkpoint.andela.config.Constants;
import checkpoint.andela.db.DbRecord;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Utility {
    public static boolean isValidColumn(String columnName)  {
        Arrays.sort(Constants.ALLOWED_ATTRIBUTES);
        int foundPosition
                = Arrays.binarySearch(Constants.ALLOWED_ATTRIBUTES, columnName);
        return foundPosition >= 0;
    }
}
