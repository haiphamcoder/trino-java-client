package io.github.haiphamcoder.trino.client.result;

import java.util.List;

import io.github.haiphamcoder.trino.client.exception.TrinoException;
import io.github.haiphamcoder.trino.client.model.QueryState;
import io.github.haiphamcoder.trino.client.model.StatementResponse;
import io.github.haiphamcoder.trino.client.model.TrinoColumn;
import io.github.haiphamcoder.trino.client.model.TrinoStats;
import io.github.haiphamcoder.trino.client.protocol.StatementClient;

/**
 * Represents a result set from a Trino query execution.
 * This class provides iteration over query results with automatic pagination
 * handling.
 * 
 * <p>
 * The result set must be closed after use to free resources:
 * 
 * <pre>{@code
 * try (TrinoResultSet resultSet = client.execute("SELECT * FROM table")) {
 *     while (resultSet.next()) {
 *         TrinoRow row = resultSet.getCurrentRow();
 *         // Process row
 *     }
 * }
 * }</pre>
 * 
 * @author Hai Pham Ngoc
 * @version 1.0.0
 */
public class TrinoResultSet implements AutoCloseable {
    private final StatementClient statementClient;
    private List<TrinoColumn> columns;
    private int currentPageIndex = -1;
    private List<List<Object>> currentPageData;
    private boolean hasNextPage = true;
    private boolean initialized = false;

    public TrinoResultSet(StatementClient statementClient) {
        this.statementClient = statementClient;
    }

    /**
     * Moves the cursor to the next row in the result set.
     * This method automatically fetches the next page of results when the current
     * page is exhausted.
     * 
     * @return true if there is a next row, false otherwise
     */
    public boolean next() {
        if (!initialized) {
            initialize();
        }

        currentPageIndex++;

        // If we've exhausted the current page, get the next page
        if (currentPageData != null && currentPageIndex >= currentPageData.size()) {
            if (hasNextPage && statementClient.getState() == QueryState.RUNNING) {
                StatementResponse response = statementClient.advance();

                if (response == null) {
                    hasNextPage = false;
                    return false;
                }

                currentPageData = response.getData();

                // If still no data, we're done
                if (currentPageData == null || currentPageData.isEmpty()) {
                    if (response.isLastPage()) {
                        hasNextPage = false;
                    }
                    return false;
                }

                currentPageIndex = 0;
            } else {
                return false;
            }
        }

        return currentPageData != null && currentPageIndex < currentPageData.size();
    }

    private void initialize() {
        StatementResponse response = statementClient.execute();

        columns = response.getColumns();
        currentPageData = response.getData();
        currentPageIndex = -1;
        hasNextPage = !response.isLastPage();
        initialized = true;
    }

    /**
     * Gets the current row in the result set.
     * This method should be called after calling next() and verifying it returned
     * true.
     * 
     * @return the current TrinoRow
     * @throws TrinoException if called before next() or when there is no current
     *                        row
     */
    public TrinoRow getCurrentRow() {
        if (currentPageIndex < 0 || currentPageData == null || currentPageIndex >= currentPageData.size()) {
            throw new TrinoException("No current row. Call next() first.");
        }

        return new TrinoRow(columns, currentPageData.get(currentPageIndex));
    }

    /**
     * Gets all columns in the result set.
     * This will trigger initialization if not already initialized.
     * 
     * @return a list of column metadata
     */
    public List<TrinoColumn> getColumns() {
        if (!initialized) {
            initialize();
        }
        return columns;
    }

    /**
     * Gets the query execution statistics.
     * 
     * @return the query statistics, or null if not available
     */
    public TrinoStats getStats() {
        StatementResponse response = statementClient.getCurrentResponse();
        return response != null ? response.getStats() : null;
    }

    /**
     * Gets the current state of the query execution.
     * 
     * @return the query state
     */
    public QueryState getState() {
        return statementClient.getState();
    }

    @Override
    public void close() {
        statementClient.close();
    }
}
