package io.github.haiphamcoder.trino.client.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the response from a Trino statement execution.
 * This class contains query results, metadata, statistics, errors, and
 * pagination information.
 * 
 * @author Hai Pham Ngoc
 * @version 1.0.0
 */
public class StatementResponse {
    /** Unique identifier for the query */
    private String id;

    /** URI for query information and monitoring */
    @SerializedName("infoUri")
    private String infoUri;

    /** URI for fetching the next page of results (null if this is the last page) */
    @SerializedName("nextUri")
    private String nextUri;

    /** Column metadata describing the result set structure */
    private List<TrinoColumn> columns;

    /** Query result data as rows of objects */
    private List<List<Object>> data;

    /** Query execution statistics */
    private TrinoStats stats;

    /** Error information if the query failed */
    private TrinoError error;

    /** List of warnings issued during query execution */
    @SerializedName("warnings")
    private List<String> warnings;

    /** Type of update statement (for DDL/DML statements) */
    @SerializedName("updateType")
    private String updateType;

    /** Number of rows affected (for update/insert/delete statements) */
    @SerializedName("updateCount")
    private Long updateCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfoUri() {
        return infoUri;
    }

    public void setInfoUri(String infoUri) {
        this.infoUri = infoUri;
    }

    public String getNextUri() {
        return nextUri;
    }

    public void setNextUri(String nextUri) {
        this.nextUri = nextUri;
    }

    public List<TrinoColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<TrinoColumn> columns) {
        this.columns = columns;
    }

    public List<List<Object>> getData() {
        return data;
    }

    public void setData(List<List<Object>> data) {
        this.data = data;
    }

    public boolean hasData() {
        return data != null && !data.isEmpty();
    }

    public TrinoStats getStats() {
        return stats;
    }

    public void setStats(TrinoStats stats) {
        this.stats = stats;
    }

    public TrinoError getError() {
        return error;
    }

    public void setError(TrinoError error) {
        this.error = error;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public Long getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(Long updateCount) {
        this.updateCount = updateCount;
    }

    /**
     * Check if this is the last page
     */
    public boolean isLastPage() {
        return nextUri == null;
    }
}
