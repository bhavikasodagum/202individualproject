package com.logparser;

import com.logparser.aggregators.*;
import com.logparser.handlers.*;
import com.logparser.logs.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class App {

    public static void main(String[] args) {
        if (args.length < 2 || !args[0].equals("--file")) {
            System.err.println("Usage: java App --file <input_file>");
            return;
        }

        String inputFilePath = args[1];
        try {
            List<String> logLines = Files.readAllLines(Paths.get(inputFilePath));

            // Initialize log aggregators
            LogAggregator apmAggregator = new APMLogAggregator();
            LogAggregator applicationAggregator = new ApplicationLogAggregator();
            LogAggregator requestAggregator = new RequestLogAggregator();

            // Set up Chain of Responsibility for parsing
            LogHandler apmHandler = new APMLogHandler();
            LogHandler applicationHandler = new ApplicationLogHandler();
            LogHandler requestHandler = new RequestLogHandler();
            apmHandler.setNextHandler(applicationHandler);
            applicationHandler.setNextHandler(requestHandler);

            // Process log lines
            for (String logLine : logLines) {
                LogEntry entry = apmHandler.handle(logLine);
                if (entry instanceof APMLogEntry) {
                    apmAggregator.addLog(entry);
                } else if (entry instanceof ApplicationLogEntry) {
                    applicationAggregator.addLog(entry);
                } else if (entry instanceof RequestLogEntry) {
                    requestAggregator.addLog(entry);
                } else {
                    // Log line couldn't be parsed; skip
                    System.err.println("Skipped invalid log line: " + logLine);
                }
            }

            // Aggregate results
            Map<String, ?> apmResults = apmAggregator.aggregate();
            Map<String, ?> applicationResults = applicationAggregator.aggregate();
            Map<String, ?> requestResults = requestAggregator.aggregate();

            // Write results to JSON files
            writeToJSON("apm.json", apmResults);
            writeToJSON("application.json", applicationResults);
            writeToJSON("request.json", requestResults);

            System.out.println("Processing completed. JSON files generated.");

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Writes a Map to a JSON file using Jackson.
     *
     * @param fileName The output file name.
     * @param data     The data to write.
     */
    private static void writeToJSON(String fileName, Map<String, ?> data) {
        ObjectMapper mapper = new ObjectMapper();
        try (FileWriter writer = new FileWriter(fileName)) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(writer, data);
        } catch (IOException e) {
            System.err.println("Error writing to " + fileName + ": " + e.getMessage());
        }
    }
}
