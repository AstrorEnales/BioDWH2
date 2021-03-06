package de.unibi.agbi.biodwh2.abdamed2.io.k2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * K2 format "Update" entry
 */
public class K2UEntry extends K2Entry {
    private Map<String, String> primaryKeys = new HashMap<>();
    private Map<String, String> updateProperties = new HashMap<>();

    K2UEntry(final EntryType type) {
        super(type);
    }

    @Override
    protected void parse(final String[] lines, final List<K2FEntry> fields) {
        for (int i = 0; i < lines.length; i++) {
            if (fields.get(i).isPrimaryKey())
                primaryKeys.put(fields.get(i).getIdentifier(), lines[i]);
            else
                updateProperties.put(fields.get(i).getIdentifier(), lines[i]);
        }
    }

    public Set<Map.Entry<String, String>> getPrimaryKeys() {
        return primaryKeys.entrySet();
    }

    public Set<Map.Entry<String, String>> getUpdateEntries() {
        return updateProperties.entrySet();
    }
}
