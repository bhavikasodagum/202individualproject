/*package com.logparser;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private final String inputFilePath = "test_input.txt";
    private final String invalidFilePath = "invalid_input.txt";
    private final String missingFilePath = "missing_input.txt";

    private final String apmOutputFile = "apm.json";
    private final String applicationOutputFile = "application.json";
    private final String requestOutputFile = "request.json";

    @BeforeEach
    void setUp() throws IOException {
        // Create a sample input file with test log entries
        Files.write(Paths.get(inputFilePath), List.of(
                "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72",
                "timestamp=2024-02-24T16:22:20Z level=INFO message=\"Scheduled maintenance starting\" host=webserver1",
                "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=202 response_time_ms=200 host=webserver1",
                "invalid log line that does not conform to any parser"
        ));

        // Create an invalid file for testing
        Files.write(Paths.get(invalidFilePath), List.of("corrupted log line"));

        // Ensure output files do not exist before tests
        deleteFileIfExists(apmOutputFile);
        deleteFileIfExists(applicationOutputFile);
        deleteFileIfExists(requestOutputFile);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete input and output files after each test
        deleteFileIfExists(inputFilePath);
        deleteFileIfExists(invalidFilePath);
        deleteFileIfExists(apmOutputFile);
        deleteFileIfExists(applicationOutputFile);
        deleteFileIfExists(requestOutputFile);
    }

    @Test
    void testMainWithValidFile() {
        String[] args = {"--file", inputFilePath};
        App.main(args);

        // Check that output files are created
        assertTrue(Files.exists(Paths.get(apmOutputFile)), "APM output file should be created");
        assertTrue(Files.exists(Paths.get(applicationOutputFile)), "Application output file should be created");
        assertTrue(Files.exists(Paths.get(requestOutputFile)), "Request output file should be created");

        // Check contents of one of the output files (APM example)
        try {
            String apmContent = Files.readString(Paths.get(apmOutputFile));
            assertNotNull(apmContent);
            assertTrue(apmContent.contains("cpu_usage_percent"), "APM content should include 'cpu_usage_percent'");
        } catch (IOException e) {
            fail("Error reading APM output file: " + e.getMessage());
        }
    }


    @Test
    void testMainWithMissingArguments() {
        String[] args = {};
        // Check that the program handles missing arguments gracefully
        assertDoesNotThrow(() -> App.main(args));
    }

    @Test
    void testMainWithNonexistentFile() {
        String[] args = {"--file", missingFilePath};
        // Test behavior when the input file does not exist
        assertDoesNotThrow(() -> App.main(args), "Program should handle missing input file gracefully");
    }

    @Test
    void testMainWithNoArguments() {
        // Simulate no arguments passed to main
        String[] args = {};
        assertDoesNotThrow(() -> App.main(args), "Program should handle no arguments gracefully");
    }

    private void deleteFileIfExists(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }
}
*/ //above is the code that disaapeared output files

/*package com.logparser;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private final String inputFilePath = "test_input.txt";
    private final String invalidFilePath = "invalid_input.txt";
    private final String missingFilePath = "missing_input.txt";

    private final String apmOutputFile = "apm.json";
    private final String applicationOutputFile = "application.json";
    private final String requestOutputFile = "request.json";

    @BeforeEach
    void setUp() throws IOException {
        // Create a sample input file with test log entries
        Files.write(Paths.get(inputFilePath), List.of(
                "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72",
                "timestamp=2024-02-24T16:22:20Z level=INFO message=\"Scheduled maintenance starting\" host=webserver1",
                "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=202 response_time_ms=200 host=webserver1",
                "invalid log line that does not conform to any parser"
        ));

        // Create an invalid file for testing
        Files.write(Paths.get(invalidFilePath), List.of("corrupted log line"));

        // Ensure output files are clean before tests
        initializeOutputFile(apmOutputFile);
        initializeOutputFile(applicationOutputFile);
        initializeOutputFile(requestOutputFile);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete only input files after each test
        deleteFileIfExists(inputFilePath);
        deleteFileIfExists(invalidFilePath);
    }

    @Test
    void testMainWithValidFile() {
        String[] args = {"--file", inputFilePath};
        App.main(args);

        // Check that output files are created
        assertTrue(Files.exists(Paths.get(apmOutputFile)), "APM output file should be created");
        assertTrue(Files.exists(Paths.get(applicationOutputFile)), "Application output file should be created");
        assertTrue(Files.exists(Paths.get(requestOutputFile)), "Request output file should be created");

        // Check contents of one of the output files (APM example)
        try {
            String apmContent = Files.readString(Paths.get(apmOutputFile));
            assertNotNull(apmContent);
            assertTrue(apmContent.contains("cpu_usage_percent"), "APM content should include 'cpu_usage_percent'");
        } catch (IOException e) {
            fail("Error reading APM output file: " + e.getMessage());
        }
    }

    @Test
    void testMainWithMissingArguments() {
        String[] args = {};
        // Check that the program handles missing arguments gracefully
        assertDoesNotThrow(() -> App.main(args));
    }

    @Test
    void testMainWithNonexistentFile() {
        String[] args = {"--file", missingFilePath};
        // Test behavior when the input file does not exist
        assertDoesNotThrow(() -> App.main(args), "Program should handle missing input file gracefully");
    }

    @Test
    void testMainWithNoArguments() {
        // Simulate no arguments passed to main
        String[] args = {};
        assertDoesNotThrow(() -> App.main(args), "Program should handle no arguments gracefully");
    }

    private void deleteFileIfExists(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    private void initializeOutputFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.writeString(path, "{}"); // Reset content to an empty JSON object
        } else {
            Files.createFile(path);
        }
    }
}*/ //code that wrote test outputs in oprimaryoutputfiles


package com.logparser;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private final String inputFilePath = "test_input.txt";
    private final String invalidFilePath = "invalid_input.txt";
    private final String missingFilePath = "missing_input.txt";

    private final String apmTestOutputFile = "test_apm.json";
    private final String applicationTestOutputFile = "test_application.json";
    private final String requestTestOutputFile = "test_request.json";

    @BeforeEach
    void setUp() throws IOException {
        // Create a sample input file with test log entries
        Files.write(Paths.get(inputFilePath), List.of(
                "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72",
                "timestamp=2024-02-24T16:22:20Z level=INFO message=\"Scheduled maintenance starting\" host=webserver1",
                "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=202 response_time_ms=200 host=webserver1",
                "invalid log line that does not conform to any parser"
        ));

        // Create an invalid file for testing
        Files.write(Paths.get(invalidFilePath), List.of("corrupted log line"));

        // Ensure test output files are initialized
        initializeOutputFile(apmTestOutputFile);
        initializeOutputFile(applicationTestOutputFile);
        initializeOutputFile(requestTestOutputFile);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete input and test output files after each test
        deleteFileIfExists(inputFilePath);
        deleteFileIfExists(invalidFilePath);
        deleteFileIfExists(apmTestOutputFile);
        deleteFileIfExists(applicationTestOutputFile);
        deleteFileIfExists(requestTestOutputFile);
    }

    @Test
void testMainWithValidFile() {
    String[] args = {
        "--file", inputFilePath,
        "--apm", "test_apm.json",
        "--application", "test_application.json",
        "--request", "test_request.json"
    };
    App.main(args);

    // Check that output files are created
    assertTrue(Files.exists(Paths.get("test_apm.json")), "Test APM output file should be created");
    assertTrue(Files.exists(Paths.get("test_application.json")), "Test Application output file should be created");
    assertTrue(Files.exists(Paths.get("test_request.json")), "Test Request output file should be created");

    // Check contents of the APM output file
    try {
        String apmContent = Files.readString(Paths.get("test_apm.json"));
        System.out.println("Test APM Output Content: " + apmContent); // Debugging output
        assertNotNull(apmContent, "Test APM output file content should not be null");
        assertTrue(apmContent.contains("cpu_usage_percent"), "Test APM content should include 'cpu_usage_percent'");
    } catch (IOException e) {
        fail("Error reading Test APM output file: " + e.getMessage());
    }
}


    @Test
    void testMainWithMissingArguments() {
        String[] args = {};
        assertDoesNotThrow(() -> App.main(args), "Program should handle missing arguments gracefully");
    }

    @Test
    void testMainWithNonexistentFile() {
        String[] args = {"--file", missingFilePath};
        assertDoesNotThrow(() -> App.main(args), "Program should handle missing input file gracefully");
    }

    @Test
    void testMainWithNoArguments() {
        String[] args = {};
        assertDoesNotThrow(() -> App.main(args), "Program should handle no arguments gracefully");
    }

    private void deleteFileIfExists(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    private void initializeOutputFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.writeString(path, "{}"); // Reset content to an empty JSON object
        } else {
            Files.createFile(path);
        }
    }
}
