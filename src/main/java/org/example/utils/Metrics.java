package org.example.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Metrics {
    private static int maxRecursionDepth = 0;
    private static int currentRecursionDepth = 0;
    private static long comparisons = 0;
    private static long allocations = 0;
    private static long startTime = 0;
    private static long endTime = 0;

    public static void startTiming() {
        startTime = System.nanoTime();
    }

    public static void endTiming() {
        endTime = System.nanoTime();
    }

    public static long getElapsedTimeNanos() {
        return endTime - startTime;
    }

    public static void incrementDepth() {
        currentRecursionDepth++;
        if (currentRecursionDepth > maxRecursionDepth) {
            maxRecursionDepth = currentRecursionDepth;
        }
    }

    public static void decrementDepth() {
        currentRecursionDepth--;
    }

    public static void incrementComparisons() {
        comparisons++;
    }

    public static void incrementComparisons(long count) {
        comparisons += count;
    }

    public static void incrementAllocations() {
        allocations++;
    }

    public static void incrementAllocations(long count) {
        allocations += count;
    }

    public static void reset() {
        maxRecursionDepth = 0;
        currentRecursionDepth = 0;
        comparisons = 0;
        allocations = 0;
        startTime = 0;
        endTime = 0;
    }

    public static void printMetrics() {
        System.out.println("Max Recursion Depth: " + maxRecursionDepth);
        System.out.println("Comparisons: " + comparisons);
        System.out.println("Allocations: " + allocations);
        System.out.println("Time (ns): " + getElapsedTimeNanos());
        System.out.println("Time (ms): " + getElapsedTimeNanos() / 1_000_000.0);
    }

    public static void writeCSVHeader(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, false))) {
            writer.println("algorithm,n,time_ns,max_depth,comparisons,allocations");
        }
    }

    public static void writeCSVRow(String filename, String algorithm, int n) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            writer.printf("%s,%d,%d,%d,%d,%d%n",
                algorithm, n, getElapsedTimeNanos(), maxRecursionDepth, comparisons, allocations);
        }
    }

    // Getters for metrics
    public static int getMaxRecursionDepth() { return maxRecursionDepth; }
    public static long getComparisons() { return comparisons; }
    public static long getAllocations() { return allocations; }
}
