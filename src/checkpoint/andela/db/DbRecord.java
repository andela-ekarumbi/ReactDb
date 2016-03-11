/**
 * This class provides a generic implementation of a database record. It
 * assumes that all the fields will contain strings and that each field may
 * store zero or more values.
 * */

package checkpoint.andela.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbRecord {

    private Map<String, List<String>> columns;

    /**
     * Creates a new {@code DbRecord}.
     * */

    public DbRecord() {
        this.columns = new HashMap<>();
    }

    /**
     * Finds a column with the given name and adds the given value to that
     * column, creating the column if it does not exist.
     * @param columnName the name of the column to be created or appended to.
     * @param columnValue the value to be added to the column.
     * @return true for a successful operation, false otherwise.
     * */

    public boolean addColumn(String columnName, String columnValue) {
        columnName = columnName.replaceAll("\\?", "");
        List<String> existingEntries = columns.get(columnName);

        if (existingEntries == null) {
            existingEntries = new ArrayList<>();
        }

        existingEntries.add(columnValue);
        columns.put(columnName, existingEntries);

        return true;
    }

    /**
     * Returns a name-value(s) collection containing the data in this record.
     * @return the data in this record as a name-value collection.
     * */

    public Map<String, List<String>> getAllColumns() {
        return columns;
    }
}
