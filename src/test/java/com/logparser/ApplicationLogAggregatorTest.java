package com.logparser;

import com.logparser.aggregators.ApplicationLogAggregator;
import com.logparser.logs.ApplicationLogEntry;
import com.logparser.logs.LogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationLogAggregatorTest {

    private ApplicationLogAggregator aggregator;

    @BeforeEach
    void setUp() {
        aggregator = new ApplicationLogAggregator();
    }

    @Test
    void addLog_shouldProcessValidApplicationLogEntry() {
        // Given
        LogEntry logEntry1 = new ApplicationLogEntry("2024-02-24T16:22:15Z", "INFO", "Test message 1", "webserver1");
        LogEntry logEntry2 = new ApplicationLogEntry("2024-02-24T16:22:20Z", "ERROR", "Test message 2", "webserver1");
        LogEntry logEntry3 = new ApplicationLogEntry("2024-02-24T16:22:25Z", "INFO", "Test message 3", "webserver1");

        // When
        aggregator.addLog(logEntry1);
        aggregator.addLog(logEntry2);
        aggregator.addLog(logEntry3);

        // Then
        Map<String, Integer> result = aggregator.aggregate();
        assertThat(result).containsEntry("INFO", 2)
                          .containsEntry("ERROR", 1);
    }

    @Test
    void addLog_shouldIgnoreNonApplicationLogEntry() {
        // Given
        LogEntry nonApplicationLogEntry = new LogEntry() {
            @Override
            public String getTimestamp() {
                return "2024-02-24T16:22:15Z";
            }

            @Override
            public String getHost() {
                return "webserver1";
            }
        };

        // When
        aggregator.addLog(nonApplicationLogEntry);

        // Then
        Map<String, Integer> result = aggregator.aggregate();
        assertThat(result).isEmpty();
    }

    @Test
    void aggregate_shouldReturnEmptyMapWhenNoLogsAdded() {
        // When
        Map<String, Integer> result = aggregator.aggregate();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void aggregate_shouldHandleMultipleSeverityLevels() {
        // Given
        aggregator.addLog(new ApplicationLogEntry("2024-02-24T16:22:15Z", "INFO", "Test message", "webserver1"));
        aggregator.addLog(new ApplicationLogEntry("2024-02-24T16:22:20Z", "WARNING", "Test message", "webserver1"));
        aggregator.addLog(new ApplicationLogEntry("2024-02-24T16:22:25Z", "ERROR", "Test message", "webserver1"));
        aggregator.addLog(new ApplicationLogEntry("2024-02-24T16:22:30Z", "DEBUG", "Test message", "webserver1"));

        // When
        Map<String, Integer> result = aggregator.aggregate();

        // Then
        assertThat(result).containsKeys("INFO", "WARNING", "ERROR", "DEBUG");
        assertThat(result.get("INFO")).isEqualTo(1);
        assertThat(result.get("WARNING")).isEqualTo(1);
        assertThat(result.get("ERROR")).isEqualTo(1);
        assertThat(result.get("DEBUG")).isEqualTo(1);
    }

    @Test
    void addLog_shouldHandleDuplicateEntries() {
        // Given
        LogEntry logEntry = new ApplicationLogEntry("2024-02-24T16:22:15Z", "ERROR", "Duplicate error", "webserver1");

        // When
        aggregator.addLog(logEntry);
        aggregator.addLog(logEntry); // Add the same log entry twice

        // Then
        Map<String, Integer> result = aggregator.aggregate();
        assertThat(result).containsEntry("ERROR", 2);
    }

    @Test
    void addLog_shouldIgnoreNullLogEntries() {
        // When
        aggregator.addLog(null);

        // Then
        Map<String, Integer> result = aggregator.aggregate();
        assertThat(result).isEmpty();
    }
}
