package com.logparser;

import com.logparser.aggregators.RequestLogAggregator;
import com.logparser.logs.APMLogEntry;
import com.logparser.logs.LogEntry;
import com.logparser.logs.RequestLogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class RequestLogAggregatorTest {

    private RequestLogAggregator aggregator;

    @BeforeEach
    void setUp() {
        aggregator = new RequestLogAggregator();
    }

    @Test
    void shouldAddValidRequestLog() {
        RequestLogEntry logEntry = new RequestLogEntry(
                "2024-12-03T12:00:00Z", "GET", "/api/test", "200", 120.5, "host1");

        aggregator.addLog(logEntry);

        Map<String, List<Double>> responseTimes = getFieldValue(aggregator, "responseTimesMap");
        assertThat(responseTimes).containsKey("/api/test");
        assertThat(responseTimes.get("/api/test")).containsExactly(120.5);

        Map<String, Map<String, Integer>> statusCodes = getFieldValue(aggregator, "statusCodesMap");
        assertThat(statusCodes).containsKey("/api/test");
        assertThat(statusCodes.get("/api/test").get("2XX")).isEqualTo(1);
    }

    @Test
    void shouldIgnoreInvalidLogEntry() {
        aggregator.addLog(null);

        Map<String, List<Double>> responseTimes = getFieldValue(aggregator, "responseTimesMap");
        assertThat(responseTimes).isEmpty();

        Map<String, Map<String, Integer>> statusCodes = getFieldValue(aggregator, "statusCodesMap");
        assertThat(statusCodes).isEmpty();
    }

    @Test
    void shouldAggregateResponseTimeStatistics() {
        aggregator.addLog(new RequestLogEntry("2024-12-03T12:00:00Z", "GET", "/api/test", "200", 120.5, "host1"));
        aggregator.addLog(new RequestLogEntry("2024-12-03T12:01:00Z", "GET", "/api/test", "404", 80.0, "host1"));
        aggregator.addLog(new RequestLogEntry("2024-12-03T12:02:00Z", "GET", "/api/test", "200", 200.0, "host1"));

        Map<String, Map<String, Object>> results = aggregator.aggregate();

        assertThat(results).containsKey("/api/test");
        Map<String, Object> stats = results.get("/api/test");

        Map<String, Object> responseTimes = (Map<String, Object>) stats.get("response_times");
        assertThat(responseTimes.get("min")).isEqualTo(80);
        assertThat(responseTimes.get("max")).isEqualTo(200);
        assertThat(responseTimes.get("50_percentile")).isEqualTo(120.5);

        Map<String, Integer> statusCodes = (Map<String, Integer>) stats.get("status_codes");
        assertThat(statusCodes.get("2XX")).isEqualTo(2);
        assertThat(statusCodes.get("4XX")).isEqualTo(1);
    }

    @Test
    void shouldHandleEmptyAggregation() {
        Map<String, Map<String, Object>> results = aggregator.aggregate();
        assertThat(results).isEmpty();
    }

   /*  @Test
    void shouldCalculatePercentilesCorrectly() {
        List<Double> values = Arrays.asList(10.0, 20.0, 30.0, 40.0, 50.0);
        setFieldValue(aggregator, "responseTimesMap", Map.of("/api/test", new ArrayList<>(values)));
    
        // Call aggregate to get results
        Map<String, Map<String, Object>> results = aggregator.aggregate();
    
        // Access the nested structure
        Map<String, Object> routeStats = results.get("/api/test");
        Map<String, Object> responseTimes = (Map<String, Object>) routeStats.get("response_times");
    
        // Update expected values to match the actual output of your RequestLogAggregator
        assertThat(responseTimes.get("50_percentile")).isEqualTo(30.0); // Median
        assertThat(responseTimes.get("90_percentile")).isEqualTo(49.6); // As per your implementation
        assertThat(responseTimes.get("95_percentile")).isEqualTo(49.8); // Matches calculation
        assertThat(responseTimes.get("99_percentile")).isEqualTo(50.0); // Max value
    }
    */
    @Test
void shouldCalculatePercentilesCorrectly() {
    // Input data for the test
    List<Double> values = Arrays.asList(10.0, 20.0, 30.0, 40.0, 50.0);

    // Set the responseTimesMap directly to include the test data
    setFieldValue(aggregator, "responseTimesMap", Map.of("/api/test", new ArrayList<>(values)));

    // Run the aggregation
    Map<String, Map<String, Object>> results = aggregator.aggregate();

    // Extract the aggregated results for /api/test
    Map<String, Object> routeStats = results.get("/api/test");
    Map<String, Object> responseTimes = (Map<String, Object>) routeStats.get("response_times");

    // Verify the percentiles and statistics
    assertThat(responseTimes.get("min")).isEqualTo(10);       // Minimum response time
    assertThat(responseTimes.get("max")).isEqualTo(50);       // Maximum response time
    assertThat(responseTimes.get("50_percentile")).isEqualTo(30.0);  // Median (50th percentile)
    assertThat(responseTimes.get("90_percentile")).isEqualTo(46.0);  // 90th percentile
    assertThat(responseTimes.get("95_percentile")).isEqualTo(48.0);  // 95th percentile
    assertThat(responseTimes.get("99_percentile")).isEqualTo(49.6);  // 99th percentile
}

    

    @Test
    void shouldCategorizeStatusCodesCorrectly() {
        String result = (String) invokePrivateMethod(
                aggregator,
                "getStatusCategory",
                new Class[]{String.class},
                new Object[]{"404"}
        );
        assertThat(result).isEqualTo("4XX");
    }

    // Utility method to access private fields
    private <T> T getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Unable to access field: " + fieldName, e);
        }
    }

    // Utility method to set private fields
    private void setFieldValue(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Unable to set field: " + fieldName, e);
        }
    }

    // Utility method to invoke private methods
    private Object invokePrivateMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] args) {
        try {
            Method method = object.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException("Unable to invoke method: " + methodName, e);
        }
    }

    @Test
void shouldHandleEdgeCasesForStatusCategory() {
    String nullStatus = (String) invokePrivateMethod(aggregator, "getStatusCategory", new Class[]{String.class}, new Object[]{null});
    assertThat(nullStatus).isEqualTo("Other");

    String emptyStatus = (String) invokePrivateMethod(aggregator, "getStatusCategory", new Class[]{String.class}, new Object[]{""});
    assertThat(emptyStatus).isEqualTo("Other");

    String unknownStatus = (String) invokePrivateMethod(aggregator, "getStatusCategory", new Class[]{String.class}, new Object[]{"999"});
    assertThat(unknownStatus).isEqualTo("Other");
}


@Test
void shouldIgnoreNonRequestLogEntries() {
    LogEntry nonRequestLogEntry = new APMLogEntry("2024-12-03T12:00:00Z", "cpu_usage_percent", 50.0, "host1");

    aggregator.addLog(nonRequestLogEntry);

    Map<String, List<Double>> responseTimes = getFieldValue(aggregator, "responseTimesMap");
    assertThat(responseTimes).isEmpty();

    Map<String, Map<String, Integer>> statusCodes = getFieldValue(aggregator, "statusCodesMap");
    assertThat(statusCodes).isEmpty();
}

@Test
void shouldHandleSingleValueInPercentileCalculation() {
    List<Double> values = Collections.singletonList(25.0);
    setFieldValue(aggregator, "responseTimesMap", Map.of("/api/test", new ArrayList<>(values)));

    Map<String, Map<String, Object>> results = aggregator.aggregate();
    Map<String, Object> responseTimes = (Map<String, Object>) results.get("/api/test").get("response_times");

    assertThat(responseTimes.get("50_percentile")).isEqualTo(25.0);
    assertThat(responseTimes.get("90_percentile")).isEqualTo(25.0);
    assertThat(responseTimes.get("95_percentile")).isEqualTo(25.0);
    assertThat(responseTimes.get("99_percentile")).isEqualTo(25.0);
}

/*@Test
void shouldReturnOtherForUnrecognizedStatusCodes() {
    String result = (String) invokePrivateMethod(
            aggregator,
            "getStatusCategory",
            new Class[]{String.class},
            new Object[]{"999"}
    );
    assertThat(result).isEqualTo("Other");
}*/
@Test
void shouldReturnOtherForUnrecognizedStatusCodes() {
    String result = (String) invokePrivateMethod(
            aggregator,
            "getStatusCategory",
            new Class[]{String.class},
            new Object[]{"999"} // Unrecognized status code
    );
    assertThat(result).isEqualTo("Other");
}


@Test
void shouldHandleEmptyResponseTimesInAggregation() {
    // Provide a single API route with no response times
    setFieldValue(aggregator, "responseTimesMap", Map.of("/api/test", Collections.singletonList(0.0)));

    Map<String, Map<String, Object>> results = aggregator.aggregate();

    assertThat(results).containsKey("/api/test");
    Map<String, Object> routeStats = results.get("/api/test");
    Map<String, Object> responseTimes = (Map<String, Object>) routeStats.get("response_times");

    // Expected behavior for an entry with no meaningful data
    assertThat(responseTimes.get("min")).isEqualTo(0);        // Single response time treated as minimum
    assertThat(responseTimes.get("max")).isEqualTo(0);        // Single response time treated as maximum
    assertThat(responseTimes.get("50_percentile")).isEqualTo(0.0); // Percentile calculations for a single value
    assertThat(responseTimes.get("90_percentile")).isEqualTo(0.0);
    assertThat(responseTimes.get("95_percentile")).isEqualTo(0.0);
    assertThat(responseTimes.get("99_percentile")).isEqualTo(0.0);
}


@Test
void shouldIgnoreLogsThatAreNotRequestLogEntry() {
    LogEntry nonRequestLogEntry = new APMLogEntry(
            "2024-12-03T12:00:00Z", "cpu_usage_percent", 50.0, "host1");

    aggregator.addLog(nonRequestLogEntry);

    Map<String, List<Double>> responseTimes = getFieldValue(aggregator, "responseTimesMap");
    assertThat(responseTimes).isEmpty();

    Map<String, Map<String, Integer>> statusCodes = getFieldValue(aggregator, "statusCodesMap");
    assertThat(statusCodes).isEmpty();
}

@Test
void shouldIgnoreNonRequestLogEntry() {
    // Non-RequestLogEntry log
    LogEntry nonRequestLogEntry = new APMLogEntry(
            "2024-12-03T12:00:00Z", "cpu_usage_percent", 50.0, "host1");

    aggregator.addLog(nonRequestLogEntry);

    // Validate that nothing was added
    Map<String, List<Double>> responseTimes = getFieldValue(aggregator, "responseTimesMap");
    assertThat(responseTimes).isEmpty();

    Map<String, Map<String, Integer>> statusCodes = getFieldValue(aggregator, "statusCodesMap");
    assertThat(statusCodes).isEmpty();
}



}
