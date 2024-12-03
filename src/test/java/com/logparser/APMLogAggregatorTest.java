package com.logparser;

import com.logparser.aggregators.APMLogAggregator;
import com.logparser.logs.APMLogEntry;
import com.logparser.logs.ApplicationLogEntry;
import com.logparser.logs.LogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class APMLogAggregatorTest {

    private APMLogAggregator aggregator;

    @BeforeEach
    void setUp() {
        aggregator = new APMLogAggregator();
    }

    @Test
    void addLog_shouldAddValidAPMLogEntry() {
        // Given
        LogEntry logEntry1 = new APMLogEntry("2024-02-24T16:22:15Z", "cpu_usage_percent", 70.0, "webserver1");
        LogEntry logEntry2 = new APMLogEntry("2024-02-24T16:22:20Z", "cpu_usage_percent", 80.0, "webserver1");

        // When
        aggregator.addLog(logEntry1);
        aggregator.addLog(logEntry2);

        // Then
        Map<String, Object> result = aggregator.aggregate();
        assertThat(result).containsKey("cpu_usage_percent");
        Map<String, Object> stats = (Map<String, Object>) result.get("cpu_usage_percent");
        assertThat(stats).containsEntry("minimum", 70)
                         .containsEntry("median", 75.0) // Median of [70, 80]
                         .containsEntry("average", 75.0) // Average of [70, 80]
                         .containsEntry("max", 80);
    }

    @Test
    void addLog_shouldIgnoreNonAPMLogEntry() {
        // Given
        LogEntry logEntry = new ApplicationLogEntry("2024-02-24T16:22:15Z", "INFO", "Test message", "webserver1");

        // When
        aggregator.addLog(logEntry);

        // Then
        Map<String, Object> result = aggregator.aggregate();
        assertThat(result).isEmpty();
    }

    @Test
    void aggregate_shouldReturnEmptyMapWhenNoLogsAdded() {
        // When
        Map<String, Object> result = aggregator.aggregate();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void aggregate_shouldCalculateStatisticsForSingleMetric() {
        // Given
        aggregator.addLog(new APMLogEntry("2024-02-24T16:22:15Z", "cpu_usage_percent", 70.0, "webserver1"));
        aggregator.addLog(new APMLogEntry("2024-02-24T16:22:20Z", "cpu_usage_percent", 80.0, "webserver1"));
        aggregator.addLog(new APMLogEntry("2024-02-24T16:22:25Z", "cpu_usage_percent", 90.0, "webserver1"));

        // When
        Map<String, Object> result = aggregator.aggregate();

        // Then
        assertThat(result).containsKey("cpu_usage_percent");
        Map<String, Object> stats = (Map<String, Object>) result.get("cpu_usage_percent");
        assertThat(stats).containsEntry("minimum", 70)
                         .containsEntry("median", 80.0) // Median of [70, 80, 90]
                         .containsEntry("average", 80.0) // Average of [70, 80, 90]
                         .containsEntry("max", 90);
    }

    @Test
    void aggregate_shouldCalculateStatisticsForMultipleMetrics() {
        // Given
        aggregator.addLog(new APMLogEntry("2024-02-24T16:22:15Z", "cpu_usage_percent", 70.0, "webserver1"));
        aggregator.addLog(new APMLogEntry("2024-02-24T16:22:20Z", "memory_usage_percent", 50.0, "webserver1"));
        aggregator.addLog(new APMLogEntry("2024-02-24T16:22:25Z", "cpu_usage_percent", 90.0, "webserver1"));

        // When
        Map<String, Object> result = aggregator.aggregate();

        // Then
        assertThat(result).containsKeys("cpu_usage_percent", "memory_usage_percent");

        Map<String, Object> cpuStats = (Map<String, Object>) result.get("cpu_usage_percent");
        assertThat(cpuStats).containsEntry("minimum", 70)
                            .containsEntry("median", 80.0) // Median of [70, 90]
                            .containsEntry("average", 80.0) // Average of [70, 90]
                            .containsEntry("max", 90);

        Map<String, Object> memoryStats = (Map<String, Object>) result.get("memory_usage_percent");
        assertThat(memoryStats).containsEntry("minimum", 50)
                               .containsEntry("median", 50.0)
                               .containsEntry("average", 50.0)
                               .containsEntry("max", 50);
    }

    @Test
    void aggregate_shouldHandleSingleLogEntry() {
        // Given
        aggregator.addLog(new APMLogEntry("2024-02-24T16:22:15Z", "cpu_usage_percent", 70.0, "webserver1"));

        // When
        Map<String, Object> result = aggregator.aggregate();

        // Then
        assertThat(result).containsKey("cpu_usage_percent");
        Map<String, Object> stats = (Map<String, Object>) result.get("cpu_usage_percent");
        assertThat(stats).containsEntry("minimum", 70)
                         .containsEntry("median", 70.0) // Median is the single value
                         .containsEntry("average", 70.0) // Average is the single value
                         .containsEntry("max", 70);
    }
}
