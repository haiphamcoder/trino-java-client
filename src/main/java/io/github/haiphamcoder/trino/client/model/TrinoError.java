package io.github.haiphamcoder.trino.client.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents error information returned by the Trino server when a query fails.
 * This class includes error codes, error types, messages, and detailed location
 * information for debugging purposes.
 * 
 * @author Hai Pham Ngoc
 * @version 1.0.0
 */
public class TrinoError {
    /** Numeric error code returned by Trino */
    @SerializedName("errorCode")
    private Integer errorCode;

    /** Name of the error */
    @SerializedName("errorName")
    private String errorName;

    /** Type classification of the error */
    @SerializedName("errorType")
    private String errorType;

    /** Human-readable error message */
    private String message;

    /** Location in the query where the error occurred */
    @SerializedName("errorLocation")
    private ErrorLocation errorLocation;

    /** Additional failure information including stack traces */
    @SerializedName("failureInfo")
    private FailureInfo failureInfo;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorLocation getErrorLocation() {
        return errorLocation;
    }

    public void setErrorLocation(ErrorLocation errorLocation) {
        this.errorLocation = errorLocation;
    }

    public FailureInfo getFailureInfo() {
        return failureInfo;
    }

    public void setFailureInfo(FailureInfo failureInfo) {
        this.failureInfo = failureInfo;
    }

    /**
     * Represents the location in the SQL query where an error occurred.
     * This information helps identify the exact position of syntax or semantic
     * errors.
     */
    public static class ErrorLocation {
        /** Line number in the query where the error occurred (1-based) */
        @SerializedName("lineNumber")
        private Integer lineNumber;

        /** Column number in the query where the error occurred (1-based) */
        @SerializedName("columnNumber")
        private Integer columnNumber;

        public Integer getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(Integer lineNumber) {
            this.lineNumber = lineNumber;
        }

        public Integer getColumnNumber() {
            return columnNumber;
        }

        public void setColumnNumber(Integer columnNumber) {
            this.columnNumber = columnNumber;
        }
    }

    /**
     * Represents additional failure information including exception type
     * and detailed error messages from the server.
     */
    public static class FailureInfo {
        /** Type of the exception that caused the failure */
        private String type;
        /** Detailed failure message */
        private String message;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
