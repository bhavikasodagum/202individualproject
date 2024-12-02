package com.logparser.logs;

import java.time.LocalDateTime;

/**
 * Represents an APM (Application Performance Metrics) log entry.
 */
public class APMLogEntry implements LogEntry {
    private String timestamp;  // The timestamp of the log
    private String metric;     // Metric type (e.g., "cpu_usage_percent")
    private double value;      // Metric value
    private String host;       // Hostname of the machine that generated the log

    // Constructor
    public APMLogEntry(String timestamp, String metric, double value, String host) {
        if (value < 0) {
            throw new IllegalArgumentException("Metric value cannot be negative.");
        }
        this.timestamp = timestamp;
        this.metric = metric;
        this.value = value;
        this.host = host;
    }

    // Getters
    @Override
    public String getTimestamp() {
        return timestamp;
    }

    public String getMetric() {
        return metric;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String getHost() {
        return host;
    }

    // Override toString for debugging
    @Override
    public String toString() {
        return "APMLogEntry{" +
                "timestamp='" + timestamp + '\'' +
                ", metric='" + metric + '\'' +
                ", value=" + value +
                ", host='" + host + '\'' +
                '}';
    }
}
