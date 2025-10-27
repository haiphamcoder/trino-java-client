package io.github.haiphamcoder.trino.client.protocol;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.StatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import io.github.haiphamcoder.trino.client.config.ClientSession;
import io.github.haiphamcoder.trino.client.exception.QueryCancelledException;
import io.github.haiphamcoder.trino.client.exception.QueryFailedException;
import io.github.haiphamcoder.trino.client.exception.TrinoException;
import io.github.haiphamcoder.trino.client.model.QueryState;
import io.github.haiphamcoder.trino.client.model.StatementResponse;
import io.github.haiphamcoder.trino.client.model.TrinoError;
import io.github.haiphamcoder.trino.client.model.TrinoStats;

/**
 * Low-level client for communicating with Trino statement API.
 * This class handles HTTP requests/responses and state management for query execution.
 * 
 * @author Hai Pham Ngoc
 * @version 1.0.0
 */
public class StatementClient implements AutoCloseable {
    /** Logger for this class */
    private static final Logger log = LoggerFactory.getLogger(StatementClient.class);
    /** JSON parser for responses */
    private static final Gson gson = new Gson();

    /** Client session configuration */
    private final ClientSession session;
    /** SQL statement to execute */
    private final String statement;
    /** Current query state */
    private final AtomicReference<QueryState> state;
    /** Current statement response */
    private StatementResponse currentResponse;
    /** HTTP client for making requests */
    private final CloseableHttpClient httpClient;
    /** Whether this client has been closed */
    private boolean closed;

    public StatementClient(ClientSession session, String statement) {
        this.session = session;
        this.statement = statement;
        this.state = new AtomicReference<>(QueryState.RUNNING);
        this.httpClient = HttpClients.createDefault();
        this.closed = false;
    }

    /**
     * Execute the statement and get the first response
     */
    public StatementResponse execute() {
        if (closed) {
            throw new TrinoException("Client is closed");
        }

        URI uri = session.getServer().resolve("/v1/statement");
        HttpPost post = new HttpPost(uri);

        // Set headers
        setHeaders(post);

        // Set body
        post.setEntity(new StringEntity(statement, ContentType.TEXT_PLAIN.withCharset("UTF-8")));

        try {
            return httpClient.execute(post, response -> {
                int statusCode = response.getCode();

                if (statusCode >= 400) {
                    handleErrorResponse(response);
                }

                currentResponse = parseResponse(response);
                updateState();

                return currentResponse;
            });
        } catch (IOException e) {
            state.set(QueryState.CLIENT_ERROR);
            throw new TrinoException("Failed to execute statement", e);
        }
    }

    /**
     * Advance to the next page
     */
    public StatementResponse advance() {
        if (closed) {
            throw new TrinoException("Client is closed");
        }

        if (currentResponse == null) {
            throw new TrinoException("No current response. Call execute() first.");
        }

        if (currentResponse.isLastPage()) {
            state.set(QueryState.FINISHED);
            return currentResponse;
        }

        if (state.get() != QueryState.RUNNING) {
            return currentResponse;
        }

        String nextUri = currentResponse.getNextUri();
        if (nextUri == null) {
            state.set(QueryState.FINISHED);
            return currentResponse;
        }

        HttpGet get = new HttpGet(URI.create(nextUri));
        setHeaders(get);

        try {
            return httpClient.execute(get, response -> {
                int statusCode = response.getCode();

                if (statusCode >= 400) {
                    handleErrorResponse(response);
                }

                currentResponse = parseResponse(response);
                updateState();

                return currentResponse;
            });
        } catch (IOException e) {
            state.set(QueryState.CLIENT_ERROR);
            throw new TrinoException("Failed to advance query", e);
        }
    }

    private void setHeaders(org.apache.hc.core5.http.HttpRequest request) {
        request.setHeader("X-Trino-User", session.getUser());
        request.setHeader("X-Trino-Source", session.getSource());

        if (session.getCatalog() != null) {
            request.setHeader("X-Trino-Catalog", session.getCatalog());
        }

        if (session.getSchema() != null) {
            request.setHeader("X-Trino-Schema", session.getSchema());
        }

        if (!session.getClientTags().isEmpty()) {
            request.setHeader("X-Trino-Client-Tags", String.join(",", session.getClientTags()));
        }

        if (session.getTimeZone() != null) {
            request.setHeader("X-Trino-Time-Zone", session.getTimeZone());
        }

        if (session.getLocale() != null) {
            request.setHeader("X-Trino-Language", session.getLocale());
        }

        // Session properties
        for (Map.Entry<String, String> entry : session.getProperties().entrySet()) {
            request.setHeader("X-Trino-Session", entry.getKey() + "=" + entry.getValue());
        }

        // Extra credentials
        for (Map.Entry<String, String> entry : session.getCredentials().entrySet()) {
            request.setHeader("X-Trino-Extra-Credential", entry.getKey() + "=" + entry.getValue());
        }

        if (session.getCompressionDisabled() != null && session.getCompressionDisabled()) {
            request.setHeader("Accept-Encoding", "identity");
        }
    }

    private StatementResponse parseResponse(ClassicHttpResponse response) throws IOException {
        try {
            String content = EntityUtils.toString(response.getEntity());
            return gson.fromJson(content, StatementResponse.class);
        } catch (ParseException e) {
            throw new IOException("Failed to parse response", e);
        }
    }

    private void handleErrorResponse(ClassicHttpResponse response) throws IOException {
        StatementResponse errorResponse = parseResponse(response);
        TrinoError error = errorResponse.getError();

        if (error != null) {
            if ("USER_CANCELED".equals(error.getErrorName())) {
                state.set(QueryState.CLIENT_ABORTED);
                throw new QueryCancelledException(errorResponse.getId());
            } else {
                state.set(QueryState.FINISHED);
                throw new QueryFailedException(errorResponse.getId(), error);
            }
        }

        // If no error object, use status code
        StatusLine statusLine = new StatusLine(response);
        throw new TrinoException("HTTP error: " + statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
    }

    private void updateState() {
        if (currentResponse == null) {
            return;
        }

        TrinoError error = currentResponse.getError();
        if (error != null) {
            if ("USER_CANCELED".equals(error.getErrorName())) {
                state.set(QueryState.CLIENT_ABORTED);
            } else {
                state.set(QueryState.FINISHED);
            }
            return;
        }

        TrinoStats stats = currentResponse.getStats();
        if (stats != null && "FAILED".equals(stats.getState())) {
            state.set(QueryState.FINISHED);
        } else if (currentResponse.isLastPage()) {
            state.set(QueryState.FINISHED);
        } else {
            state.set(QueryState.RUNNING);
        }
    }

    public QueryState getState() {
        return state.get();
    }

    public StatementResponse getCurrentResponse() {
        return currentResponse;
    }

    public ClientSession getSession() {
        return session;
    }

    public String getStatement() {
        return statement;
    }

    @Override
    public void close() {
        if (!closed) {
            closed = true;
            try {
                httpClient.close();
            } catch (IOException e) {
                log.warn("Error closing HTTP client", e);
            }
        }
    }
}
