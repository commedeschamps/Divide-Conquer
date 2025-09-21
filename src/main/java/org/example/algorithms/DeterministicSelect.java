package org.example.algorithms;

import org.example.utils.Metrics;
import org.example.utils.ArrayUtils;


public class DeterministicSelect {


    public static int select(int[] arr, int k) {
        if (arr == null || k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("Invalid input");
        }

        return select(arr, 0, arr.length - 1, k);
    }

    private static int select(int[] arr, int low, int high, int k) {
        Metrics.incrementDepth();

        try {
            // Base case: small arrays
            if (high - low + 1 <= 5) {
                ArrayUtils.insertionSort(arr, low, high);
                return arr[k];
            }

            // Find median of medians as pivot
            int pivotValue = medianOfMedians(arr, low, high);

            // Find the pivot position
            int pivotIndex = findPivotIndex(arr, low, high, pivotValue);

            // Partition around the pivot
            int partitionIndex = ArrayUtils.partition(arr, low, high, pivotIndex);

            // Recurse on the appropriate side
            if (k == partitionIndex) {
                return arr[k];
            } else if (k < partitionIndex) {
                return select(arr, low, partitionIndex - 1, k);
            } else {
                return select(arr, partitionIndex + 1, high, k);
            }

        } finally {
            Metrics.decrementDepth();
        }
    }

    private static int medianOfMedians(int[] arr, int low, int high) {
        int n = high - low + 1;
        int numGroups = (n + 4) / 5; // ceiling division

        // Create array for medians
        int[] medians = new int[numGroups];
        Metrics.incrementAllocations(numGroups);

        // Find median of each group of 5
        for (int i = 0; i < numGroups; i++) {
            int groupLow = low + i * 5;
            int groupHigh = Math.min(groupLow + 4, high);

            // Sort the group and take median
            ArrayUtils.insertionSort(arr, groupLow, groupHigh);
            int medianIndex = groupLow + (groupHigh - groupLow) / 2;
            medians[i] = arr[medianIndex];
        }

        // Recursively find median of medians
        if (numGroups == 1) {
            return medians[0];
        } else {
            return select(medians, 0, numGroups - 1, numGroups / 2);
        }
    }

    private static int findPivotIndex(int[] arr, int low, int high, int pivotValue) {
        for (int i = low; i <= high; i++) {
            if (arr[i] == pivotValue) {
                return i;
            }
        }
        throw new RuntimeException("Pivot value not found in array");
    }
}
