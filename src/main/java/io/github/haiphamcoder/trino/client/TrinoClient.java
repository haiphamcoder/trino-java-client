package io.github.haiphamcoder.trino.client;

import java.net.URI;

import io.github.haiphamcoder.trino.client.config.ClientSession;
import io.github.haiphamcoder.trino.client.protocol.StatementClient;
import io.github.haiphamcoder.trino.client.result.TrinoResultSet;
import io.github.haiphamcoder.trino.client.result.TrinoRow;

/**
 * Main client class for executing queries against a Trino server.
 * This class provides a high-level API for query execution and result
 * retrieval.
 * 
 * <p>
 * Usage example:
 * 
 * <pre>{@code
 * TrinoClient client = TrinoClient.builder()
 *         .server("http://localhost:8080")
 *         .user("admin")
 *         .catalog("hive")
 *         .schema("default")
 *         .build();
 * 
 * TrinoResultSet resultSet = client.execute("SELECT * FROM table");
 * while (resultSet.next()) {
 *     TrinoRow row = resultSet.getCurrentRow();
 *     String value = row.getValue("column_name", String.class);
 * }
 * }</pre>
 * 
 * @author Hai Pham Ngoc
 * @version 1.0.0
 */
public class TrinoClient {
    /** Client session configuration */
    private final ClientSession session;

    /**
     * Constructs a new TrinoClient with the specified session configuration.
     * 
     * @param session the client session configuration
     */
    public TrinoClient(ClientSession session) {
        this.session = session;
    }

    /**
     * Executes a SQL query and returns a result set.
     * The result set must be closed after use to free resources.
     * 
     * @param sql the SQL statement to execute
     * @return a TrinoResultSet containing the query results
     */
    public TrinoResultSet execute(String sql) {
        StatementClient statementClient = new StatementClient(session, sql);
        return new TrinoResultSet(statementClient);
    }

    /**
     * Executes a query and returns the first row of results.
     * This is a convenience method for queries that return a single row.
     * 
     * @param sql the SQL statement to execute
     * @return the first row of results, or null if no rows are returned
     */
    public TrinoRow executeQuery(String sql) {
        try (TrinoResultSet resultSet = execute(sql)) {
            if (resultSet.next()) {
                return resultSet.getCurrentRow();
            }
            return null;
        }
    }

    /**
     * Executes a statement that returns no results (e.g., INSERT, UPDATE, DELETE,
     * CREATE).
     * This method consumes all pages of results to ensure the statement completes.
     * 
     * @param sql the SQL statement to execute
     */
    public void executeUpdate(String sql) {
        try (TrinoResultSet resultSet = execute(sql)) {
            // Consume all results
            while (resultSet.next()) {
                // Loop through all pages
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private URI server;
        private String user;
        private String source = "trino-java-client";
        private String catalog;
        private String schema;

        public Builder server(URI server) {
            this.server = server;
            return this;
        }

        public Builder server(String server) {
            this.server = URI.create(server);
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder catalog(String catalog) {
            this.catalog = catalog;
            return this;
        }

        public Builder schema(String schema) {
            this.schema = schema;
            return this;
        }

        public TrinoClient build() {
            ClientSession session = ClientSession.builder()
                    .server(server)
                    .user(user)
                    .source(source)
                    .catalog(catalog)
                    .schema(schema)
                    .build();
            return new TrinoClient(session);
        }
    }
}
