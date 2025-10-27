package io.github.haiphamcoder.trino.client.exception;

import io.github.haiphamcoder.trino.client.model.TrinoError;

/**
 * Exception thrown when a query execution fails on the Trino server.
 * This exception contains detailed error information including error codes,
 * error types, and error messages from the server.
 * 
 * @author Hai Pham Ngoc
 * @version 1.0.0
 */
public class QueryFailedException extends TrinoException {
    /**
     * Constructs a new QueryFailedException with the specified query ID and error details.
     * 
     * @param queryId the ID of the failed query
     * @param error the Trino error details from the server
     */
    public QueryFailedException(String queryId, TrinoError error) {
        super(String.format("Query failed: %s (Error: %s)", queryId, error != null ? error.getMessage() : "Unknown"),
                error);
    }
}
