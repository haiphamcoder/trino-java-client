package io.github.haiphamcoder.trino.client.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.haiphamcoder.trino.client.model.TrinoColumn;

/**
 * Represents a single row in a Trino query result set.
 * This class provides convenient methods to access column values by index or name,
 * with support for type-safe value retrieval.
 * 
 * @author Hai Pham Ngoc
 * @version 1.0.0
 */
public class TrinoRow {
    /** Column metadata for this row */
    private final List<TrinoColumn> columns;
    /** Values for all columns in this row */
    private final List<Object> values;
    /** Map of column names to their indices for fast lookup */
    private final Map<String, Integer> columnIndexMap;

    /**
     * Constructs a new TrinoRow with the specified columns and values.
     * 
     * @param columns the column metadata
     * @param values the values for all columns
     */
    public TrinoRow(List<TrinoColumn> columns, List<Object> values) {
        this.columns = columns;
        this.values = values;
        this.columnIndexMap = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            columnIndexMap.put(columns.get(i).getName().toLowerCase(), i);
        }
    }

    /**
     * Gets the value at the specified column index.
     * 
     * @param index the column index (0-based)
     * @return the value at the specified index
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public Object getValue(int index) {
        if (index < 0 || index >= values.size()) {
            throw new IndexOutOfBoundsException("Column index out of bounds: " + index);
        }
        return values.get(index);
    }

    /**
     * Gets the value for the specified column name (case-insensitive).
     * 
     * @param columnName the name of the column
     * @return the value for the specified column
     * @throws IllegalArgumentException if the column name is not found
     */
    public Object getValue(String columnName) {
        Integer index = columnIndexMap.get(columnName.toLowerCase());
        if (index == null) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
        return values.get(index);
    }

    /**
     * Gets the value at the specified column index and casts it to the specified type.
     * 
     * @param <T> the type to cast to
     * @param index the column index (0-based)
     * @param type the class representing the desired type
     * @return the value cast to the specified type, or null if the value is null
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws ClassCastException if the value cannot be cast to the specified type
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(int index, Class<T> type) {
        Object value = getValue(index);
        if (value == null) {
            return null;
        }
        if (type.isInstance(value)) {
            return (T) value;
        }
        throw new ClassCastException("Cannot cast " + value.getClass() + " to " + type);
    }

    /**
     * Gets the value for the specified column name and casts it to the specified type.
     * 
     * @param <T> the type to cast to
     * @param columnName the name of the column
     * @param type the class representing the desired type
     * @return the value cast to the specified type, or null if the value is null
     * @throws IllegalArgumentException if the column name is not found
     * @throws ClassCastException if the value cannot be cast to the specified type
     */
    public <T> T getValue(String columnName, Class<T> type) {
        Object value = getValue(columnName);
        if (value == null) {
            return null;
        }
        if (type.isInstance(value)) {
            @SuppressWarnings("unchecked")
            T result = (T) value;
            return result;
        }
        throw new ClassCastException("Cannot cast " + value.getClass() + " to " + type);
    }

    /**
     * Gets all values in this row as a list.
     * 
     * @return a list of all column values
     */
    public List<Object> getValues() {
        return values;
    }

    /**
     * Gets the number of columns in this row.
     * 
     * @return the number of columns
     */
    public int getColumnCount() {
        return values.size();
    }

    /**
     * Gets the column metadata for this row.
     * 
     * @return a list of TrinoColumn objects containing column information
     */
    public List<TrinoColumn> getColumns() {
        return columns;
    }
}
