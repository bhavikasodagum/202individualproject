package com.logparser.handlers;

import com.logparser.parsers.RequestLogParser;

/**
 * Handler for processing Request logs in the Chain of Responsibility.
 */
public class RequestLogHandler extends LogHandler {

    public RequestLogHandler() {
        this.parser = new RequestLogParser(); // Set specific parser for Request logs
    }
}
