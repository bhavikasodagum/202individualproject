package com.logparser;
import org.junit.jupiter.api.Test;

import com.logparser.logs.RequestLogEntry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RequestLogEntryTest {

    private static final String VALID_TIMESTAMP = "2024-12-01T12:00:00";
    private static final String VALID_REQUEST_METHOD = "GET";
    private static final String VALID_REQUEST_URL = "/api/resource";
    private static final String VALID_RESPONSE_STATUS = "200";
    private static final double VALID_RESPONSE_TIME_MS = 150.0;
    private static final String VALID_HOST = "localhost";

    @Test
    public void constructor_shouldInitializeRequestLogEntryWithValidInput() {
        // Valid input
        RequestLogEntry entry = new RequestLogEntry(VALID_TIMESTAMP, VALID_REQUEST_METHOD, VALID_REQUEST_URL,
                VALID_RESPONSE_STATUS, VALID_RESPONSE_TIME_MS, VALID_HOST);

        assertThat(entry.getTimestamp()).isEqualTo(VALID_TIMESTAMP);
        assertThat(entry.getRequestMethod()).isEqualTo(VALID_REQUEST_METHOD);
        assertThat(entry.getRequestUrl()).isEqualTo(VALID_REQUEST_URL);
        assertThat(entry.getResponseStatus()).isEqualTo(VALID_RESPONSE_STATUS);
        assertThat(entry.getResponseTimeMs()).isEqualTo(VALID_RESPONSE_TIME_MS);
        assertThat(entry.getHost()).isEqualTo(VALID_HOST);
    }

    @Test
    public void constructor_shouldThrowExceptionForNullTimestamp() {
        // Test for null timestamp
        assertThatThrownBy(() -> new RequestLogEntry(null, VALID_REQUEST_METHOD, VALID_REQUEST_URL,
                VALID_RESPONSE_STATUS, VALID_RESPONSE_TIME_MS, VALID_HOST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Timestamp cannot be null or empty");
    }

    @Test
    public void constructor_shouldThrowExceptionForNullRequestMethod() {
        // Test for null requestMethod
        assertThatThrownBy(() -> new RequestLogEntry(VALID_TIMESTAMP, null, VALID_REQUEST_URL,
                VALID_RESPONSE_STATUS, VALID_RESPONSE_TIME_MS, VALID_HOST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Request method cannot be null or empty");
    }

    @Test
    public void constructor_shouldThrowExceptionForNullRequestUrl() {
        // Test for null requestUrl
        assertThatThrownBy(() -> new RequestLogEntry(VALID_TIMESTAMP, VALID_REQUEST_METHOD, null,
                VALID_RESPONSE_STATUS, VALID_RESPONSE_TIME_MS, VALID_HOST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Request URL cannot be null or empty");
    }

    @Test
    public void constructor_shouldThrowExceptionForNullResponseStatus() {
        // Test for null responseStatus
        assertThatThrownBy(() -> new RequestLogEntry(VALID_TIMESTAMP, VALID_REQUEST_METHOD, VALID_REQUEST_URL,
                null, VALID_RESPONSE_TIME_MS, VALID_HOST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Response status cannot be null or empty");
    }

    @Test
    public void constructor_shouldThrowExceptionForNullHost() {
        // Test for null host
        assertThatThrownBy(() -> new RequestLogEntry(VALID_TIMESTAMP, VALID_REQUEST_METHOD, VALID_REQUEST_URL,
                VALID_RESPONSE_STATUS, VALID_RESPONSE_TIME_MS, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Host cannot be null or empty");
    }

    @Test
    public void constructor_shouldThrowExceptionForNegativeResponseTime() {
        // Test for negative response time
        assertThatThrownBy(() -> new RequestLogEntry(VALID_TIMESTAMP, VALID_REQUEST_METHOD, VALID_REQUEST_URL,
                VALID_RESPONSE_STATUS, -1.0, VALID_HOST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Response time cannot be negative");
    }

    @Test
    public void constructor_shouldThrowExceptionForEmptyStrings() {
        // Test for empty timestamp
        assertThatThrownBy(() -> new RequestLogEntry("", VALID_REQUEST_METHOD, VALID_REQUEST_URL,
                VALID_RESPONSE_STATUS, VALID_RESPONSE_TIME_MS, VALID_HOST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Timestamp cannot be null or empty");

        // Test for empty requestMethod
        assertThatThrownBy(() -> new RequestLogEntry(VALID_TIMESTAMP, "", VALID_REQUEST_URL,
                VALID_RESPONSE_STATUS, VALID_RESPONSE_TIME_MS, VALID_HOST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Request method cannot be null or empty");

        // Test for empty requestUrl
        assertThatThrownBy(() -> new RequestLogEntry(VALID_TIMESTAMP, VALID_REQUEST_METHOD, "",
                VALID_RESPONSE_STATUS, VALID_RESPONSE_TIME_MS, VALID_HOST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Request URL cannot be null or empty");

        // Test for empty responseStatus
        assertThatThrownBy(() -> new RequestLogEntry(VALID_TIMESTAMP, VALID_REQUEST_METHOD, VALID_REQUEST_URL,
                "", VALID_RESPONSE_TIME_MS, VALID_HOST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Response status cannot be null or empty");

        // Test for empty host
        assertThatThrownBy(() -> new RequestLogEntry(VALID_TIMESTAMP, VALID_REQUEST_METHOD, VALID_REQUEST_URL,
                VALID_RESPONSE_STATUS, VALID_RESPONSE_TIME_MS, ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Host cannot be null or empty");
    }

    @Test
    public void toString_shouldReturnValidStringRepresentation() {
        RequestLogEntry entry = new RequestLogEntry(VALID_TIMESTAMP, VALID_REQUEST_METHOD, VALID_REQUEST_URL,
                VALID_RESPONSE_STATUS, VALID_RESPONSE_TIME_MS, VALID_HOST);

        String expectedToString = "RequestLogEntry{" +
                "timestamp='" + VALID_TIMESTAMP + '\'' +
                ", requestMethod='" + VALID_REQUEST_METHOD + '\'' +
                ", requestUrl='" + VALID_REQUEST_URL + '\'' +
                ", responseStatus='" + VALID_RESPONSE_STATUS + '\'' +
                ", responseTimeMs=" + VALID_RESPONSE_TIME_MS +
                ", host='" + VALID_HOST + '\'' +
                '}';

        assertThat(entry.toString()).isEqualTo(expectedToString);
    }

    // Additional Test Case for edge cases, e.g., long inputs or boundary values
    @Test
    public void constructor_shouldHandleLongInputs() {
        String longInput = "A".repeat(1000); // 1000 character string

        RequestLogEntry entry = new RequestLogEntry(longInput, VALID_REQUEST_METHOD, VALID_REQUEST_URL,
                VALID_RESPONSE_STATUS, VALID_RESPONSE_TIME_MS, VALID_HOST);

        assertThat(entry.getTimestamp()).isEqualTo(longInput);
    }
}
