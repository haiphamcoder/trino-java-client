package io.github.haiphamcoder.trino.client.exception;

import io.github.haiphamcoder.trino.client.model.TrinoError;

/**
 * Base exception class for all Trino client exceptions.
 * This exception can contain detailed error information from the Trino server
 * including error codes, error types, and error locations.
 * 
 * @author Hai Pham Ngoc
 * @version 1.0.0
 */
public class TrinoException extends RuntimeException {
    /** Detailed error information from Trino server */
    private TrinoError trinoError;

    /**
     * Constructs a new TrinoException with the specified message.
     * 
     * @param message the detail message
     */
    public TrinoException(String message) {
        super(message);
    }

    /**
     * Constructs a new TrinoException with the specified message and Trino error details.
     * 
     * @param message the detail message
     * @param trinoError the Trino error details
     */
    public TrinoException(String message, TrinoError trinoError) {
        super(message);
        this.trinoError = trinoError;
    }

    /**
     * Constructs a new TrinoException with the specified message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public TrinoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new TrinoException with the specified message, Trino error details, and cause.
     * 
     * @param message the detail message
     * @param trinoError the Trino error details
     * @param cause the cause of the exception
     */
    public TrinoException(String message, TrinoError trinoError, Throwable cause) {
        super(message, cause);
        this.trinoError = trinoError;
    }

    /**
     * Gets the detailed error information from Trino server.
     * 
     * @return the TrinoError object containing error details, or null if not available
     */
    public TrinoError getTrinoError() {
        return trinoError;
    }
}
