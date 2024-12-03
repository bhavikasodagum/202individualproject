package com.logparser;

import com.logparser.logs.APMLogEntry;
import com.logparser.logs.ApplicationLogEntry;
import com.logparser.logs.LogEntry;
import com.logparser.parsers.APMLogParser;
import com.logparser.parsers.ApplicationLogParser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class APMLogParserTest {

    // Test valid APM log line with correct fields
    @Test
    void testValidAPMLogLine() {
        APMLogParser parser = new APMLogParser();
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72";

        APMLogEntry entry = (APMLogEntry) parser.parse(logLine);

        assertNotNull(entry);
        assertEquals("2024-02-24T16:22:15Z", entry.getTimestamp());
        assertEquals("cpu_usage_percent", entry.getMetric());
        assertEquals(72.0, entry.getValue());
        assertEquals("webserver1", entry.getHost());
    }

    // Test valid APM log line with extra fields (should still parse correctly)
    @Test
    void testAPMLogLineWithAdditionalFields() {
        APMLogParser parser = new APMLogParser();
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=disk_usage_percent mountpoint=/ host=webserver1 value=88";

        APMLogEntry entry = (APMLogEntry) parser.parse(logLine);

        assertNotNull(entry);
        assertEquals("2024-02-24T16:22:15Z", entry.getTimestamp());
        assertEquals("disk_usage_percent", entry.getMetric());
        assertEquals(88.0, entry.getValue());
        assertEquals("webserver1", entry.getHost());
    }

    // Test log line with missing value (should return null)
    @Test
    void testInvalidAPMLogLineMissingValue() {
        APMLogParser parser = new APMLogParser();
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1";

        LogEntry entry = parser.parse(logLine);

        assertNull(entry, "Log line without 'value' should return null");
    }

    // Test log line with missing metric (should return null)
    @Test
    void testInvalidAPMLogLineMissingMetric() {
        APMLogParser parser = new APMLogParser();
        String logLine = "timestamp=2024-02-24T16:22:15Z host=webserver1 value=72";

        LogEntry entry = parser.parse(logLine);

        assertNull(entry, "Log line missing 'metric' should return null");
    }

    // Test log line with invalid value type (non-numeric value, should return null)
    @Test
    void testInvalidAPMLogLineInvalidValue() {
        APMLogParser parser = new APMLogParser();
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=invalid";

        LogEntry entry = parser.parse(logLine);

        assertNull(entry, "Log line with invalid 'value' type should return null");
    }

    // Test for non-APM log line (should be passed to next parser or return null)
    @Test
    void testNonAPMLogLine() {
        APMLogParser parser = new APMLogParser();
        String logLine = "timestamp=2024-02-24T16:22:15Z level=INFO message=\"Sample log\" host=webserver1";

        LogEntry entry = parser.parse(logLine);

        assertNull(entry, "Non-APM log line should return null");
    }

    // Test for log line with incorrect format (missing fields, should return null)
    @Test
    void testInvalidAPMLogLineFormat() {
        APMLogParser parser = new APMLogParser();
        String logLine = "timestamp=2024-02-24T16:22:15Z host=webserver1";

        LogEntry entry = parser.parse(logLine);

        assertNull(entry, "Log line with insufficient fields should return null");
    }

    // Test for log line with unexpected field format (e.g., 'host' with wrong value, should still parse)
    @Test
    void testInvalidHostFieldFormat() {
        APMLogParser parser = new APMLogParser();
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=wrongHostFormat value=72";

        LogEntry entry = parser.parse(logLine);

        assertNotNull(entry);
        assertEquals("wrongHostFormat", ((APMLogEntry) entry).getHost());  // Cast to APMLogEntry
    }

    // Test for invalid log line with non-matching regex (unexpected log format, should return null)
    @Test
    void testInvalidLogLineWithIncorrectFormat() {
        APMLogParser parser = new APMLogParser();
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent value=1000";

        LogEntry entry = parser.parse(logLine);

        assertNull(entry, "Log line with missing 'host' should return null");
    }
// Test for valid log line with unexpected format (e.g., missing space between '=')

    // Test for invalid log line with missing timestamp (should return null)
    @Test
    void testInvalidLogLineWithMissingTimestamp() {
        APMLogParser parser = new APMLogParser();
        String logLine = "metric=cpu_usage_percent host=webserver1 value=72";

        LogEntry entry = parser.parse(logLine);

        assertNull(entry, "Log line with missing 'timestamp' should return null");
    }

    // Test for valid APM log line with special characters in host
    @Test
    void testValidAPMLogLineWithSpecialCharactersInHost() {
        APMLogParser parser = new APMLogParser();
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver@1 value=72";

        APMLogEntry entry = (APMLogEntry) parser.parse(logLine);

        assertNotNull(entry);
        assertEquals("webserver@1", entry.getHost());
    }

    // Test invalid log line with extra spaces or incorrect delimiters
    // Test invalid log line with extra spaces or incorrect delimiters

// Test invalid log line with extra spaces or incorrect delimiters
// Test invalid log line with extra spaces or incorrect delimiters





    // Test with missing 'metric' and 'host' but still valid 'value'
    @Test
    void testMissingMetricAndHost() {
        APMLogParser parser = new APMLogParser();
        String logLine = "timestamp=2024-02-24T16:22:15Z value=72";

        LogEntry entry = parser.parse(logLine);

        assertNull(entry, "Log line missing both 'metric' and 'host' should return null");
    }
    // Test for checking the setNextParser method in APMLogParser
// Test for checking the setNextParser method in APMLogParser
@Test
void testSetNextParser() {
    APMLogParser parser1 = new APMLogParser();
    ApplicationLogParser parser2 = new ApplicationLogParser();  // Another parser for chaining

    parser1.setNextParser(parser2);

    String logLine = "timestamp=2024-02-24T16:22:15Z level=INFO message=\"Scheduled maintenance starting\" host=webserver1";

    // The log line should be passed to the next parser, which is ApplicationLogParser
    LogEntry entry = parser1.parse(logLine);
    
    // Verify that the next parser (ApplicationLogParser) processes the log line
    assertNotNull(entry, "Log line should be passed to the next parser.");
    assertTrue(entry instanceof ApplicationLogEntry, "Next parser should process the Application log.");
}

// Test for passing a log line to the next parser in the chain
@Test
void testPassToNextParser() {
    APMLogParser parser1 = new APMLogParser();
    ApplicationLogParser parser2 = new ApplicationLogParser();

    // Set up chain of parsers
    parser1.setNextParser(parser2);

    String logLine = "timestamp=2024-02-24T16:22:15Z level=INFO message=\"Scheduled maintenance starting\" host=webserver1";

    // The log line should not match APMLogParser and will be passed to the next parser (ApplicationLogParser)
    LogEntry entry = parser1.parse(logLine);

    // Verify the next parser handles the log correctly
    assertNotNull(entry, "Log line should be processed by the next parser in the chain.");
    assertTrue(entry instanceof ApplicationLogEntry, "Log should be passed to ApplicationLogParser.");
}

}
