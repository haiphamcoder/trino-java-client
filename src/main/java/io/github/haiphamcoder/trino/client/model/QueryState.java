package io.github.haiphamcoder.trino.client.model;

/**
 * Enum representing the possible states of a Trino query.
 * This enum tracks the lifecycle of query execution from initialization to
 * completion.
 * 
 * @author Hai Pham Ngoc
 * @version 1.0.0
 */
public enum QueryState {
    /** Query is running (includes planning, queued, running, etc.) */
    RUNNING,
    /** Query has encountered a client-side error */
    CLIENT_ERROR,
    /** Query was aborted by the client */
    CLIENT_ABORTED,
    /** Query has finished on the server (successfully or failed) */
    FINISHED
}
