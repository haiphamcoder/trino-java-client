package io.github.haiphamcoder.trino.client.result;

import io.github.haiphamcoder.trino.client.exception.TrinoException;
import io.github.haiphamcoder.trino.client.model.QueryState;
import io.github.haiphamcoder.trino.client.model.StatementResponse;
import io.github.haiphamcoder.trino.client.model.TrinoColumn;
import io.github.haiphamcoder.trino.client.protocol.StatementClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

/**
 * Unit tests for {@link TrinoResultSet}.
 * 
 * @author Hai Pham Ngoc
 */
@ExtendWith(MockitoExtension.class)
class TrinoResultSetTest {

    @Mock
    private StatementClient statementClient;

    private List<TrinoColumn> columns;
    private StatementResponse firstResponse;
    private StatementResponse secondResponse;

    @BeforeEach
    void setUp() {
        // Setup columns
        columns = new ArrayList<>();
        TrinoColumn col = new TrinoColumn();
        col.setName("id");
        col.setType("bigint");
        columns.add(col);

        col = new TrinoColumn();
        col.setName("name");
        col.setType("varchar");
        columns.add(col);

        // Setup first response
        firstResponse = new StatementResponse();
        firstResponse.setColumns(columns);
        List<List<Object>> firstData = new ArrayList<>();
        firstData.add(List.of(1L, "Alice"));
        firstData.add(List.of(2L, "Bob"));
        firstResponse.setData(firstData);
        firstResponse.setNextUri("http://example.com/next");

        // Setup second response
        secondResponse = new StatementResponse();
        secondResponse.setColumns(columns);
        List<List<Object>> secondData = new ArrayList<>();
        secondData.add(List.of(3L, "Charlie"));
        secondResponse.setData(secondData);
        secondResponse.setNextUri(null); // Last page
    }

    @Test
    @DisplayName("next should initialize and return first row on first call")
    void testNextInitializesAndReturnsFirstRow() {
        lenient().when(statementClient.getState()).thenReturn(QueryState.RUNNING);
        when(statementClient.execute()).thenReturn(firstResponse);

        TrinoResultSet resultSet = new TrinoResultSet(statementClient);

        assertTrue(resultSet.next());
        assertEquals(1L, resultSet.getCurrentRow().getValue(0));
        verify(statementClient, times(1)).execute();
    }

    @Test
    @DisplayName("next should return all rows from single page")
    void testNextReturnsAllRowsFromSinglePage() {
        StatementResponse singlePageResponse = new StatementResponse();
        singlePageResponse.setColumns(columns);
        List<List<Object>> data = new ArrayList<>();
        data.add(List.of(1L, "Alice"));
        data.add(List.of(2L, "Bob"));
        data.add(List.of(3L, "Charlie"));
        singlePageResponse.setData(data);
        singlePageResponse.setNextUri(null);

        when(statementClient.execute()).thenReturn(singlePageResponse);
        lenient().when(statementClient.getState()).thenReturn(QueryState.RUNNING);

        TrinoResultSet resultSet = new TrinoResultSet(statementClient);

        int count = 0;
        while (resultSet.next()) {
            count++;
            resultSet.getCurrentRow();
        }

        assertEquals(3, count);
    }

    @Test
    @DisplayName("next should fetch next page when current page is exhausted")
    void testNextFetchesNextPage() {
        when(statementClient.execute()).thenReturn(firstResponse);
        when(statementClient.advance()).thenReturn(secondResponse);
        when(statementClient.getState()).thenReturn(QueryState.RUNNING);

        TrinoResultSet resultSet = new TrinoResultSet(statementClient);

        // Read first 2 rows from first page
        assertTrue(resultSet.next());
        assertEquals("Alice", resultSet.getCurrentRow().getValue(1));
        
        assertTrue(resultSet.next());
        assertEquals("Bob", resultSet.getCurrentRow().getValue(1));

        // Should fetch next page and return first row
        assertTrue(resultSet.next());
        assertEquals("Charlie", resultSet.getCurrentRow().getValue(1));

        verify(statementClient, times(1)).advance();
    }

    @Test
    @DisplayName("next should return false when no more rows")
    void testNextReturnsFalseWhenNoMoreRows() {
        StatementResponse emptyResponse = new StatementResponse();
        emptyResponse.setColumns(columns);
        emptyResponse.setData(new ArrayList<>());
        emptyResponse.setNextUri(null);

        when(statementClient.execute()).thenReturn(emptyResponse);

        TrinoResultSet resultSet = new TrinoResultSet(statementClient);

        assertFalse(resultSet.next());
    }

    @Test
    @DisplayName("getCurrentRow should throw exception when called before next")
    void testGetCurrentRowThrowsBeforeNext() {
        TrinoResultSet resultSet = new TrinoResultSet(statementClient);

        assertThrows(TrinoException.class, () -> resultSet.getCurrentRow());
    }

    @Test
    @DisplayName("getCurrentRow should throw exception when no current row")
    void testGetCurrentRowThrowsWhenNoCurrentRow() {
        StatementResponse emptyResponse = new StatementResponse();
        emptyResponse.setColumns(columns);
        emptyResponse.setData(new ArrayList<>());
        emptyResponse.setNextUri(null);

        when(statementClient.execute()).thenReturn(emptyResponse);

        TrinoResultSet resultSet = new TrinoResultSet(statementClient);
        resultSet.next(); // Returns false

        assertThrows(TrinoException.class, () -> resultSet.getCurrentRow());
    }

    @Test
    @DisplayName("getColumns should initialize and return columns")
    void testGetColumnsInitializes() {
        when(statementClient.execute()).thenReturn(firstResponse);

        TrinoResultSet resultSet = new TrinoResultSet(statementClient);
        List<TrinoColumn> cols = resultSet.getColumns();

        assertNotNull(cols);
        assertEquals(2, cols.size());
        assertEquals("id", cols.get(0).getName());
        verify(statementClient, times(1)).execute();
    }

    @Test
    @DisplayName("getStats should return query statistics")
    void testGetStats() {
        StatementResponse response = new StatementResponse();
        response.setColumns(columns);
        response.setData(new ArrayList<>());

        lenient().when(statementClient.execute()).thenReturn(response);
        when(statementClient.getCurrentResponse()).thenReturn(response);

        TrinoResultSet resultSet = new TrinoResultSet(statementClient);
        resultSet.getStats();

        verify(statementClient, times(1)).getCurrentResponse();
    }

    @Test
    @DisplayName("getState should return query state")
    void testGetState() {
        when(statementClient.getState()).thenReturn(QueryState.FINISHED);

        TrinoResultSet resultSet = new TrinoResultSet(statementClient);
        QueryState state = resultSet.getState();

        assertEquals(QueryState.FINISHED, state);
    }

    @Test
    @DisplayName("close should close statement client")
    void testClose() throws Exception {
        TrinoResultSet resultSet = new TrinoResultSet(statementClient);
        resultSet.close();

        verify(statementClient, times(1)).close();
    }
}

