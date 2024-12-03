package com.logparser;

import com.logparser.logs.ApplicationLogEntry;
import com.logparser.handlers.ApplicationLogHandler;
import com.logparser.handlers.LogHandler;
import com.logparser.logs.APMLogEntry;
import com.logparser.logs.LogEntry;
import com.logparser.parsers.ApplicationLogParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationLogHandlerTest {

    private ApplicationLogHandler applicationLogHandler;
    private TestLogHandler nextHandler;

    @BeforeEach
    void setUp() {
        applicationLogHandler = new ApplicationLogHandler();
        nextHandler = new TestLogHandler();
        applicationLogHandler.setNextHandler(nextHandler);
    }

    @Test
    void constructor_shouldInitializeParser() throws NoSuchFieldException, IllegalAccessException {
        // Use reflection to access the 'parser' field
        Field parserField = ApplicationLogHandler.class.getSuperclass().getDeclaredField("parser");
        parserField.setAccessible(true);

        Object parser = parserField.get(applicationLogHandler);

        // Verify that the parser is of type ApplicationLogParser
        assertThat(parser).isInstanceOf(ApplicationLogParser.class);
    }

    @Test
    void handle_shouldProcessApplicationLogCorrectly() {
        // Given a valid Application log line
        String logLine = "timestamp=2024-12-01 level=INFO message=\"Test message\" host=localhost";

        // When
        LogEntry logEntry = applicationLogHandler.handle(logLine);

        // Then
        assertThat(logEntry).isInstanceOf(ApplicationLogEntry.class);
        ApplicationLogEntry appLogEntry = (ApplicationLogEntry) logEntry;
        assertThat(appLogEntry.getLevel()).isEqualTo("INFO");
        assertThat(appLogEntry.getMessage()).isEqualTo("Test message");
        assertThat(appLogEntry.getHost()).isEqualTo("localhost");
    }

    @Test
    void handle_shouldPassNonApplicationLogToNextHandler() {
        // Given a non-Application log line
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72";

        // When
        LogEntry logEntry = applicationLogHandler.handle(logLine);

        // Then
        assertThat(nextHandler.handledLogLine).isEqualTo(logLine);
        assertThat(logEntry).isInstanceOf(APMLogEntry.class);
    }

    @Test
    void handle_shouldReturnNullForInvalidLog() {
        // Given an invalid log line
        String logLine = "invalid log line";

        // When
        LogEntry logEntry = applicationLogHandler.handle(logLine);

        // Then
        assertThat(logEntry).isNull();
        assertThat(nextHandler.handledLogLine).isEqualTo(logLine);
    }

    @Test
    void handle_shouldReturnNullWhenNextHandlerIsNullAndLogIsUnhandled() {
        // Given
        String logLine = "invalid log line";
        applicationLogHandler.setNextHandler(null);

        // When
        LogEntry logEntry = applicationLogHandler.handle(logLine);

        // Then
        assertThat(logEntry).isNull();
    }

    @Test
    void setNextHandler_shouldSetNextHandlerCorrectly() throws NoSuchFieldException, IllegalAccessException {
        // Given a new handler
        TestLogHandler newHandler = new TestLogHandler();

        // Use reflection to verify the nextHandler field
        Field nextHandlerField = ApplicationLogHandler.class.getSuperclass().getDeclaredField("nextHandler");
        nextHandlerField.setAccessible(true);

        // When
        applicationLogHandler.setNextHandler(newHandler);

        // Then
        Object nextHandler = nextHandlerField.get(applicationLogHandler);
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

            // Simulate handling only valid APMLogEntry logs
            if (logLine.contains("metric=cpu_usage_percent")) {
                return new APMLogEntry("2024-02-24T16:22:15Z", "cpu_usage_percent", 72.0, "webserver1");
            }

            return null; // Return null for other log lines
        }
    }
}
