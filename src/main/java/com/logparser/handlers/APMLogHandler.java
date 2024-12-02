package com.logparser.handlers;

import com.logparser.parsers.APMLogParser;

/**
 * Handler for processing APM logs in the Chain of Responsibility.
 */
public class APMLogHandler extends LogHandler {

    public APMLogHandler() {
        this.parser = new APMLogParser(); // Set specific parser for APM logs
    }
}
