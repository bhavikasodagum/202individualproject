package com.logparser.parsers;

import com.logparser.logs.RequestLogEntry;
import com.logparser.logs.LogEntry;

/**
 * Parser for Request logs (e.g., HTTP methods, response statuses).
 */
public class RequestLogParser implements LogParser {

    private LogParser nextParser;  // The next parser in the chain

    @Override
    public LogEntry parse(String logLine) {
        // Check if the log line contains 'request_method=' to identify Request logs
        if (logLine.contains("request_method=")) {
            try {
                // Example: timestamp=2024-02-24T16:22:25Z request_method=POST request_url="/api/update" response_status=202 response_time_ms=200 host=webserver1
                String[] parts = logLine.split(" ");
                String requestMethod = parts[1].split("=")[1];  // Extract HTTP method (e.g., "POST")
                String requestUrl = parts[2].split("=")[1];  // Extract request URL (e.g., "/api/update")
                String responseStatus = parts[3].split("=")[1];  // Extract HTTP status (e.g., "202")
                double responseTimeMs = Double.parseDouble(parts[4].split("=")[1]);  // Extract response time (e.g., 200)
                String host = parts[5].split("=")[1];  // Extract host (e.g., "webserver1")

                // Return a RequestLogEntry object with the parsed data
                return new RequestLogEntry("timestamp", requestMethod, requestUrl, responseStatus, responseTimeMs, host);
            } catch (Exception e) {
                // Return null for invalid log lines
                return null;
            }
        }

        // If not a Request log, pass to the next parser
        return nextParser != null ? nextParser.parse(logLine) : null;
    }

    public void setNextParser(LogParser nextParser) {
        this.nextParser = nextParser;  // Set the next parser in the chain
    }
}
