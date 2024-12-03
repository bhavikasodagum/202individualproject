package com.logparser;

import org.junit.jupiter.api.Test;

import com.logparser.logs.APMLogEntry;

import static org.assertj.core.api.Assertions.*;

class APMLogEntryTest {

    // Test valid constructor
    @Test
    void shouldCreateAPMLogEntryWithValidValues() {
        // Given
        String timestamp = "2024-02-24T16:22:15Z";
        String metric = "cpu_usage_percent";
        double value = 75.0;
        String host = "webserver1";

        // When
        APMLogEntry logEntry = new APMLogEntry(timestamp, metric, value, host);

        // Then
        assertThat(logEntry.getTimestamp()).isEqualTo(timestamp);
        assertThat(logEntry.getMetric()).isEqualTo(metric);
        assertThat(logEntry.getValue()).isEqualTo(value);
        assertThat(logEntry.getHost()).isEqualTo(host);
    }

    // Test constructor with invalid (negative) value
    @Test
    void shouldThrowExceptionWhenValueIsNegative() {
        // Given
        String timestamp = "2024-02-24T16:22:15Z";
        String metric = "cpu_usage_percent";
        double negativeValue = -10.0;
        String host = "webserver1";

        // When & Then
        assertThatThrownBy(() -> new APMLogEntry(timestamp, metric, negativeValue, host))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Metric value cannot be negative.");
    }

    // Test getter for timestamp
    @Test
    void shouldReturnCorrectTimestamp() {
        // Given
        String timestamp = "2024-02-24T16:22:15Z";
        APMLogEntry logEntry = new APMLogEntry(timestamp, "cpu_usage_percent", 75.0, "webserver1");

        // When & Then
        assertThat(logEntry.getTimestamp()).isEqualTo(timestamp);
    }

    // Test getter for metric
    @Test
    void shouldReturnCorrectMetric() {
        // Given
        String metric = "cpu_usage_percent";
        APMLogEntry logEntry = new APMLogEntry("2024-02-24T16:22:15Z", metric, 75.0, "webserver1");

        // When & Then
        assertThat(logEntry.getMetric()).isEqualTo(metric);
    }

    // Test getter for value
    @Test
    void shouldReturnCorrectValue() {
        // Given
        double value = 75.0;
        APMLogEntry logEntry = new APMLogEntry("2024-02-24T16:22:15Z", "cpu_usage_percent", value, "webserver1");

        // When & Then
        assertThat(logEntry.getValue()).isEqualTo(value);
    }

    // Test getter for host
    @Test
    void shouldReturnCorrectHost() {
        // Given
        String host = "webserver1";
        APMLogEntry logEntry = new APMLogEntry("2024-02-24T16:22:15Z", "cpu_usage_percent", 75.0, host);

        // When & Then
        assertThat(logEntry.getHost()).isEqualTo(host);
    }

    // Test toString method
    @Test
    void shouldReturnCorrectToString() {
        // Given
        String timestamp = "2024-02-24T16:22:15Z";
        String metric = "cpu_usage_percent";
        double value = 75.0;
        String host = "webserver1";
        APMLogEntry logEntry = new APMLogEntry(timestamp, metric, value, host);

        // When
        String result = logEntry.toString();

        // Then
        assertThat(result).isEqualTo("APMLogEntry{timestamp='2024-02-24T16:22:15Z', metric='cpu_usage_percent', value=75.0, host='webserver1'}");
    }
}
