package org.example.algorithms;

import org.example.utils.Metrics;
import org.example.utils.ArrayUtils;


public class MergeSort {
    private static final int INSERTION_SORT_CUTOFF = 16;

    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;

        // Allocate auxiliary array once to minimize allocations
        int[] aux = new int[arr.length];
        Metrics.incrementAllocations(arr.length);

        mergeSort(arr, aux, 0, arr.length - 1);
    }

    private static void mergeSort(int[] arr, int[] aux, int low, int high) {
        Metrics.incrementDepth();

        try {
            // Base case: use insertion sort for small subarrays
            if (high - low + 1 <= INSERTION_SORT_CUTOFF) {
                ArrayUtils.insertionSort(arr, low, high);
                return;
            }

            int mid = low + (high - low) / 2;

            // Divide: recursively sort both halves
            mergeSort(arr, aux, low, mid);
            mergeSort(arr, aux, mid + 1, high);

            // Conquer: merge the sorted halves
            merge(arr, aux, low, mid, high);

        } finally {
            Metrics.decrementDepth();
        }
    }

    private static void merge(int[] arr, int[] aux, int low, int mid, int high) {
        // Copy to auxiliary array
        System.arraycopy(arr, low, aux, low, high - low + 1);

        int i = low;      // left subarray pointer
        int j = mid + 1;  // right subarray pointer
        int k = low;      // merged array pointer

        // Merge back to original array
        while (i <= mid && j <= high) {
            Metrics.incrementComparisons();
            if (aux[i] <= aux[j]) {
                arr[k++] = aux[i++];
            } else {
                arr[k++] = aux[j++];
            }
        }

        // Copy remaining elements
        while (i <= mid) {
            arr[k++] = aux[i++];
        }

        while (j <= high) {
            arr[k++] = aux[j++];
        }
    }
}
