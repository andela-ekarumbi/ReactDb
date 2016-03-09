package checkpoint.andela.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbRecord {

    private Map<String, List<String>> columns;

    public DbRecord() {
        this.columns = new HashMap<>();
    }

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

    public Map<String, List<String>> getAllColumns() {
        return columns;
    }
}
