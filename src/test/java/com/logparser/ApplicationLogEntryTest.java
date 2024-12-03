package com.logparser;

import org.junit.jupiter.api.Test;

import com.logparser.logs.ApplicationLogEntry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApplicationLogEntryTest {

    private static final String TIMESTAMP = "2024-12-01T10:00:00";
    private static final String LEVEL = "INFO";
    private static final String MESSAGE = "This is a test log message";
    private static final String HOST = "localhost";

    // Test valid inputs
    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
        // Given
        ApplicationLogEntry logEntry = new ApplicationLogEntry(TIMESTAMP, LEVEL, MESSAGE, HOST);

        // When
        String timestamp = logEntry.getTimestamp();
        String level = logEntry.getLevel();
        String message = logEntry.getMessage();
        String host = logEntry.getHost();

        // Then
        assertThat(timestamp).isEqualTo(TIMESTAMP);
        assertThat(level).isEqualTo(LEVEL);
        assertThat(message).isEqualTo(MESSAGE);
        assertThat(host).isEqualTo(HOST);
    }

    // Test with empty string values
    @Test
    void constructor_shouldHandleEmptyStringValues() {
        // Given
        ApplicationLogEntry logEntry = new ApplicationLogEntry("", "", "", "");

        // When
        String timestamp = logEntry.getTimestamp();
        String level = logEntry.getLevel();
        String message = logEntry.getMessage();
        String host = logEntry.getHost();

        // Then
        assertThat(timestamp).isEqualTo("");
        assertThat(level).isEqualTo("");
        assertThat(message).isEqualTo("");
        assertThat(host).isEqualTo("");
    }

    // Test with null values
    @Test
    void constructor_shouldHandleNullValues() {
        // Given
        ApplicationLogEntry logEntry = new ApplicationLogEntry(null, null, null, null);

        // When
        String timestamp = logEntry.getTimestamp();
        String level = logEntry.getLevel();
        String message = logEntry.getMessage();
        String host = logEntry.getHost();

        // Then
        assertThat(timestamp).isNull();
        assertThat(level).isNull();
        assertThat(message).isNull();
        assertThat(host).isNull();
    }

    // Test toString method
    @Test
    void toString_shouldReturnCorrectStringRepresentation() {
        // Given
        ApplicationLogEntry logEntry = new ApplicationLogEntry(TIMESTAMP, LEVEL, MESSAGE, HOST);

        // When
        String result = logEntry.toString();

        // Then
        assertThat(result).contains(TIMESTAMP);
        assertThat(result).contains(LEVEL);
        assertThat(result).contains(MESSAGE);
        assertThat(result).contains(HOST);
    }

    // Test with missing mandatory fields (null or empty)
    @Test
    void constructor_shouldHandleNullTimestamp() {
        // Given
        ApplicationLogEntry logEntry = new ApplicationLogEntry(null, LEVEL, MESSAGE, HOST);

        // When
        String timestamp = logEntry.getTimestamp();
        String level = logEntry.getLevel();
        String message = logEntry.getMessage();
        String host = logEntry.getHost();

        // Then
        assertThat(timestamp).isNull();
        assertThat(level).isEqualTo(LEVEL);
        assertThat(message).isEqualTo(MESSAGE);
        assertThat(host).isEqualTo(HOST);
    }

    @Test
    void constructor_shouldHandleNullLevel() {
        // Given
        ApplicationLogEntry logEntry = new ApplicationLogEntry(TIMESTAMP, null, MESSAGE, HOST);

        // When
        String timestamp = logEntry.getTimestamp();
        String level = logEntry.getLevel();
        String message = logEntry.getMessage();
        String host = logEntry.getHost();

        // Then
        assertThat(timestamp).isEqualTo(TIMESTAMP);
        assertThat(level).isNull();
        assertThat(message).isEqualTo(MESSAGE);
        assertThat(host).isEqualTo(HOST);
    }

    @Test
    void constructor_shouldHandleNullMessage() {
        // Given
        ApplicationLogEntry logEntry = new ApplicationLogEntry(TIMESTAMP, LEVEL, null, HOST);

        // When
        String timestamp = logEntry.getTimestamp();
        String level = logEntry.getLevel();
        String message = logEntry.getMessage();
        String host = logEntry.getHost();

        // Then
        assertThat(timestamp).isEqualTo(TIMESTAMP);
        assertThat(level).isEqualTo(LEVEL);
        assertThat(message).isNull();
        assertThat(host).isEqualTo(HOST);
    }

    @Test
    void constructor_shouldHandleNullHost() {
        // Given
        ApplicationLogEntry logEntry = new ApplicationLogEntry(TIMESTAMP, LEVEL, MESSAGE, null);

        // When
        String timestamp = logEntry.getTimestamp();
        String level = logEntry.getLevel();
        String message = logEntry.getMessage();
        String host = logEntry.getHost();

        // Then
        assertThat(timestamp).isEqualTo(TIMESTAMP);
        assertThat(level).isEqualTo(LEVEL);
        assertThat(message).isEqualTo(MESSAGE);
        assertThat(host).isNull();
    }

  
    @Test
    void constructor_shouldHandleNullInputGracefully() {
        // Given
        ApplicationLogEntry logEntry = new ApplicationLogEntry(null, null, null, null);
    
        // When
        String timestamp = logEntry.getTimestamp();
        String level = logEntry.getLevel();
        String message = logEntry.getMessage();
        String host = logEntry.getHost();
    
        // Then
        assertThat(timestamp).isNull();
        assertThat(level).isNull();
        assertThat(message).isNull();
        assertThat(host).isNull();
    }
    
}
