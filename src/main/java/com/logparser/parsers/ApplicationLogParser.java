package com.logparser.parsers;

import com.logparser.logs.ApplicationLogEntry;
import com.logparser.logs.LogEntry;

/**
 * Parser for Application logs (e.g., ERROR, INFO, DEBUG).
 */
public class ApplicationLogParser implements LogParser {

    private LogParser nextParser;  // The next parser in the chain

    @Override
    public LogEntry parse(String logLine) {
        // Check if the log line contains 'level=' to identify Application logs
        if (logLine.contains("level=")) {
            try {
                // Example: timestamp=2024-02-24T16:22:20Z level=INFO message="Scheduled maintenance starting" host=webserver1
                String[] parts = logLine.split(" ");
                String timestamp = parts[0].split("=")[1];  // Extract timestamp
                String level = parts[1].split("=")[1];      // Extract log level (e.g., "INFO")
                String message = extractMessage(logLine);   // Extract message with spaces
                String host = parts[parts.length - 1].split("=")[1];  // Extract host (last field)

                // Return an ApplicationLogEntry object with the parsed data
                return new ApplicationLogEntry(timestamp, level, message, host);
            } catch (Exception e) {
                // Log the error for debugging
                System.err.println("Error parsing Application log line: " + logLine + " - " + e.getMessage());
                // Return null for invalid log lines
                return null;
            }
        }

        // If not an Application log, pass to the next parser
        return nextParser != null ? nextParser.parse(logLine) : null;
    }

    /**
     * Helper method to extract the message field from the log line.
     *
     * @param logLine The full log line.
     * @return The extracted message.
     */
    private String extractMessage(String logLine) {
        // Find the start and end positions of the message field
        int messageStart = logLine.indexOf("message=") + 8; // Start after "message="
        int messageEnd = logLine.lastIndexOf("host=") - 1;  // End before "host="
        return logLine.substring(messageStart, messageEnd).replace("\"", ""); // Remove quotes
    }

    public void setNextParser(LogParser nextParser) {
        this.nextParser = nextParser;  // Set the next parser in the chain
    }
}
