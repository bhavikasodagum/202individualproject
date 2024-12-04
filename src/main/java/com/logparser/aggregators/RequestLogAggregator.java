package com.logparser.aggregators;

import com.logparser.logs.RequestLogEntry;
import com.logparser.logs.LogEntry;

import java.util.*;

/**
 * Aggregator for Request logs.
 * Implements the LogAggregator strategy for request log entries.
 */
public class RequestLogAggregator implements LogAggregator {

    private static final List<Integer> PERCENTILES = Arrays.asList(50, 90, 95, 99);

    // Map to store response times grouped by API route
    private final Map<String, List<Double>> responseTimesMap = new HashMap<>();

    // Map to store status code counts grouped by API route
    private final Map<String, Map<String, Integer>> statusCodesMap = new HashMap<>();

    /**
     * Adds a log entry for aggregation.
     * Only processes RequestLogEntry instances.
     *
     * @param entry The LogEntry to aggregate.
     */
    @Override
    public void addLog(LogEntry entry) {
        if (entry instanceof RequestLogEntry) {
            RequestLogEntry requestLog = (RequestLogEntry) entry;
            String url = requestLog.getRequestUrl();
            double responseTime = requestLog.getResponseTimeMs();
            String statusCodeCategory = getStatusCategory(requestLog.getResponseStatus());

            // Add response time to the map
            responseTimesMap.computeIfAbsent(url, k -> new ArrayList<>()).add(responseTime);

            // Update status code counts
            statusCodesMap.computeIfAbsent(url, k -> initializeStatusCounts())
                    .merge(statusCodeCategory, 1, Integer::sum);
        }
    }

    /**
     * Aggregates the logs and returns the results as a Map.
     *
     * @return A Map of aggregated statistics for each API route.
     */
    @Override
    public Map<String, Map<String, Object>> aggregate() {
        Map<String, Map<String, Object>> result = new HashMap<>();

        for (String url : responseTimesMap.keySet()) {
            List<Double> responseTimes = responseTimesMap.get(url);
            Collections.sort(responseTimes);  // Sort response times for percentile calculations

            // Calculate statistics
            int min = (int) responseTimes.get(0).doubleValue();
            int max = (int) responseTimes.get(responseTimes.size() - 1).doubleValue();

            Map<String, Object> routeStats = new LinkedHashMap<>();
            routeStats.put("response_times", calculateResponseTimeStats(responseTimes, min, max));
            routeStats.put("status_codes", statusCodesMap.get(url));

            result.put(url, routeStats);
        }

        return result;
    }

    /**
     * Calculates response time statistics, including percentiles.
     *
     * @param values The sorted list of response times.
     * @param min    The minimum response time.
     * @param max    The maximum response time.
     * @return A map of response time statistics.
     */
    private Map<String, Object> calculateResponseTimeStats(List<Double> values, int min, int max) {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("min", min);

        for (int percentile : PERCENTILES) {
            double percentileValue = calculatePercentile(values, percentile);
            if (percentile == 99) {
                stats.put(percentile + "_percentile", roundToTwoDecimalPlaces(percentileValue));
            } else {
                stats.put(percentile + "_percentile", roundToOneDecimalPlace(percentileValue));
            }
        }

        stats.put("max", max);
        return stats;
    }

    /**
     * Calculates the percentile value from a sorted list of doubles.
     *
     * @param values     The sorted list of response times.
     * @param percentile The desired percentile (e.g., 50, 90).
     * @return The percentile value.
     */
    private double calculatePercentile(List<Double> values, int percentile) {
        int n = values.size();
        double rank = (percentile / 100.0) * (n - 1); // Rank index
        int lowerIndex = (int) Math.floor(rank);
        int upperIndex = (int) Math.ceil(rank);

        if (lowerIndex == upperIndex) {
            return values.get(lowerIndex);
        } else {
            // Linear interpolation
            double lowerValue = values.get(lowerIndex);
            double upperValue = values.get(upperIndex);
            return lowerValue + (rank - lowerIndex) * (upperValue - lowerValue);
        }
    }

    /**
     * Converts an HTTP status code into its category (e.g., "2XX", "4XX").
     *
     * @param statusCode The HTTP status code as a String.
     * @return The status code category (e.g., "2XX").
     */
    private String getStatusCategory(String statusCode) {
        // Added Handling for null and Empty Strings 
        if (statusCode == null || statusCode.isEmpty()) {
            return "Other";
        }
        if (statusCode.startsWith("2")) {
            return "2XX";
        } else if (statusCode.startsWith("4")) {
            return "4XX";
        } else if (statusCode.startsWith("5")) {
            return "5XX";
        }
        return "Other";
    }
    
    

    /**
     * Initializes status code counts with zero values for "2XX", "4XX", and "5XX".
     *
     * @return A map with initialized status code categories.
     */
    private Map<String, Integer> initializeStatusCounts() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("2XX", 0);
        counts.put("4XX", 0);
        counts.put("5XX", 0);
        return counts;
    }

    /**
     * Rounds a value to one decimal place.
     *
     * @param value The value to round.
     * @return The rounded value.
     */
    private double roundToOneDecimalPlace(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    /**
     * Rounds a value to two decimal places.
     *
     * @param value The value to round.
     * @return The rounded value.
     */
    private double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
