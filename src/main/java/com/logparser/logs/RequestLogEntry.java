package com.logparser.logs;

/**
 * Represents a Request log entry.
 */
public class RequestLogEntry implements LogEntry {
    private String timestamp;         // The timestamp of the log
    private String requestMethod;     // HTTP request method (e.g., "GET", "POST")
    private String requestUrl;        // Requested URL
    private String responseStatus;    // HTTP response status (e.g., "200", "404")
    private double responseTimeMs;    // Response time in milliseconds
    private String host;              // Hostname of the machine that generated the log

    // Constructor
    public RequestLogEntry(String timestamp, String requestMethod, String requestUrl,
                           String responseStatus, double responseTimeMs, String host) {
        if (timestamp == null || timestamp.isEmpty()) {
            throw new IllegalArgumentException("Timestamp cannot be null or empty");
        }
        if (requestMethod == null || requestMethod.isEmpty()) {
            throw new IllegalArgumentException("Request method cannot be null or empty");
        }
        if (requestUrl == null || requestUrl.isEmpty()) {
            throw new IllegalArgumentException("Request URL cannot be null or empty");
        }
        if (responseStatus == null || responseStatus.isEmpty()) {
            throw new IllegalArgumentException("Response status cannot be null or empty");
        }
        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("Host cannot be null or empty");
        }
        if (responseTimeMs < 0) {
            throw new IllegalArgumentException("Response time cannot be negative");
        }

        this.timestamp = timestamp;
        this.requestMethod = requestMethod;
        this.requestUrl = requestUrl;
        this.responseStatus = responseStatus;
        this.responseTimeMs = responseTimeMs;
        this.host = host;
    }

    // Getters
    @Override
    public String getTimestamp() {
        return timestamp;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public double getResponseTimeMs() {
        return responseTimeMs;
    }

    @Override
    public String getHost() {
        return host;
    }

    // Override toString for debugging
    @Override
    public String toString() {
        return "RequestLogEntry{" +
                "timestamp='" + timestamp + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", responseStatus='" + responseStatus + '\'' +
                ", responseTimeMs=" + responseTimeMs +
                ", host='" + host + '\'' +
                '}';
    }
}
