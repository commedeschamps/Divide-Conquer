package org.example.algorithms;

import org.example.utils.Metrics;
import org.example.utils.ArrayUtils;


public class QuickSort {

    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(int[] arr, int low, int high) {
        while (low < high) {
            // Randomized pivot selection
            int pivotIndex = ArrayUtils.randomPivot(low, high);
            int pivot = ArrayUtils.partition(arr, low, high, pivotIndex);

            // Recurse on smaller partition, iterate on larger
            // This ensures O(log n) stack depth even in worst case
            if (pivot - low < high - pivot) {
                // Left partition is smaller - recurse on it
                Metrics.incrementDepth();
                try {
                    quickSort(arr, low, pivot - 1);
                } finally {
                    Metrics.decrementDepth();
                }
                low = pivot + 1; // Tail recursion elimination for right partition
            } else {
                // Right partition is smaller - recurse on it
                Metrics.incrementDepth();
                try {
                    quickSort(arr, pivot + 1, high);
                } finally {
                    Metrics.decrementDepth();
                }
                high = pivot - 1; // Tail recursion elimination for left partition
            }
        }
    }
}
