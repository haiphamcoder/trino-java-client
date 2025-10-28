# Trino Java Client

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.haiphamcoder/trino-client.svg)](https://search.maven.org/artifact/io.github.haiphamcoder/trino-client)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://adoptium.net/)

A lightweight, high-performance Java client library for executing SQL queries against [Trino](https://trino.io/) (formerly known as Presto SQL) servers. This library provides a simple and intuitive API for interacting with Trino clusters.

## Features

- 🚀 **Simple API**: Easy-to-use fluent builder pattern for client configuration
- 📊 **Streaming Results**: Automatic pagination handling for large result sets
- 🔧 **Type-Safe**: Type-safe value retrieval with generic type support
- ⚡ **Performance**: Efficient HTTP client implementation with Apache HttpClient 5
- 📝 **Comprehensive**: Full support for Trino features including session properties, timezone, locale, and more
- 🛡️ **Robust Error Handling**: Detailed error information with location tracking
- 📈 **Query Statistics**: Access to query execution statistics and progress information
- 🎯 **Zero Dependencies**: Minimal external dependencies (HttpClient5, Gson, SLF4J)

## Requirements

- Java 17 or higher
- Access to a Trino server

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.haiphamcoder</groupId>
    <artifactId>trino-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

Add the following dependency to your `build.gradle`:

```groovy
dependencies {
    implementation 'io.github.haiphamcoder:trino-client:1.0.0'
}
```

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("io.github.haiphamcoder:trino-client:1.0.0")
}
```

## Quick Start

### Basic Usage

```java
import io.github.haiphamcoder.trino.client.TrinoClient;
import io.github.haiphamcoder.trino.client.result.TrinoResultSet;
import io.github.haiphamcoder.trino.client.result.TrinoRow;

// Create a client
TrinoClient client = TrinoClient.builder()
        .server("http://localhost:8080")
        .user("admin")
        .catalog("hive")
        .schema("default")
        .build();

// Execute a query
try (TrinoResultSet resultSet = client.execute("SELECT name, age FROM users WHERE age > 18")) {
    while (resultSet.next()) {
        TrinoRow row = resultSet.getCurrentRow();
        
        // Access values by column name
        String name = row.getValue("name", String.class);
        Integer age = row.getValue("age", Integer.class);
        
        // Or by index
        String nameAlt = row.getValue(0, String.class);
        Integer ageAlt = row.getValue(1, Integer.class);
        
        System.out.println(name + " - " + age);
    }
}
```

### Using Query Execution Methods

The `TrinoClient` provides three methods for different use cases:

#### Execute Query with Multiple Rows

```java
try (TrinoResultSet resultSet = client.execute("SELECT * FROM table_name")) {
    while (resultSet.next()) {
        TrinoRow row = resultSet.getCurrentRow();
        // Process row
    }
}
```

#### Execute Query with Single Row

```java
TrinoRow row = client.executeQuery("SELECT COUNT(*) as total FROM users");
if (row != null) {
    Long total = row.getValue("total", Long.class);
    System.out.println("Total users: " + total);
}
```

#### Execute Update Statement

```java
client.executeUpdate("CREATE TABLE IF NOT EXISTS my_table (id INTEGER, name VARCHAR)");
client.executeUpdate("INSERT INTO my_table VALUES (1, 'John'), (2, 'Jane')");
```

## Advanced Configuration

### Session Properties

Configure session properties to customize query behavior:

```java
TrinoClient client = TrinoClient.builder()
        .server("http://localhost:8080")
        .user("admin")
        .catalog("hive")
        .schema("default")
        .property("query_max_run_time", "1h")
        .property("query_max_execution_time", "45m")
        .property("memory_limit", "2GB")
        .build();
```

### Timezone and Locale

```java
TrinoClient client = TrinoClient.builder()
        .server("http://localhost:8080")
        .user("admin")
        .catalog("hive")
        .schema("default")
        .timeZone("America/New_York")
        .locale("en_US")
        .build();
```

### Client Tags

Use client tags for resource group selection:

```java
TrinoClient client = TrinoClient.builder()
        .server("http://localhost:8080")
        .user("admin")
        .catalog("hive")
        .schema("default")
        .clientTag("production")
        .clientTag("critical")
        .build();
```

### Custom Source Identifier

```java
TrinoClient client = TrinoClient.builder()
        .server("http://localhost:8080")
        .user("admin")
        .catalog("hive")
        .schema("default")
        .source("my-custom-app")
        .build();
```

### Credentials

Add extra credentials for custom authentication:

```java
TrinoClient client = TrinoClient.builder()
        .server("http://localhost:8080")
        .user("admin")
        .catalog("hive")
        .schema("default")
        .credential("key", "value")
        .build();
```

### Disable Compression

```java
TrinoClient client = TrinoClient.builder()
        .server("http://localhost:8080")
        .user("admin")
        .catalog("hive")
        .schema("default")
        .compressionDisabled(true)
        .build();
```

## Working with Results

### Accessing Column Information

```java
import io.github.haiphamcoder.trino.client.model.TrinoColumn;

try (TrinoResultSet resultSet = client.execute("SELECT * FROM users")) {
    List<TrinoColumn> columns = resultSet.getColumns();
    
    for (TrinoColumn column : columns) {
        System.out.println("Column: " + column.getName() + ", Type: " + column.getType());
    }
    
    while (resultSet.next()) {
        TrinoRow row = resultSet.getCurrentRow();
        // Process row
    }
}
```

### Getting Query Statistics

```java
import io.github.haiphamcoder.trino.client.model.TrinoStats;

try (TrinoResultSet resultSet = client.execute("SELECT * FROM large_table")) {
    TrinoStats stats = resultSet.getStats();
    
    if (stats != null) {
        System.out.println("State: " + stats.getState());
        System.out.println("Rows processed: " + stats.getRowsProcessed());
        System.out.println("Bytes processed: " + stats.getBytesProcessed());
        System.out.println("Elapsed time: " + stats.getElapsedTimeMillis() + " ms");
        System.out.println("Progress: " + stats.getProgressPercentage() + "%");
    }
    
    while (resultSet.next()) {
        // Process rows
    }
}
```

## Error Handling

The library provides detailed error information through specialized exception classes:

```java
import io.github.haiphamcoder.trino.client.exception.QueryFailedException;
import io.github.haiphamcoder.trino.client.exception.QueryCancelledException;
import io.github.haiphamcoder.trino.client.exception.TrinoException;
import io.github.haiphamcoder.trino.client.model.TrinoError;

try {
    client.execute("SELECT * FROM non_existent_table");
} catch (QueryFailedException e) {
    System.err.println("Query failed: " + e.getMessage());
    TrinoError error = e.getTrinoError();
    if (error != null) {
        System.err.println("Error code: " + error.getErrorCode());
        System.err.println("Error type: " + error.getErrorType());
        if (error.getErrorLocation() != null) {
            System.err.println("Error at line " + error.getErrorLocation().getLineNumber() +
                             ", column " + error.getErrorLocation().getColumnNumber());
        }
    }
} catch (QueryCancelledException e) {
    System.err.println("Query was cancelled: " + e.getMessage());
} catch (TrinoException e) {
    System.err.println("Trino error: " + e.getMessage());
}
```

## Exception Classes

- **`TrinoException`**: Base exception for all Trino client errors
- **`QueryFailedException`**: Thrown when a query fails on the server
- **`QueryCancelledException`**: Thrown when a query is cancelled by the user

## Query State Management

Monitor query state during execution:

```java
try (TrinoResultSet resultSet = client.execute("SELECT * FROM large_table")) {
    QueryState state = resultSet.getState();
    
    switch (state) {
        case RUNNING:
            System.out.println("Query is running...");
            break;
        case FINISHED:
            System.out.println("Query completed successfully");
            break;
        case CLIENT_ERROR:
            System.out.println("Client-side error occurred");
            break;
        case CLIENT_ABORTED:
            System.out.println("Query was aborted");
            break;
    }
    
    while (resultSet.next()) {
        // Process rows
    }
}
```

## Type Support

The library supports all Trino data types through type-safe getters:

```java
TrinoRow row = client.executeQuery("SELECT col1, col2, col3, col4 FROM table");

String text = row.getValue("col1", String.class);
Long number = row.getValue("col2", Long.class);
Double decimal = row.getValue("col3", Double.class);
Boolean flag = row.getValue("col4", Boolean.class);
```

## Best Practices

### 1. Always Close Result Sets

Use try-with-resources to ensure proper resource cleanup:

```java
try (TrinoResultSet resultSet = client.execute("SELECT * FROM table")) {
    // Process results
} // Automatically closes resources
```

### 2. Handle Null Values

Check for null values when retrieving data:

```java
Integer value = row.getValue("column_name", Integer.class);
if (value != null) {
    // Process value
}
```

### 3. Reuse Client Instances

Create client instances once and reuse them for multiple queries:

```java
TrinoClient client = TrinoClient.builder()
        .server("http://localhost:8080")
        .user("admin")
        .catalog("hive")
        .schema("default")
        .build();

// Reuse for multiple queries
try (TrinoResultSet rs1 = client.execute("SELECT * FROM table1")) { ... }
try (TrinoResultSet rs2 = client.execute("SELECT * FROM table2")) { ... }
```

### 4. Handle Large Result Sets

For large result sets, the library automatically handles pagination:

```java
try (TrinoResultSet resultSet = client.execute("SELECT * FROM very_large_table")) {
    int count = 0;
    while (resultSet.next()) {
        TrinoRow row = resultSet.getCurrentRow();
        // Process row
        count++;
        
        if (count % 1000 == 0) {
            // Check progress periodically
            TrinoStats stats = resultSet.getStats();
            System.out.println("Processed " + count + " rows, progress: " + 
                             stats.getProgressPercentage() + "%");
        }
    }
}
```

### 5. Use Prepared Statements Pattern

For repeated queries with different parameters:

```java
// Build base SQL
String baseSql = "SELECT * FROM users WHERE age > ? AND city = ?";

// Note: The library doesn't currently support prepared statements directly,
// but you can construct parameterized queries using string formatting
String sql = String.format("SELECT * FROM users WHERE age > %d AND city = '%s'", 
                           minAge, city);
```

## Building from Source

Clone the repository and build using Maven:

```bash
git clone https://github.com/haiphamcoder/trino-java-client.git
cd trino-java-client
mvn clean install
```

## Testing

Run tests with Maven:

```bash
mvn test
```

Generate test coverage report:

```bash
mvn clean test jacoco:report
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Author

***Hai Pham Ngoc***

- Email: ngochai285nd@gmail.com
- GitHub: [@haiphamcoder](https://github.com/haiphamcoder)

## Acknowledgments

- [Trino](https://trino.io/) - The distributed SQL query engine
- [Apache HttpClient 5](https://hc.apache.org/httpcomponents-client-5.4.x/) - HTTP client library
- [Gson](https://github.com/google/gson) - JSON processing library

## Support

If you encounter any issues or have questions, please:

1. Check the [Issues](https://github.com/haiphamcoder/trino-java-client/issues) page
2. Create a new issue if your problem isn't already reported
3. Include relevant details about your environment and use case

## Changelog

### 1.0.0 (2025-10-28)

- Initial release
- Basic Trino client functionality
- Support for query execution and result iteration
- Session properties, timezone, locale configuration
- Comprehensive error handling
- Query statistics and progress tracking
