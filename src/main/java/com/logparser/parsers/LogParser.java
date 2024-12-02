package com.logparser.parsers;

import com.logparser.logs.LogEntry;

/**
 * Interface for parsing log lines into LogEntry objects.
 * Implemented by all concrete parsers in the Chain of Responsibility.
 */
public interface LogParser {
    /**
     * Parses a log line and returns the corresponding LogEntry object.
     *
     * @param logLine The raw log line to parse.
     * @return A LogEntry object if the log line is successfully parsed; null otherwise.
     */
    LogEntry parse(String logLine);
}
