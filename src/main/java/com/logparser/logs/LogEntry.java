package com.logparser.logs;

/**
 * Represents a generic log entry.
 * All specific log entry types (APM, Application, Request) must implement this interface.
 */
public interface LogEntry {
    String getTimestamp();  // Get the timestamp of the log entry
    String getHost();       // Get the host that generated the log
}
