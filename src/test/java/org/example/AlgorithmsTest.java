package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import org.example.algorithms.*;
import org.example.utils.ArrayUtils;
import org.example.utils.Metrics;

import java.util.Arrays;

class AlgorithmsTest {

    @BeforeEach
    void setUp() {
        Metrics.reset();
    }

    @Test
    @DisplayName("MergeSort correctness test")
    void testMergeSortCorrectness() {
        // Test random array
        int[] arr = ArrayUtils.generateRandomArray(100, 1000);
        int[] expected = ArrayUtils.copyArray(arr);
        Arrays.sort(expected);

        MergeSort.sort(arr);

        assertArrayEquals(expected, arr, "MergeSort should sort array correctly");
        assertTrue(ArrayUtils.isSorted(arr), "Array should be sorted");
    }

    @Test
    @DisplayName("MergeSort edge cases")
    void testMergeSortEdgeCases() {
        // Empty array
        int[] empty = {};
        MergeSort.sort(empty);
        assertEquals(0, empty.length);

        // Single element
        int[] single = {42};
        MergeSort.sort(single);
        assertArrayEquals(new int[]{42}, single);

        // Two elements
        int[] two = {2, 1};
        MergeSort.sort(two);
        assertArrayEquals(new int[]{1, 2}, two);

        // Already sorted
        int[] sorted = {1, 2, 3, 4, 5};
        MergeSort.sort(sorted);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, sorted);

        // Reverse sorted
        int[] reverse = {5, 4, 3, 2, 1};
        MergeSort.sort(reverse);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, reverse);
    }

    @Test
    @DisplayName("QuickSort correctness test")
    void testQuickSortCorrectness() {
        // Test random array
        int[] arr = ArrayUtils.generateRandomArray(100, 1000);
        int[] expected = ArrayUtils.copyArray(arr);
        Arrays.sort(expected);

        QuickSort.sort(arr);

        assertArrayEquals(expected, arr, "QuickSort should sort array correctly");
        assertTrue(ArrayUtils.isSorted(arr), "Array should be sorted");
    }

    @Test
    @DisplayName("QuickSort depth bounded test")
    void testQuickSortDepthBounded() {
        // Test that recursion depth is bounded (approximately O(log n))
        int size = 1000;
        int[] arr = ArrayUtils.generateRandomArray(size, size * 10);

        Metrics.reset();
        QuickSort.sort(arr);

        int maxDepth = Metrics.getMaxRecursionDepth();
        int expectedMaxDepth = (int) (2 * Math.log(size) / Math.log(2)) + 10; // Some slack

        assertTrue(maxDepth <= expectedMaxDepth,
            String.format("Recursion depth %d should be bounded by ~2*log2(n)+C = %d", maxDepth, expectedMaxDepth));
    }

    @Test
    @DisplayName("Deterministic Select correctness test")
    void testDeterministicSelectCorrectness() {
        // Test with multiple random trials
        for (int trial = 0; trial < 10; trial++) {
            int[] arr = ArrayUtils.generateRandomArray(100, 1000);
            int[] sortedCopy = ArrayUtils.copyArray(arr);
            Arrays.sort(sortedCopy);

            for (int k : new int[]{0, 25, 49, 75, 99}) {
                int result = DeterministicSelect.select(ArrayUtils.copyArray(arr), k);
                assertEquals(sortedCopy[k], result,
                    String.format("Select should find correct k-th element for k=%d", k));
            }
        }
    }

    @Test
    @DisplayName("Deterministic Select edge cases")
    void testDeterministicSelectEdgeCases() {
        // Single element
        int[] single = {42};
        assertEquals(42, DeterministicSelect.select(single, 0));

        // Two elements
        int[] two = {2, 1};
        assertEquals(1, DeterministicSelect.select(ArrayUtils.copyArray(two), 0));
        assertEquals(2, DeterministicSelect.select(ArrayUtils.copyArray(two), 1));

        // Invalid inputs
        assertThrows(IllegalArgumentException.class, () ->
            DeterministicSelect.select(new int[]{1, 2, 3}, -1));
        assertThrows(IllegalArgumentException.class, () ->
            DeterministicSelect.select(new int[]{1, 2, 3}, 3));
        assertThrows(IllegalArgumentException.class, () ->
            DeterministicSelect.select(null, 0));
    }

    @Test
    @DisplayName("Closest Pair correctness test")
    void testClosestPairCorrectness() {
        // Test with known points
        ClosestPair.Point[] points = {
            new ClosestPair.Point(0, 0),
            new ClosestPair.Point(1, 0),
            new ClosestPair.Point(2, 0),
            new ClosestPair.Point(3, 0)
        };

        ClosestPair.PointPair result = ClosestPair.findClosestPair(points);
        assertEquals(1.0, result.distance, 0.0001, "Distance should be 1.0");
    }

    @Test
    @DisplayName("Closest Pair vs brute force")
    void testClosestPairAgainstBruteForce() {
        // Test against brute force for small arrays
        for (int trial = 0; trial < 5; trial++) {
            ClosestPair.Point[] points = ClosestPair.generateRandomPoints(20, 100.0);

            ClosestPair.PointPair dcResult = ClosestPair.findClosestPair(points);
            ClosestPair.PointPair bruteResult = bruteForceClosestPair(points);

            assertEquals(bruteResult.distance, dcResult.distance, 0.0001,
                "Divide-and-conquer result should match brute force");
        }
    }

    @Test
    @DisplayName("Closest Pair edge cases")
    void testClosestPairEdgeCases() {
        // Two points
        ClosestPair.Point[] two = {
            new ClosestPair.Point(0, 0),
            new ClosestPair.Point(3, 4)
        };
        ClosestPair.PointPair result = ClosestPair.findClosestPair(two);
        assertEquals(5.0, result.distance, 0.0001);

        // Invalid inputs
        assertThrows(IllegalArgumentException.class, () ->
            ClosestPair.findClosestPair(new ClosestPair.Point[]{new ClosestPair.Point(0, 0)}));
        assertThrows(IllegalArgumentException.class, () ->
            ClosestPair.findClosestPair(null));
    }

    @Test
    @DisplayName("Metrics tracking test")
    void testMetricsTracking() {
        int[] arr = ArrayUtils.generateRandomArray(100, 1000);

        Metrics.reset();
        Metrics.startTiming();
        MergeSort.sort(arr);
        Metrics.endTiming();

        assertTrue(Metrics.getElapsedTimeNanos() > 0, "Should track elapsed time");
        assertTrue(Metrics.getMaxRecursionDepth() > 0, "Should track recursion depth");
        assertTrue(Metrics.getComparisons() > 0, "Should track comparisons");
        assertTrue(Metrics.getAllocations() > 0, "Should track allocations");
    }

    // Helper method for brute force closest pair (for testing)
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
