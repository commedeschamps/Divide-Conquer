package org.example.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.infra.Blackhole;

import org.example.algorithms.*;
import org.example.utils.ArrayUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class AlgorithmsBenchmark {

    @Param({"1000", "5000", "10000", "50000"})
    private int size;

    private int[] randomArray;
    private int[] worstCaseArray;
    private ClosestPair.Point[] randomPoints;

    @Setup(Level.Trial)
    public void setup() {
        randomArray = ArrayUtils.generateRandomArray(size, size * 10);
        worstCaseArray = ArrayUtils.generateWorstCaseArray(size);
        randomPoints = ClosestPair.generateRandomPoints(size, 1000.0);
    }

    @Benchmark
    public void benchmarkMergeSort(Blackhole bh) {
        int[] arr = ArrayUtils.copyArray(randomArray);
        MergeSort.sort(arr);
        bh.consume(arr); // Prevent dead code elimination
    }

    @Benchmark
    public void benchmarkQuickSort(Blackhole bh) {
        int[] arr = ArrayUtils.copyArray(randomArray);
        QuickSort.sort(arr);
        bh.consume(arr); // Prevent dead code elimination
    }

    @Benchmark
    public void benchmarkQuickSortWorstCase(Blackhole bh) {
        int[] arr = ArrayUtils.copyArray(worstCaseArray);
        QuickSort.sort(arr);
        bh.consume(arr); // Prevent dead code elimination
    }

    @Benchmark
    public void benchmarkJavaSort(Blackhole bh) {
        int[] arr = ArrayUtils.copyArray(randomArray);
        Arrays.sort(arr);
        bh.consume(arr); // Prevent dead code elimination
    }

    @Benchmark
    public int benchmarkDeterministicSelect() {
        int[] arr = ArrayUtils.copyArray(randomArray);
        return DeterministicSelect.select(arr, size / 2); // Return result to prevent DCE
    }

    @Benchmark
    public ClosestPair.PointPair benchmarkClosestPair() {
        ClosestPair.Point[] points = Arrays.copyOf(randomPoints, randomPoints.length);
        return ClosestPair.findClosestPair(points); // Return result to prevent DCE
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AlgorithmsBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
