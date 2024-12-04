/*package com.logparser;

import com.logparser.aggregators.APMLogAggregator;
import com.logparser.aggregators.ApplicationLogAggregator;
import com.logparser.aggregators.RequestLogAggregator;
import com.logparser.handlers.APMLogHandler;
import com.logparser.handlers.ApplicationLogHandler;
import com.logparser.handlers.LogHandler;
import com.logparser.handlers.RequestLogHandler;
import com.logparser.logs.LogEntry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LogParserIntegrationTest {

    @TempDir
    Path tempDir;

    private APMLogAggregator apmAggregator;
    private ApplicationLogAggregator applicationAggregator;
    private RequestLogAggregator requestAggregator;
    private LogHandler chain;

    @BeforeEach
    void setUp() {
        // Initialize the aggregators
        apmAggregator = new APMLogAggregator();
        applicationAggregator = new ApplicationLogAggregator();
        requestAggregator = new RequestLogAggregator();

        // Create handlers and set up the chain of responsibility
        LogHandler apmHandler = new APMLogHandler();
        LogHandler applicationHandler = new ApplicationLogHandler();
        LogHandler requestHandler = new RequestLogHandler();

        apmHandler.setNextHandler(applicationHandler);
        applicationHandler.setNextHandler(requestHandler);

        chain = apmHandler;
    }

    private void processLogs(List<String> logLines) {
        for (String line : logLines) {
            LogEntry entry = chain.handle(line);
            if (entry != null) {
                if (entry instanceof com.logparser.logs.APMLogEntry) {
                    apmAggregator.addLog(entry);
                } else if (entry instanceof com.logparser.logs.ApplicationLogEntry) {
                    applicationAggregator.addLog(entry);
                } else if (entry instanceof com.logparser.logs.RequestLogEntry) {
                    requestAggregator.addLog(entry);
                }
            }
        }
    }

    @Test
    void testEmptyAPMLog() throws IOException {
        List<String> logLines = Arrays.asList(
                "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1"
        );
        processLogs(logLines);
        assertTrue(apmAggregator.aggregate().isEmpty());

        Path apmJsonPath = tempDir.resolve("apm.json");
        App.writeToJSON(apmJsonPath.toString(), apmAggregator.aggregate());
        assertTrue(Files.size(apmJsonPath) > 0); // Verifies an empty JSON object is written
    }

    @Test
    void testEmptyApplicationLog() throws IOException {
        List<String> logLines = Arrays.asList(
                "timestamp=2024-02-24T16:22:25Z metric=memory_usage_percent host=webserver1 value=85"
        );
        processLogs(logLines);
        assertTrue(applicationAggregator.aggregate().isEmpty());

        Path applicationJsonPath = tempDir.resolve("application.json");
        App.writeToJSON(applicationJsonPath.toString(), applicationAggregator.aggregate());
        assertTrue(Files.size(applicationJsonPath) > 0); // Verifies an empty JSON object is written
    }

    @Test
    void testEmptyRequestLog() throws IOException {
        List<String> logLines = Arrays.asList(
                "timestamp=2024-02-24T16:22:15Z level=INFO message=\"Maintenance\" host=webserver1"
        );
        processLogs(logLines);
        assertTrue(requestAggregator.aggregate().isEmpty());

        Path requestJsonPath = tempDir.resolve("request.json");
        App.writeToJSON(requestJsonPath.toString(), requestAggregator.aggregate());
        assertTrue(Files.size(requestJsonPath) > 0); // Verifies an empty JSON object is written
    }

    @Test
    void testSingleAPMLog() {
        List<String> logLines = Arrays.asList(
                "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72"
        );
        processLogs(logLines);
        assertFalse(apmAggregator.aggregate().isEmpty());
        assertTrue(apmAggregator.aggregate().containsKey("cpu_usage_percent"));
    }

    @Test
    void testSingleApplicationLog() {
        List<String> logLines = Arrays.asList(
                "timestamp=2024-02-24T16:22:20Z level=ERROR message=\"Error occurred\" host=webserver1"
        );
        processLogs(logLines);
        assertFalse(applicationAggregator.aggregate().isEmpty());
        assertTrue(applicationAggregator.aggregate().containsKey("ERROR"));
    }

    @Test
    void testSingleRequestLog() {
        List<String> logLines = Arrays.asList(
                "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=202 response_time_ms=200 host=webserver1"
        );
        processLogs(logLines);
        assertFalse(requestAggregator.aggregate().isEmpty());
        assertTrue(requestAggregator.aggregate().containsKey("/api/update"));
    }

    @Test
    void testNormalFlow() throws IOException {
        List<String> logLines = Arrays.asList(
                "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72",
                "timestamp=2024-02-24T16:22:20Z level=INFO message=\"Maintenance\" host=webserver1",
                "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=202 response_time_ms=200 host=webserver1"
        );
        processLogs(logLines);

        assertFalse(apmAggregator.aggregate().isEmpty());
        assertFalse(applicationAggregator.aggregate().isEmpty());
        assertFalse(requestAggregator.aggregate().isEmpty());

        Path apmJsonPath = tempDir.resolve("apm.json");
        Path appJsonPath = tempDir.resolve("application.json");
        Path requestJsonPath = tempDir.resolve("request.json");

        App.writeToJSON(apmJsonPath.toString(), apmAggregator.aggregate());
        App.writeToJSON(appJsonPath.toString(), applicationAggregator.aggregate());
        App.writeToJSON(requestJsonPath.toString(), requestAggregator.aggregate());

        assertTrue(Files.size(apmJsonPath) > 0);
        assertTrue(Files.size(appJsonPath) > 0);
        assertTrue(Files.size(requestJsonPath) > 0);
    }
}


*/
package com.logparser;

import com.logparser.aggregators.APMLogAggregator;
import com.logparser.aggregators.ApplicationLogAggregator;
import com.logparser.aggregators.RequestLogAggregator;
import com.logparser.handlers.APMLogHandler;
import com.logparser.handlers.ApplicationLogHandler;
import com.logparser.handlers.LogHandler;
import com.logparser.handlers.RequestLogHandler;
import com.logparser.logs.LogEntry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LogParserIntegrationTest {

    private static final Path OUTPUT_DIR = Paths.get("src/test/output");

    private APMLogAggregator apmAggregator;
    private ApplicationLogAggregator applicationAggregator;
    private RequestLogAggregator requestAggregator;
    private LogHandler chain;

    @BeforeEach
    void setUp() throws IOException {
        // Ensure the output directory exists
        Files.createDirectories(OUTPUT_DIR);

        // Initialize the aggregators
        apmAggregator = new APMLogAggregator();
        applicationAggregator = new ApplicationLogAggregator();
        requestAggregator = new RequestLogAggregator();

        // Create handlers and set up the chain of responsibility
        LogHandler apmHandler = new APMLogHandler();
        LogHandler applicationHandler = new ApplicationLogHandler();
        LogHandler requestHandler = new RequestLogHandler();

        apmHandler.setNextHandler(applicationHandler);
        applicationHandler.setNextHandler(requestHandler);

        chain = apmHandler;
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up files in the output directory
        Files.walk(OUTPUT_DIR)
             .map(Path::toFile)
             .forEach(file -> {
                 if (!file.isDirectory()) {
                     file.delete();
                 }
             });
    }

    private void processLogs(List<String> logLines) {
        for (String line : logLines) {
            LogEntry entry = chain.handle(line);
            if (entry != null) {
                if (entry instanceof com.logparser.logs.APMLogEntry) {
                    apmAggregator.addLog(entry);
                } else if (entry instanceof com.logparser.logs.ApplicationLogEntry) {
                    applicationAggregator.addLog(entry);
                } else if (entry instanceof com.logparser.logs.RequestLogEntry) {
                    requestAggregator.addLog(entry);
                }
            }
        }
    }

    @Test
    void testEmptyAPMLog() throws IOException {
        List<String> logLines = Arrays.asList(
                "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1"
        );
        processLogs(logLines);
        assertTrue(apmAggregator.aggregate().isEmpty());

        Path apmJsonPath = OUTPUT_DIR.resolve("apm.json");
        App.writeToJSON(apmJsonPath.toString(), apmAggregator.aggregate());
        assertTrue(Files.size(apmJsonPath) > 0); // Verifies an empty JSON object is written
    }

    @Test
    void testEmptyApplicationLog() throws IOException {
        List<String> logLines = Arrays.asList(
                "timestamp=2024-02-24T16:22:25Z metric=memory_usage_percent host=webserver1 value=85"
        );
        processLogs(logLines);
        assertTrue(applicationAggregator.aggregate().isEmpty());

        Path applicationJsonPath = OUTPUT_DIR.resolve("application.json");
        App.writeToJSON(applicationJsonPath.toString(), applicationAggregator.aggregate());
        assertTrue(Files.size(applicationJsonPath) > 0); // Verifies an empty JSON object is written
    }

    @Test
    void testEmptyRequestLog() throws IOException {
        List<String> logLines = Arrays.asList(
                "timestamp=2024-02-24T16:22:15Z level=INFO message=\"Maintenance\" host=webserver1"
        );
        processLogs(logLines);
        assertTrue(requestAggregator.aggregate().isEmpty());

        Path requestJsonPath = OUTPUT_DIR.resolve("request.json");
        App.writeToJSON(requestJsonPath.toString(), requestAggregator.aggregate());
        assertTrue(Files.size(requestJsonPath) > 0); // Verifies an empty JSON object is written
    }

    @Test
    void testSingleAPMLog() throws IOException {
        List<String> logLines = Arrays.asList(
                "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72"
        );
        processLogs(logLines);
        assertFalse(apmAggregator.aggregate().isEmpty());
        assertTrue(apmAggregator.aggregate().containsKey("cpu_usage_percent"));

        Path apmJsonPath = OUTPUT_DIR.resolve("apm.json");
        App.writeToJSON(apmJsonPath.toString(), apmAggregator.aggregate());
        assertTrue(Files.size(apmJsonPath) > 0);
    }

    @Test
    void testNormalFlow() throws IOException {
        List<String> logLines = Arrays.asList(
                "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72",
                "timestamp=2024-02-24T16:22:20Z level=INFO message=\"Maintenance\" host=webserver1",
                "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=202 response_time_ms=200 host=webserver1"
        );
        processLogs(logLines);

        assertFalse(apmAggregator.aggregate().isEmpty());
        assertFalse(applicationAggregator.aggregate().isEmpty());
        assertFalse(requestAggregator.aggregate().isEmpty());

        Path apmJsonPath = OUTPUT_DIR.resolve("apm.json");
        Path appJsonPath = OUTPUT_DIR.resolve("application.json");
        Path requestJsonPath = OUTPUT_DIR.resolve("request.json");

        App.writeToJSON(apmJsonPath.toString(), apmAggregator.aggregate());
        App.writeToJSON(appJsonPath.toString(), applicationAggregator.aggregate());
        App.writeToJSON(requestJsonPath.toString(), requestAggregator.aggregate());

        assertTrue(Files.size(apmJsonPath) > 0);
        assertTrue(Files.size(appJsonPath) > 0);
        assertTrue(Files.size(requestJsonPath) > 0);
    }
}
