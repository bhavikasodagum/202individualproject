package com.logparser;

import com.logparser.logs.ApplicationLogEntry;
import com.logparser.logs.LogEntry;
import com.logparser.parsers.ApplicationLogParser;
import com.logparser.parsers.LogParser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationLogParserTest {

    private static final String VALID_LOG_LINE = "timestamp=2024-12-01 level=INFO message=\"Test message\" host=localhost";
    private static final String INVALID_LOG_LINE = "timestamp=2024-12-01 level=INFO host=localhost";  // Missing message
    private static final String MALFORMED_LOG_LINE = "timestamp=2024-12-01 level=INFO message=host=localhost";  // Malformed message
    private static final String INCOMPLETE_LOG_LINE = "timestamp=2024-12-01 level=INFO message=\"Test message\"";  // Missing host

    @InjectMocks
    private ApplicationLogParser applicationLogParser;

    @Mock
    private LogParser nextParser;  // Mock the next parser in the chain

    @BeforeEach
    void setUp() {
        // Ensure nextParser is set to a mock LogParser for chain testing
        applicationLogParser.setNextParser(nextParser);
    }

    @Test
    void shouldParseValidLogLine() {
        // When
        LogEntry result = applicationLogParser.parse(VALID_LOG_LINE);

        // Then
        assertThat(result).isInstanceOf(ApplicationLogEntry.class);
        ApplicationLogEntry entry = (ApplicationLogEntry) result;
        assertThat(entry.getTimestamp()).isEqualTo("2024-12-01");
        assertThat(entry.getLevel()).isEqualTo("INFO");
        assertThat(entry.getMessage()).isEqualTo("Test message");
        assertThat(entry.getHost()).isEqualTo("localhost");
    }

    @Test
    void shouldReturnNullForInvalidLogLineMissingMessage() {
        // When
        LogEntry result = applicationLogParser.parse(INVALID_LOG_LINE);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldReturnNullForMalformedLogLine() {
        // When
        LogEntry result = applicationLogParser.parse(MALFORMED_LOG_LINE);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldReturnNullForIncompleteLogLineMissingHost() {
        // When
        LogEntry result = applicationLogParser.parse(INCOMPLETE_LOG_LINE);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldPassLogToNextParserIfNotAnApplicationLog() {
        // Given a non-Application log
        String nonApplicationLogLine = "some other log entry";

        // When
        when(nextParser.parse(nonApplicationLogLine)).thenReturn(new ApplicationLogEntry("timestamp", "INFO", "message", "host"));
        LogEntry result = applicationLogParser.parse(nonApplicationLogLine);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(ApplicationLogEntry.class);
    }

    @Test
    void shouldReturnNullWhenNextParserIsNull() {
        // Given the next parser is null
        applicationLogParser.setNextParser(null);

        // When
        LogEntry result = applicationLogParser.parse("random log");

        // Then
        assertThat(result).isNull();
    }

   
}
