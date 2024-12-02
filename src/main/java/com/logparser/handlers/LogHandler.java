package com.logparser.handlers;

import com.logparser.logs.LogEntry;
import com.logparser.parsers.LogParser;

/**
 * Abstract handler for log processing in the Chain of Responsibility.
 */
public abstract class LogHandler {

    protected LogHandler nextHandler;
    protected LogParser parser;

    /**
     * Sets the next handler in the chain.
     *
     * @param nextHandler The next handler to process the log.
     */
    public void setNextHandler(LogHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * Processes a log line by passing it to the associated parser.
     * If the log line cannot be handled, it is passed to the next handler.
     *
     * @param logLine The log line to process.
     * @return A LogEntry object if the line is successfully parsed; null otherwise.
     */
    public LogEntry handle(String logLine) {
        LogEntry entry = parser.parse(logLine);
        if (entry != null) {
            return entry; // Successfully parsed
        }
        // Pass to the next handler if parsing failed
        return nextHandler != null ? nextHandler.handle(logLine) : null;
    }
}
