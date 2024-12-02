package com.logparser.aggregators;

import com.logparser.logs.LogEntry;

import java.util.Map;

/**
 * Interface for aggregating logs.
 * Implemented by concrete aggregators for specific log types.
 */
public interface LogAggregator {

    /**
     * Adds a log entry to the aggregator.
     *
     * @param entry The LogEntry to aggregate.
     */
    void addLog(LogEntry entry);

    /**
     * Performs aggregation on the added logs and returns the results.
     *
     * @return A Map containing aggregated results.
     */
    Map<String, ?> aggregate();
}
