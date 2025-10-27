package io.github.haiphamcoder.trino.client.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents statistics information for a Trino query execution.
 * This class provides detailed metrics about query progress, resource usage,
 * and execution status returned by the Trino server.
 * 
 * @author Hai Pham Ngoc
 * @version 1.0.0
 */
public class TrinoStats {
    /** Current state of the query execution */
    @SerializedName("state")
    private String state;

    /** Whether the query is queued for execution */
    @SerializedName("queued")
    private Boolean queued;

    /** Whether the query has been scheduled */
    @SerializedName("scheduled")
    private Boolean scheduled;

    /** Number of nodes participating in the query execution */
    @SerializedName("nodes")
    private Integer nodes;

    /** Total number of splits for the query */
    @SerializedName("totalSplits")
    private Integer totalSplits;

    /** Number of splits currently queued */
    @SerializedName("queuedSplits")
    private Integer queuedSplits;

    /** Number of splits currently running */
    @SerializedName("runningSplits")
    private Integer runningSplits;

    /** Number of splits that have completed execution */
    @SerializedName("completedSplits")
    private Integer completedSplits;

    /** Total bytes processed by the query */
    @SerializedName("bytesProcessed")
    private Long bytesProcessed;

    /** Total rows processed by the query */
    @SerializedName("rowsProcessed")
    private Long rowsProcessed;

    /** Total elapsed time for the query in milliseconds */
    @SerializedName("elapsedTimeMillis")
    private Long elapsedTimeMillis;

    /** Time spent in queue in milliseconds */
    @SerializedName("queuedTimeMillis")
    private Long queuedTimeMillis;

    /** Cumulative user memory used by the query in bytes */
    @SerializedName("cumulativeUserMemory")
    private Double cumulativeUserMemory;

    /** Total CPU time used by the query across all nodes in milliseconds */
    @SerializedName("totalCpuTimeMillis")
    private Long totalCpuTimeMillis;

    /** Wall time for the query execution in milliseconds */
    @SerializedName("queryWallTimeMillis")
    private Long queryWallTimeMillis;

    /** Query execution progress as a percentage (0.0 to 100.0) */
    @SerializedName("progressPercentage")
    private Double progressPercentage;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getQueued() {
        return queued;
    }

    public void setQueued(Boolean queued) {
        this.queued = queued;
    }

    public Boolean getScheduled() {
        return scheduled;
    }

    public void setScheduled(Boolean scheduled) {
        this.scheduled = scheduled;
    }

    public Integer getNodes() {
        return nodes;
    }

    public void setNodes(Integer nodes) {
        this.nodes = nodes;
    }

    public Integer getTotalSplits() {
        return totalSplits;
    }

    public void setTotalSplits(Integer totalSplits) {
        this.totalSplits = totalSplits;
    }

    public Integer getQueuedSplits() {
        return queuedSplits;
    }

    public void setQueuedSplits(Integer queuedSplits) {
        this.queuedSplits = queuedSplits;
    }

    public Integer getRunningSplits() {
        return runningSplits;
    }

    public void setRunningSplits(Integer runningSplits) {
        this.runningSplits = runningSplits;
    }

    public Integer getCompletedSplits() {
        return completedSplits;
    }

    public void setCompletedSplits(Integer completedSplits) {
        this.completedSplits = completedSplits;
    }

    public Long getBytesProcessed() {
        return bytesProcessed;
    }

    public void setBytesProcessed(Long bytesProcessed) {
        this.bytesProcessed = bytesProcessed;
    }

    public Long getRowsProcessed() {
        return rowsProcessed;
    }

    public void setRowsProcessed(Long rowsProcessed) {
        this.rowsProcessed = rowsProcessed;
    }

    public Long getElapsedTimeMillis() {
        return elapsedTimeMillis;
    }

    public void setElapsedTimeMillis(Long elapsedTimeMillis) {
        this.elapsedTimeMillis = elapsedTimeMillis;
    }

    public Long getQueuedTimeMillis() {
        return queuedTimeMillis;
    }

    public void setQueuedTimeMillis(Long queuedTimeMillis) {
        this.queuedTimeMillis = queuedTimeMillis;
    }

    public Double getCumulativeUserMemory() {
        return cumulativeUserMemory;
    }

    public void setCumulativeUserMemory(Double cumulativeUserMemory) {
        this.cumulativeUserMemory = cumulativeUserMemory;
    }

    public Long getTotalCpuTimeMillis() {
        return totalCpuTimeMillis;
    }

    public void setTotalCpuTimeMillis(Long totalCpuTimeMillis) {
        this.totalCpuTimeMillis = totalCpuTimeMillis;
    }

    public Long getQueryWallTimeMillis() {
        return queryWallTimeMillis;
    }

    public void setQueryWallTimeMillis(Long queryWallTimeMillis) {
        this.queryWallTimeMillis = queryWallTimeMillis;
    }

    public Double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}
