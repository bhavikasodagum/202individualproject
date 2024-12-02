package com.logparser.logs;

/**
 * Represents an Application log entry.
 */
public class ApplicationLogEntry implements LogEntry {
    private String timestamp;  // The timestamp of the log
    private String level;      // Log level (e.g., "INFO", "ERROR")
    private String message;    // Log message
    private String host;       // Hostname of the machine that generated the log

    // Constructor
    public ApplicationLogEntry(String timestamp, String level, String message, String host) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.host = host;
    }

    // Getters
    @Override
    public String getTimestamp() {
        return timestamp;
    }

    public String getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String getHost() {
        return host;
    }

    // Override toString for debugging
    @Override
    public String toString() {
        return "ApplicationLogEntry{" +
                "timestamp='" + timestamp + '\'' +
                ", level='" + level + '\'' +
                ", message='" + message + '\'' +
                ", host='" + host + '\'' +
                '}';
    }
}
