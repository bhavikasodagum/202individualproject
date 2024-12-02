package com.logparser.parsers;

import com.logparser.logs.APMLogEntry;
import com.logparser.logs.LogEntry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for APM (Application Performance Metrics) logs.
 */
public class APMLogParser implements LogParser {

    private LogParser nextParser;  // The next parser in the chain

    @Override
    public LogEntry parse(String logLine) {
        // Check if the log line contains 'metric=' to identify APM logs
        if (logLine.contains("metric=")) {
            try {
                // Use regex to extract only the required fields
                Pattern pattern = Pattern.compile(
                        "timestamp=(\\S+) metric=(\\S+) .*?host=(\\S+) .*?value=(\\S+)"
                );
                Matcher matcher = pattern.matcher(logLine);

                if (matcher.find()) {
                    String timestamp = matcher.group(1); // Extract timestamp
                    String metric = matcher.group(2);    // Extract metric
                    String host = matcher.group(3);      // Extract host
                    double value = Double.parseDouble(matcher.group(4)); // Extract value

                    // Return an APMLogEntry object with the parsed data
                    return new APMLogEntry(timestamp, metric, value, host);
                } else {
                    throw new IllegalArgumentException("Invalid APM log format.");
                }
            } catch (Exception e) {
                // Log the error for debugging
                System.err.println("Error parsing APM log line: " + logLine + " - " + e.getMessage());
                // Return null for invalid log lines
                return null;
            }
        }

        // If not an APM log, pass to the next parser
        return nextParser != null ? nextParser.parse(logLine) : null;
    }

    public void setNextParser(LogParser nextParser) {
        this.nextParser = nextParser;  // Set the next parser in the chain
    }
}
