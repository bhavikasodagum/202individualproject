package com.logparser.aggregators;

import com.logparser.logs.ApplicationLogEntry;
import com.logparser.logs.LogEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * Aggregator for Application logs.
 * Implements the LogAggregator strategy for counting logs by severity level.
 */
public class ApplicationLogAggregator implements LogAggregator {

    private final Map<String, Integer> severityCounts = new HashMap<>();

    /**
     * Adds a log entry for aggregation.
     * Only processes ApplicationLogEntry instances.
     *
     * @param entry The LogEntry to aggregate.
     */
    @Override
    public void addLog(LogEntry entry) {
        if (entry instanceof ApplicationLogEntry) {
            ApplicationLogEntry appLog = (ApplicationLogEntry) entry;
            String level = appLog.getLevel();
            severityCounts.put(level, severityCounts.getOrDefault(level, 0) + 1);
        }
    }

    /**
     * Aggregates the logs and returns the results as a Map.
     *
     * @return A Map containing severity levels as keys and their counts as values.
     */
    @Override
    public Map<String, Integer> aggregate() {
        return severityCounts;
    }
}
