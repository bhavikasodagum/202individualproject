package com.logparser;
import com.logparser.handlers.APMLogHandler;
import com.logparser.handlers.LogHandler;
import com.logparser.logs.APMLogEntry;
import com.logparser.logs.ApplicationLogEntry;
import com.logparser.logs.LogEntry;
import com.logparser.parsers.APMLogParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class APMLogHandlerTest {

    private APMLogHandler apmLogHandler;
    private TestLogHandler nextHandler;

    @BeforeEach
    void setUp() {
        apmLogHandler = new APMLogHandler();
        nextHandler = new TestLogHandler();
        apmLogHandler.setNextHandler(nextHandler);
    }

    @Test
    void constructor_shouldInitializeParser() throws NoSuchFieldException, IllegalAccessException {
        // Use reflection to access the 'parser' field
        Field parserField = APMLogHandler.class.getSuperclass().getDeclaredField("parser");
        parserField.setAccessible(true);

        Object parser = parserField.get(apmLogHandler);

        // Verify that the parser is of type APMLogParser
        assertThat(parser).isInstanceOf(APMLogParser.class);
    }

    @Test
    void handle_shouldProcessAPMLogCorrectly() {
        // Given a valid APM log line
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72";

        // When
        LogEntry logEntry = apmLogHandler.handle(logLine);

        // Then
        assertThat(logEntry).isInstanceOf(APMLogEntry.class);
        APMLogEntry apmLogEntry = (APMLogEntry) logEntry;
        assertThat(apmLogEntry.getMetric()).isEqualTo("cpu_usage_percent");
        assertThat(apmLogEntry.getValue()).isEqualTo(72.0);
        assertThat(apmLogEntry.getHost()).isEqualTo("webserver1");
    }

    @Test
    void handle_shouldPassNonAPMLogToNextHandler() {
        // Given a non-APM log line
        String logLine = "timestamp=2024-02-24T16:22:15Z level=INFO message=\"Test log\" host=webserver1";

        // When
        LogEntry logEntry = apmLogHandler.handle(logLine);

        // Then
        assertThat(nextHandler.handledLogLine).isEqualTo(logLine);
        assertThat(logEntry).isInstanceOf(ApplicationLogEntry.class);
    }

    @Test
    void handle_shouldReturnNullForInvalidLog() {
        // Given an invalid log line
        String logLine = "invalid log line";

        // When
        LogEntry logEntry = apmLogHandler.handle(logLine);

        // Then
        assertThat(logEntry).isNull();
        assertThat(nextHandler.handledLogLine).isEqualTo(logLine);
    }

    @Test
    void handle_shouldReturnNullWhenNextHandlerIsNullAndLogIsUnhandled() {
        // Given
        String logLine = "invalid log line";
        apmLogHandler.setNextHandler(null);

        // When
        LogEntry logEntry = apmLogHandler.handle(logLine);

        // Then
        assertThat(logEntry).isNull();
    }

    @Test
    void setNextHandler_shouldSetNextHandlerCorrectly() throws NoSuchFieldException, IllegalAccessException {
        // Given a new handler
        TestLogHandler newHandler = new TestLogHandler();

        // Use reflection to verify the nextHandler field
        Field nextHandlerField = APMLogHandler.class.getSuperclass().getDeclaredField("nextHandler");
        nextHandlerField.setAccessible(true);

        // When
        apmLogHandler.setNextHandler(newHandler);

        // Then
        Object nextHandler = nextHandlerField.get(apmLogHandler);
        assertThat(nextHandler).isEqualTo(newHandler);
    }

    /**
     * A simple test-specific subclass of LogHandler to simulate behavior for testing.
     */
    private static class TestLogHandler extends LogHandler {
        String handledLogLine;

        @Override
        public LogEntry handle(String logLine) {
            this.handledLogLine = logLine;

            // Simulate handling only valid ApplicationLogEntry logs
            if (logLine.contains("level=INFO")) {
                return new ApplicationLogEntry("2024-02-24T16:22:15Z", "INFO", "Test log", "webserver1");
            }

            return null; // Return null for other log lines
        }
    }
}
