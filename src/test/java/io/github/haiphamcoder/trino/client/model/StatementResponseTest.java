package io.github.haiphamcoder.trino.client.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link StatementResponse}.
 * 
 * @author Hai Pham Ngoc
 */
class StatementResponseTest {

    @Test
    @DisplayName("hasData should return false when data is null")
    void testHasData_WithNullData() {
        StatementResponse response = new StatementResponse();
        response.setData(null);

        assertFalse(response.hasData());
    }

    @Test
    @DisplayName("hasData should return false when data is empty")
    void testHasData_WithEmptyData() {
        StatementResponse response = new StatementResponse();
        response.setData(new ArrayList<>());

        assertFalse(response.hasData());
    }

    @Test
    @DisplayName("hasData should return true when data contains rows")
    void testHasData_WithData() {
        StatementResponse response = new StatementResponse();
        List<List<Object>> data = new ArrayList<>();
        data.add(List.of("value1", "value2"));
        response.setData(data);

        assertTrue(response.hasData());
    }

    @Test
    @DisplayName("isLastPage should return true when nextUri is null")
    void testIsLastPage_WithNullNextUri() {
        StatementResponse response = new StatementResponse();
        response.setNextUri(null);

        assertTrue(response.isLastPage());
    }

    @Test
    @DisplayName("isLastPage should return false when nextUri is not null")
    void testIsLastPage_WithNextUri() {
        StatementResponse response = new StatementResponse();
        response.setNextUri("http://example.com/next");

        assertFalse(response.isLastPage());
    }

    @Test
    @DisplayName("isLastPage should return false when nextUri is empty string")
    void testIsLastPage_WithEmptyNextUri() {
        StatementResponse response = new StatementResponse();
        response.setNextUri("");

        assertFalse(response.isLastPage());
    }
}
