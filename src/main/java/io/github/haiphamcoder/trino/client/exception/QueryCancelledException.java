package io.github.haiphamcoder.trino.client.exception;

/**
 * Exception thrown when a query is cancelled.
 * This exception is raised when a query execution is explicitly cancelled
 * either by the user or by the client.
 * 
 * @author Hai Pham Ngoc
 * @version 1.0.0
 */
public class QueryCancelledException extends TrinoException {
    /**
     * Constructs a new QueryCancelledException with the specified query ID.
     * 
     * @param queryId the ID of the cancelled query
     */
    public QueryCancelledException(String queryId) {
        super("Query was cancelled: " + queryId);
    }
}
