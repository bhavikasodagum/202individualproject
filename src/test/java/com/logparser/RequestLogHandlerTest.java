package com.logparser;

import com.logparser.handlers.LogHandler;
import com.logparser.handlers.RequestLogHandler;
import com.logparser.logs.ApplicationLogEntry;
import com.logparser.logs.RequestLogEntry;
import com.logparser.logs.LogEntry;
import com.logparser.parsers.RequestLogParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class RequestLogHandlerTest {

    private RequestLogHandler requestLogHandler;
    private TestLogHandler nextHandler;

    @BeforeEach
    void setUp() {
        requestLogHandler = new RequestLogHandler();
        nextHandler = new TestLogHandler();
        requestLogHandler.setNextHandler(nextHandler);
    }

    @Test
    void constructor_shouldInitializeParser() throws NoSuchFieldException, IllegalAccessException {
        // Use reflection to access the 'parser' field
        Field parserField = RequestLogHandler.class.getSuperclass().getDeclaredField("parser");
        parserField.setAccessible(true);

        Object parser = parserField.get(requestLogHandler);

        // Verify that the parser is of type RequestLogParser
        assertThat(parser).isInstanceOf(RequestLogParser.class);
    }

    @Test
    void handle_shouldProcessRequestLogCorrectly() {
        // Given a valid Request log line
        String logLine = "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=202 response_time_ms=200 host=webserver1";

        // When
        LogEntry logEntry = requestLogHandler.handle(logLine);

        // Then
        assertThat(logEntry).isInstanceOf(RequestLogEntry.class);
        RequestLogEntry requestLogEntry = (RequestLogEntry) logEntry;
        assertThat(requestLogEntry.getRequestMethod()).isEqualTo("POST");
        assertThat(requestLogEntry.getRequestUrl()).isEqualTo("/api/update");
        assertThat(requestLogEntry.getResponseStatus()).isEqualTo("202");
        assertThat(requestLogEntry.getResponseTimeMs()).isEqualTo(200);
        assertThat(requestLogEntry.getHost()).isEqualTo("webserver1");
    }

    @Test
    void handle_shouldPassNonRequestLogToNextHandler() {
        // Given a non-Request log line
        String logLine = "timestamp=2024-12-01 level=INFO message=\"Test message\" host=localhost";

        // When
        LogEntry logEntry = requestLogHandler.handle(logLine);

        // Then
        assertThat(nextHandler.handledLogLine).isEqualTo(logLine);
        assertThat(logEntry).isInstanceOf(ApplicationLogEntry.class);
    }

    @Test
    void handle_shouldReturnNullForInvalidLog() {
        // Given an invalid log line
        String logLine = "invalid log line";

        // When
        LogEntry logEntry = requestLogHandler.handle(logLine);

        // Then
        assertThat(logEntry).isNull();
        assertThat(nextHandler.handledLogLine).isEqualTo(logLine);
    }

    @Test
    void handle_shouldReturnNullWhenNextHandlerIsNullAndLogIsUnhandled() {
        // Given
        String logLine = "invalid log line";
        requestLogHandler.setNextHandler(null);

        // When
        LogEntry logEntry = requestLogHandler.handle(logLine);

        // Then
        assertThat(logEntry).isNull();
    }

    @Test
    void setNextHandler_shouldSetNextHandlerCorrectly() throws NoSuchFieldException, IllegalAccessException {
        // Given a new handler
        TestLogHandler newHandler = new TestLogHandler();

        // Use reflection to verify the nextHandler field
        Field nextHandlerField = RequestLogHandler.class.getSuperclass().getDeclaredField("nextHandler");
        nextHandlerField.setAccessible(true);

        // When
        requestLogHandler.setNextHandler(newHandler);

        // Then
        Object nextHandler = nextHandlerField.get(requestLogHandler);
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
                return new ApplicationLogEntry("2024-12-01", "INFO", "Test message", "localhost");
            }

            return null; // Return null for other log lines
        }
    }
}
