package io.github.haiphamcoder.trino.client.config;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Configuration object for a Trino client session.
 * This class encapsulates all the settings needed to establish a connection
 * and execute queries on a Trino server.
 * 
 * <p>
 * Use the {@link Builder} class to create instances of ClientSession:
 * 
 * <pre>{@code
 * ClientSession session = ClientSession.builder()
 *         .server(URI.create("http://localhost:8080"))
 *         .user("admin")
 *         .catalog("hive")
 *         .schema("default")
 *         .property("query_max_run_time", "1h")
 *         .build();
 * }</pre>
 * 
 * @author Hai Pham Ngoc
 * @version 1.0.0
 */
public class ClientSession {
    /** Trino server URI */
    private URI server;
    /** User name for authentication */
    private String user;
    /** Client source identifier */
    private String source;
    /** Catalog name */
    private String catalog;
    /** Schema name */
    private String schema;
    /** Client tags for resource group selection */
    private Set<String> clientTags;
    /** Session properties */
    private Map<String, String> properties;
    /** User credentials for authentication */
    private Map<String, String> credentials;
    /** Prepared statements mapping */
    private Map<String, String> preparedStatements;
    /** Timezone for temporal operations */
    private String timeZone;
    /** Locale for localization */
    private String locale;
    /** Whether compression is disabled */
    private Boolean compressionDisabled;

    /**
     * Creates a new Builder for constructing a ClientSession.
     * 
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for creating ClientSession instances.
     * This class provides a fluent API for setting all session properties.
     */
    public static class Builder {
        private URI server;
        private String user;
        private String source = "trino-java-client";
        private String catalog;
        private String schema;
        private Set<String> clientTags = new HashSet<>();
        private Map<String, String> properties = new HashMap<>();
        private Map<String, String> credentials = new HashMap<>();
        private Map<String, String> preparedStatements = new HashMap<>();
        private String timeZone;
        private String locale;
        private Boolean compressionDisabled;

        /**
         * Sets the Trino server URI.
         * 
         * @param server the server URI
         * @return this builder
         */
        public Builder server(URI server) {
            this.server = server;
            return this;
        }

        /**
         * Sets the user name for authentication.
         * 
         * @param user the user name
         * @return this builder
         */
        public Builder user(String user) {
            this.user = user;
            return this;
        }

        /**
         * Sets the client source identifier (default: "trino-java-client").
         * 
         * @param source the source identifier
         * @return this builder
         */
        public Builder source(String source) {
            this.source = source;
            return this;
        }

        /**
         * Sets the catalog name.
         * 
         * @param catalog the catalog name
         * @return this builder
         */
        public Builder catalog(String catalog) {
            this.catalog = catalog;
            return this;
        }

        /**
         * Sets the schema name.
         * 
         * @param schema the schema name
         * @return this builder
         */
        public Builder schema(String schema) {
            this.schema = schema;
            return this;
        }

        /**
         * Adds a client tag for resource group selection.
         * 
         * @param tag the client tag
         * @return this builder
         */
        public Builder clientTag(String tag) {
            this.clientTags.add(tag);
            return this;
        }

        /**
         * Sets a session property.
         * 
         * @param key   the property key
         * @param value the property value
         * @return this builder
         */
        public Builder property(String key, String value) {
            this.properties.put(key, value);
            return this;
        }

        /**
         * Sets a user credential for authentication.
         * 
         * @param key   the credential key
         * @param value the credential value
         * @return this builder
         */
        public Builder credential(String key, String value) {
            this.credentials.put(key, value);
            return this;
        }

        /**
         * Adds a prepared statement mapping.
         * 
         * @param name      the statement name
         * @param statement the SQL statement
         * @return this builder
         */
        public Builder preparedStatement(String name, String statement) {
            this.preparedStatements.put(name, statement);
            return this;
        }

        /**
         * Sets the timezone for temporal operations (e.g., "UTC", "America/New_York").
         * 
         * @param timeZone the timezone string
         * @return this builder
         */
        public Builder timeZone(String timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        /**
         * Sets the locale for localization (e.g., "en_US", "fr_FR").
         * 
         * @param locale the locale string
         * @return this builder
         */
        public Builder locale(String locale) {
            this.locale = locale;
            return this;
        }

        /**
         * Enables or disables response compression.
         * 
         * @param compressionDisabled true to disable compression, false to enable it
         * @return this builder
         */
        public Builder compressionDisabled(boolean compressionDisabled) {
            this.compressionDisabled = compressionDisabled;
            return this;
        }

        /**
         * Builds and returns a new ClientSession with the configured properties.
         * 
         * @return a new ClientSession instance
         */
        public ClientSession build() {
            return new ClientSession(this);
        }
    }

    private ClientSession(Builder builder) {
        this.server = builder.server;
        this.user = builder.user;
        this.source = builder.source;
        this.catalog = builder.catalog;
        this.schema = builder.schema;
        this.clientTags = builder.clientTags;
        this.properties = builder.properties;
        this.credentials = builder.credentials;
        this.preparedStatements = builder.preparedStatements;
        this.timeZone = builder.timeZone;
        this.locale = builder.locale;
        this.compressionDisabled = builder.compressionDisabled;
    }

    public URI getServer() {
        return server;
    }

    public String getUser() {
        return user;
    }

    public String getSource() {
        return source;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    public Set<String> getClientTags() {
        return clientTags;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public Map<String, String> getCredentials() {
        return credentials;
    }

    public Map<String, String> getPreparedStatements() {
        return preparedStatements;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getLocale() {
        return locale;
    }

    public Boolean getCompressionDisabled() {
        return compressionDisabled;
    }
}
