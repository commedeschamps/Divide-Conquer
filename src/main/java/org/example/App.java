package org.example;

import org.example.algorithms.*;
import org.example.utils.ArrayUtils;
import org.example.utils.Metrics;

import java.io.IOException;
import java.util.Arrays;


public class App {

    public static void main(String[] args) {
        if (args.length == 0) {
            runAllTests();
        } else {
            processCliArgs(args);
        }
    }

    private static void runAllTests() {
        System.out.println("=== Divide and Conquer Algorithms Demo ===\n");

        // Test MergeSort
        testMergeSort();

        // Test QuickSort
        testQuickSort();

        // Test Deterministic Select
        testDeterministicSelect();

        // Test Closest Pair
        testClosestPair();

        // Generate performance report
        generatePerformanceReport();
    }

    private static void testMergeSort() {
        System.out.println("--- MergeSort Test ---");

        int[] arr = ArrayUtils.generateRandomArray(1000, 10000);
        int[] original = ArrayUtils.copyArray(arr);

        Metrics.reset();
        Metrics.startTiming();
        MergeSort.sort(arr);
        Metrics.endTiming();

        System.out.println("Array size: " + original.length);
        System.out.println("Sorted correctly: " + ArrayUtils.isSorted(arr));
        Metrics.printMetrics();
        System.out.println();
    }

    private static void testQuickSort() {
        System.out.println("--- QuickSort Test ---");

        int[] arr = ArrayUtils.generateRandomArray(1000, 10000);
        int[] original = ArrayUtils.copyArray(arr);

        Metrics.reset();
        Metrics.startTiming();
        QuickSort.sort(arr);
        Metrics.endTiming();

        System.out.println("Array size: " + original.length);
        System.out.println("Sorted correctly: " + ArrayUtils.isSorted(arr));
        Metrics.printMetrics();
        System.out.println();
    }

    private static void testDeterministicSelect() {
        System.out.println("--- Deterministic Select Test ---");

        int[] arr = ArrayUtils.generateRandomArray(1000, 10000);
        int[] sortedCopy = ArrayUtils.copyArray(arr);
        Arrays.sort(sortedCopy);

        int k = 499; // Find median (0-indexed)

        Metrics.reset();
        Metrics.startTiming();
        int result = DeterministicSelect.select(ArrayUtils.copyArray(arr), k);
        Metrics.endTiming();

        System.out.println("Array size: " + arr.length);
        System.out.println("k = " + k);
        System.out.println("Selected element: " + result);
        System.out.println("Expected (sorted[k]): " + sortedCopy[k]);
        System.out.println("Correct: " + (result == sortedCopy[k]));
        Metrics.printMetrics();
        System.out.println();
    }

    private static void testClosestPair() {
        System.out.println("--- Closest Pair Test ---");

        ClosestPair.Point[] points = ClosestPair.generateRandomPoints(1000, 1000.0);

        Metrics.reset();
        Metrics.startTiming();
        ClosestPair.PointPair result = ClosestPair.findClosestPair(points);
        Metrics.endTiming();

        System.out.println("Number of points: " + points.length);
        System.out.println("Closest pair: " + result);
        Metrics.printMetrics();
        System.out.println();
    }

    private static void generatePerformanceReport() {
        System.out.println("--- Generating Performance Report ---");

        try {
            String filename = "performance_report.csv";
            Metrics.writeCSVHeader(filename);

            // Test different array sizes
            int[] sizes = {100, 500, 1000, 5000, 10000, 50000};

            for (int size : sizes) {
                System.out.println("Testing size: " + size);

                // Test MergeSort
                testAlgorithmPerformance("MergeSort", size, filename);

                // Test QuickSort
                testAlgorithmPerformance("QuickSort", size, filename);

                // Test Select (smaller sizes only due to O(n) complexity)
                if (size <= 10000) {
                    testAlgorithmPerformance("Select", size, filename);
                }

                // Test ClosestPair (smaller sizes for demo)
                if (size <= 5000) {
                    testAlgorithmPerformance("ClosestPair", size, filename);
                }
            }

            System.out.println("Performance report saved to: " + filename);

        } catch (IOException e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }

    private static void testAlgorithmPerformance(String algorithm, int size, String filename) throws IOException {
        Metrics.reset();

        switch (algorithm) {
            case "MergeSort":
                int[] arr1 = ArrayUtils.generateRandomArray(size, size * 10);
                Metrics.startTiming();
                MergeSort.sort(arr1);
                Metrics.endTiming();
                break;

            case "QuickSort":
                int[] arr2 = ArrayUtils.generateRandomArray(size, size * 10);
                Metrics.startTiming();
                QuickSort.sort(arr2);
                Metrics.endTiming();
                break;

            case "Select":
                int[] arr3 = ArrayUtils.generateRandomArray(size, size * 10);
                Metrics.startTiming();
                DeterministicSelect.select(arr3, size / 2);
                Metrics.endTiming();
                break;

            case "ClosestPair":
                ClosestPair.Point[] points = ClosestPair.generateRandomPoints(size, 1000.0);
                Metrics.startTiming();
                ClosestPair.findClosestPair(points);
                Metrics.endTiming();
                break;
        }

        Metrics.writeCSVRow(filename, algorithm, size);
    }

    private static void processCliArgs(String[] args) {
        // Simple CLI processing - can be extended
        String algorithm = args[0].toLowerCase();
        int size = args.length > 1 ? Integer.parseInt(args[1]) : 1000;

        System.out.println("Running " + algorithm + " with size " + size);

        switch (algorithm) {
            case "mergesort":
                int[] arr1 = ArrayUtils.generateRandomArray(size, size * 10);
                Metrics.reset();
                Metrics.startTiming();
                MergeSort.sort(arr1);
                Metrics.endTiming();
                Metrics.printMetrics();
                break;

            case "quicksort":
                int[] arr2 = ArrayUtils.generateRandomArray(size, size * 10);
                Metrics.reset();
                Metrics.startTiming();
                QuickSort.sort(arr2);
                Metrics.endTiming();
                Metrics.printMetrics();
                break;

            case "select":
                int[] arr3 = ArrayUtils.generateRandomArray(size, size * 10);
                Metrics.reset();
                Metrics.startTiming();
                int result = DeterministicSelect.select(arr3, size / 2);
                Metrics.endTiming();
                System.out.println("Selected element: " + result);
                Metrics.printMetrics();
                break;

            case "closest":
                ClosestPair.Point[] points = ClosestPair.generateRandomPoints(size, 1000.0);
                Metrics.reset();
                Metrics.startTiming();
                ClosestPair.PointPair pair = ClosestPair.findClosestPair(points);
                Metrics.endTiming();
                System.out.println("Closest pair: " + pair);
                Metrics.printMetrics();
                break;

            default:
                System.out.println("Unknown algorithm: " + algorithm);
                System.out.println("Available: mergesort, quicksort, select, closest");
        }
    }
}
