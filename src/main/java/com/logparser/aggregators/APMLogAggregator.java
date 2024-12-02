package com.logparser.aggregators;

import com.logparser.logs.APMLogEntry;
import com.logparser.logs.LogEntry;

import java.util.*;
import java.util.stream.Collectors;

public class APMLogAggregator implements LogAggregator {

    private final Map<String, List<Double>> metricValues = new HashMap<>();

    @Override
    public void addLog(LogEntry logEntry) {
        if (logEntry instanceof APMLogEntry) {
            APMLogEntry apmLog = (APMLogEntry) logEntry;
            String metric = apmLog.getMetric();
            double value = apmLog.getValue();

            // Add value to the corresponding metric list
            metricValues.computeIfAbsent(metric, k -> new ArrayList<>()).add(value);
        }
    }

    @Override
    public Map<String, Object> aggregate() {
        Map<String, Object> result = new LinkedHashMap<>();

        for (Map.Entry<String, List<Double>> entry : metricValues.entrySet()) {
            String metric = entry.getKey();
            List<Double> values = entry.getValue();

            // Sort values for accurate calculations
            List<Double> sortedValues = values.stream().sorted().collect(Collectors.toList());

            // Calculate statistics
            int size = sortedValues.size();
            double min = sortedValues.get(0);
            double max = sortedValues.get(size - 1);
            double average = sortedValues.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double median = calculateMedian(sortedValues);

            // Format results
            Map<String, Object> stats = new LinkedHashMap<>();
            stats.put("minimum", (int) min);  // Minimum as integer
            stats.put("median", median);     // Median as floating-point
            stats.put("average", average);   // Average as floating-point
            stats.put("max", (int) max);     // Maximum as integer
            result.put(metric, stats);
        }

        return result;
    }

    /**
     * Helper method to calculate the median.
     *
     * @param sortedValues The sorted list of values.
     * @return The median value.
     */
    private double calculateMedian(List<Double> sortedValues) {
        int size = sortedValues.size();
        if (size % 2 == 1) {
            // Odd-sized list: middle element
            return sortedValues.get(size / 2);
        } else {
            // Even-sized list: average of two middle elements
            double lower = sortedValues.get((size / 2) - 1);
            double upper = sortedValues.get(size / 2);
            return (lower + upper) / 2.0;
        }
    }
}
