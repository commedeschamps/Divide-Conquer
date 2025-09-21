package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import org.example.algorithms.*;
import org.example.utils.ArrayUtils;
import org.example.utils.Metrics;

import java.util.Arrays;


class ExtendedAlgorithmsTest {

    @Test
    @DisplayName("QuickSort depth bounded verification")
    void testQuickSortDepthBounded() {
        for (int size : new int[]{100, 500, 1000, 5000, 10000}) {
            int[] arr = ArrayUtils.generateRandomArray(size, size * 10);

            Metrics.reset();
            QuickSort.sort(arr);

            int maxDepth = Metrics.getMaxRecursionDepth();
            int expectedMaxDepth = 2 * (int) Math.floor(Math.log(size) / Math.log(2)) + 5; // O(1) constant

            assertTrue(maxDepth <= expectedMaxDepth,
                String.format("QS depth %d should be ≲ ~2*floor(log2 %d) + O(1) = %d",
                    maxDepth, size, expectedMaxDepth));
            assertTrue(ArrayUtils.isSorted(arr), "Array should be sorted correctly");
        }
    }

    @RepeatedTest(100)
    @DisplayName("Deterministic Select: 100 random trials vs Arrays.sort")
    void testDeterministicSelectVsArraysSort() {
        int size = 200 + (int)(Math.random() * 800); // Random size 200-1000
        int[] arr = ArrayUtils.generateRandomArray(size, size * 10);
        int k = (int)(Math.random() * size); // Random k

        // Get expected result using Arrays.sort
        int[] sortedCopy = ArrayUtils.copyArray(arr);
        Arrays.sort(sortedCopy);
        int expected = sortedCopy[k];

        // Test our implementation
        int result = DeterministicSelect.select(ArrayUtils.copyArray(arr), k);

        assertEquals(expected, result,
            String.format("Select failed for size=%d, k=%d", size, k));
    }

    @Test
    @DisplayName("Closest Pair validation against O(n²) brute force")
    void testClosestPairVsBruteForce() {
        // Test on small n (≤ 2000) as required
        for (int size : new int[]{10, 50, 100, 500, 1000, 2000}) {
            ClosestPair.Point[] points = ClosestPair.generateRandomPoints(size, 1000.0);

            // O(n log n) divide-and-conquer result
            ClosestPair.PointPair dcResult = ClosestPair.findClosestPair(points);

            // O(n²) brute force result
            ClosestPair.PointPair bruteResult = bruteForceClosestPair(points);

            assertEquals(bruteResult.distance, dcResult.distance, 1e-10,
                String.format("Results don't match for size %d", size));
        }
    }

    @Test
    @DisplayName("Sorting correctness on adversarial arrays")
    void testSortingAdversarialArrays() {
        // Test various adversarial cases
        int[][] adversarialCases = {
            {5, 4, 3, 2, 1}, // Reverse sorted
            {1, 1, 1, 1, 1}, // All duplicates
            {1, 3, 2, 4, 3, 5, 4}, // Many duplicates
            {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, -1, 1}, // Extreme values
            {} // Empty array
        };

        for (int[] testCase : adversarialCases) {
            // Test MergeSort
            int[] mergeCopy = ArrayUtils.copyArray(testCase);
            int[] expected = ArrayUtils.copyArray(testCase);
            Arrays.sort(expected);

            MergeSort.sort(mergeCopy);
            assertArrayEquals(expected, mergeCopy, "MergeSort failed on adversarial case");

            // Test QuickSort
            int[] quickCopy = ArrayUtils.copyArray(testCase);
            QuickSort.sort(quickCopy);
            assertArrayEquals(expected, quickCopy, "QuickSort failed on adversarial case");
        }
    }

    @Test
    @DisplayName("Edge cases: duplicates and tiny arrays")
    void testEdgeCasesAndDuplicates() {
        // Tiny arrays
        int[] single = {42};
        MergeSort.sort(single);
        assertArrayEquals(new int[]{42}, single);

        QuickSort.sort(single);
        assertArrayEquals(new int[]{42}, single);

        // Array with many duplicates
        int[] duplicates = new int[1000];
        Arrays.fill(duplicates, 42);

        int[] mergeCopy = ArrayUtils.copyArray(duplicates);
        MergeSort.sort(mergeCopy);
        assertArrayEquals(duplicates, mergeCopy);

        int[] quickCopy = ArrayUtils.copyArray(duplicates);
        QuickSort.sort(quickCopy);
        assertArrayEquals(duplicates, quickCopy);

        // Select with duplicates
        int result = DeterministicSelect.select(ArrayUtils.copyArray(duplicates), 500);
        assertEquals(42, result);
    }

    // Helper method for O(n²) brute force closest pair
    private ClosestPair.PointPair bruteForceClosestPair(ClosestPair.Point[] points) {
        double minDistance = Double.MAX_VALUE;
        ClosestPair.PointPair closest = null;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double distance = points[i].distanceTo(points[j]);
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = new ClosestPair.PointPair(points[i], points[j]);
                }
            }
        }

        return closest;
    }
}
