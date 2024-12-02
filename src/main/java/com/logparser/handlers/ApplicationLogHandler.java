package com.logparser.handlers;

import com.logparser.parsers.ApplicationLogParser;

/**
 * Handler for processing Application logs in the Chain of Responsibility.
 */
public class ApplicationLogHandler extends LogHandler {

    public ApplicationLogHandler() {
        this.parser = new ApplicationLogParser(); // Set specific parser for Application logs
    }
}
