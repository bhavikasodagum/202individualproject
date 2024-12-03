package com.logparser;

import com.logparser.logs.RequestLogEntry;
import com.logparser.parsers.LogParser;
import com.logparser.parsers.RequestLogParser;
import com.logparser.parsers.LogParser;
import com.logparser.logs.LogEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RequestLogParserTest {

    private static final String VALID_LOG = "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=202 response_time_ms=200 host=webserver1";
    private static final String INVALID_LOG = "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_time_ms=200 host=webserver1";
    private static final String EMPTY_LOG = "";
    private static final String NULL_LOG = null;

    @Mock
    private LogParser nextParser;

    @InjectMocks
    private RequestLogParser requestLogParser;

    // Test for valid log parsing
    @Test
    void givenValidLog_whenParse_thenReturnRequestLogEntry() {
        // when
        LogEntry logEntry = requestLogParser.parse(VALID_LOG);

        // then
        assertThat(logEntry).isInstanceOf(RequestLogEntry.class);
        RequestLogEntry entry = (RequestLogEntry) logEntry;
        assertThat(entry.getRequestMethod()).isEqualTo("POST");
        assertThat(entry.getRequestUrl()).isEqualTo("/api/update");
        assertThat(entry.getResponseStatus()).isEqualTo("202");
        assertThat(entry.getResponseTimeMs()).isEqualTo(200);
        assertThat(entry.getHost()).isEqualTo("webserver1");
    }

    // Test for invalid log (missing response status)
    @Test
    void givenInvalidLog_whenParse_thenReturnNull() {
        // when
        LogEntry logEntry = requestLogParser.parse(INVALID_LOG);

        // then
        assertThat(logEntry).isNull();
    }

    // Test for empty log
    @Test
    void givenEmptyLog_whenParse_thenReturnNull() {
        // when
        LogEntry logEntry = requestLogParser.parse(EMPTY_LOG);

        // then
        assertThat(logEntry).isNull();
    }

    // Test for null log
    @Test
    void givenNullLog_whenParse_thenReturnNull() {
        // when
        LogEntry logEntry = requestLogParser.parse(NULL_LOG);

        // then
        assertThat(logEntry).isNull();
    }

    // Test for passing log to the next parser if it's not a Request log
    @Test
    void givenNonRequestLog_whenParse_thenPassToNextParser() {
        // Arrange
        when(nextParser.parse("someOtherLog")).thenReturn(new RequestLogEntry("timestamp", "GET", "/api/test", "404", 100, "webserver2"));

        // when
        LogEntry logEntry = requestLogParser.parse("someOtherLog");

        // then
        verify(nextParser).parse("someOtherLog");
        assertThat(logEntry).isNotNull();
    }

    // Test for setting the next parser in the chain
    @Test
    void givenNextParser_whenSetNextParser_thenNextParserIsSet() {
        // when
        requestLogParser.setNextParser(nextParser);

        // then
        assertThat(requestLogParser).hasFieldOrPropertyWithValue("nextParser", nextParser);
    }

    // Test for handling an invalid log format in the parse method
    @Test
    void givenMalformedLog_whenParse_thenReturnNull() {
        // Arrange
        String malformedLog = "timestamp=2024-02-24T16:22:25Z request_method=POST response_status=202 response_time_ms=200";

        // when
        LogEntry logEntry = requestLogParser.parse(malformedLog);

        // then
        assertThat(logEntry).isNull();
    }
}
