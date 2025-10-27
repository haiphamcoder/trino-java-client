package io.github.haiphamcoder.trino.client.result;

import io.github.haiphamcoder.trino.client.model.TrinoColumn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TrinoRow}.
 * 
 * @author Hai Pham Ngoc
 */
class TrinoRowTest {

    private List<TrinoColumn> columns;
    private List<Object> values;
    private TrinoRow row;

    @BeforeEach
    void setUp() {
        columns = new ArrayList<>();
        TrinoColumn col1 = new TrinoColumn();
        col1.setName("id");
        col1.setType("bigint");
        
        TrinoColumn col2 = new TrinoColumn();
        col2.setName("name");
        col2.setType("varchar");
        
        TrinoColumn col3 = new TrinoColumn();
        col3.setName("age");
        col3.setType("integer");
        
        columns.add(col1);
        columns.add(col2);
        columns.add(col3);
        
        values = new ArrayList<>();
        values.add(123L);
        values.add("John Doe");
        values.add(30);
        
        row = new TrinoRow(columns, values);
    }

    @Test
    @DisplayName("getValue by index should return correct value")
    void testGetValueByIndex() {
        assertEquals(123L, row.getValue(0));
        assertEquals("John Doe", row.getValue(1));
        assertEquals(30, row.getValue(2));
    }

    @Test
    @DisplayName("getValue by index should throw IndexOutOfBoundsException for negative index")
    void testGetValueByNegativeIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> row.getValue(-1));
    }

    @Test
    @DisplayName("getValue by index should throw IndexOutOfBoundsException for out of bounds index")
    void testGetValueByOutOfBoundsIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> row.getValue(10));
    }

    @Test
    @DisplayName("getValue by column name should return correct value (case-insensitive)")
    void testGetValueByColumnName() {
        assertEquals(123L, row.getValue("id"));
        assertEquals(123L, row.getValue("ID"));
        assertEquals(123L, row.getValue("Id"));
        assertEquals("John Doe", row.getValue("name"));
        assertEquals("John Doe", row.getValue("NAME"));
        assertEquals(30, row.getValue("age"));
    }

    @Test
    @DisplayName("getValue by column name should throw IllegalArgumentException for non-existent column")
    void testGetValueByNonExistentColumn() {
        assertThrows(IllegalArgumentException.class, () -> row.getValue("nonExistent"));
    }

    @Test
    @DisplayName("getValue by index with type should return correctly casted value")
    void testGetValueByIndexWithType() {
        Long id = row.getValue(0, Long.class);
        String name = row.getValue(1, String.class);
        Integer age = row.getValue(2, Integer.class);
        
        assertEquals(123L, id);
        assertEquals("John Doe", name);
        assertEquals(30, age);
    }

    @Test
    @DisplayName("getValue by index with type should return null for null values")
    void testGetValueByIndexWithTypeReturnsNull() {
        List<Object> nullValues = new ArrayList<>();
        nullValues.add(null);
        nullValues.add("test");
        
        List<TrinoColumn> col = new ArrayList<>();
        TrinoColumn c = new TrinoColumn();
        c.setName("col1");
        col.add(c);
        c = new TrinoColumn();
        c.setName("col2");
        col.add(c);
        
        TrinoRow testRow = new TrinoRow(col, nullValues);
        assertNull(testRow.getValue(0, String.class));
    }

    @Test
    @DisplayName("getValue by index with type should throw ClassCastException for incompatible type")
    void testGetValueByIndexWithIncompatibleType() {
        assertThrows(ClassCastException.class, () -> row.getValue(0, String.class));
    }

    @Test
    @DisplayName("getValue by column name with type should return correctly casted value")
    void testGetValueByColumnNameWithType() {
        Long id = row.getValue("id", Long.class);
        String name = row.getValue("name", String.class);
        Integer age = row.getValue("age", Integer.class);
        
        assertEquals(123L, id);
        assertEquals("John Doe", name);
        assertEquals(30, age);
    }

    @Test
    @DisplayName("getValue by column name with type should be case-insensitive")
    void testGetValueByColumnNameWithTypeCaseInsensitive() {
        Long id = row.getValue("ID", Long.class);
        String name = row.getValue("NAME", String.class);
        
        assertEquals(123L, id);
        assertEquals("John Doe", name);
    }

    @Test
    @DisplayName("getValues should return all values")
    void testGetValues() {
        List<Object> allValues = row.getValues();
        assertEquals(3, allValues.size());
        assertEquals(123L, allValues.get(0));
        assertEquals("John Doe", allValues.get(1));
        assertEquals(30, allValues.get(2));
    }

    @Test
    @DisplayName("getColumnCount should return correct number of columns")
    void testGetColumnCount() {
        assertEquals(3, row.getColumnCount());
    }

    @Test
    @DisplayName("getColumns should return all column metadata")
    void testGetColumns() {
        List<TrinoColumn> cols = row.getColumns();
        assertEquals(3, cols.size());
        assertEquals("id", cols.get(0).getName());
        assertEquals("name", cols.get(1).getName());
        assertEquals("age", cols.get(2).getName());
    }

    @Test
    @DisplayName("getValue with type should handle Number types correctly")
    void testGetValueWithNumberType() {
        Number age = row.getValue(2, Number.class);
        assertEquals(30, age.intValue());
    }
}

