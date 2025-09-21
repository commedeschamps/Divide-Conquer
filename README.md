# Divide and Conquer Algorithms

Implementation of classic divide-and-conquer algorithms with performance analysis and safe recursion patterns.

## Learning Goals

- Implement classic divide-and-conquer algorithms with safe recursion patterns
- Analyze running-time recurrences using Master Theorem and Akra-Bazzi intuition; validate with measurements
- Collect metrics (time, recursion depth, comparisons/allocations) and communicate results via a short report and clean Git history

## Implemented Algorithms

### 1. MergeSort (O(n log n))
- **Features**: Linear merge; reusable buffer; small-n cut-off (e.g., insertion sort)
- **Analysis**: T(n) = 2T(n/2) + O(n) - Master Theorem Case 2, where a=2, b=2, f(n)=n
- **Result**: Θ(n log n) in all cases

### 2. QuickSort (robust)
- **Features**: Randomized pivot; recurse on smaller partition, iterate over larger one
- **Analysis**: Bounded stack ≈ O(log n) typical case
- **Result**: O(n log n) average, O(n²) worst case

### 3. Deterministic Select (Median-of-Medians, O(n))
- **Features**: Group by 5, median of medians as pivot, in-place partitioning
- **Analysis**: T(n) ≤ T(n/5) + T(7n/10) + O(n) - Akra-Bazzi: 1/5 + 7/10 = 9/10 < 1
- **Result**: O(n) worst case

### 4. Closest Pair of Points (2D, O(n log n))
- **Features**: Sort by x, recursive split, "strip" check by y order (classic 7–8 neighbor scan)
- **Analysis**: T(n) = 2T(n/2) + O(n) - Master Theorem Case 2
- **Result**: O(n log n)

## Architecture

### Depth and Allocation Control
- **Metrics class**: Tracks recursion depth, number of comparisons, allocations and time
- **Bounded recursion**: QuickSort uses tail recursion elimination to limit stack
- **Buffer reuse**: MergeSort allocates auxiliary array once
- **Cutoff strategies**: Switch to insertion sort for small arrays

### Project Structure
```
src/
├── main/java/org/example/
│   ├── App.java                    # CLI application
│   ├── algorithms/
│   │   ├── MergeSort.java         # Merge sort
│   │   ├── QuickSort.java         # Quick sort
│   │   ├── DeterministicSelect.java # Deterministic selection
│   │   └── ClosestPair.java       # Closest pair of points
│   └── utils/
│       ├── Metrics.java           # Metrics collection
│       └── ArrayUtils.java        # Array utilities
└── test/java/org/example/
    └── AlgorithmsTest.java        # Correctness tests
```

## Build and Run

### Compilation
```bash
mvn clean compile
```

### Run Tests
```bash
mvn test
```

### Run Demo
```bash
mvn exec:java -Dexec.mainClass="org.example.App"
```

### Run Specific Algorithm
```bash
mvn exec:java -Dexec.mainClass="org.example.App" -Dexec.args="mergesort 10000"
mvn exec:java -Dexec.mainClass="org.example.App" -Dexec.args="quicksort 10000"
mvn exec:java -Dexec.mainClass="org.example.App" -Dexec.args="select 10000"
mvn exec:java -Dexec.mainClass="org.example.App" -Dexec.args="closest 1000"
```

## Performance Analysis

### Recurrence Relations

#### MergeSort
- **Recurrence**: T(n) = 2T(n/2) + Θ(n)
- **Master Theorem**: Case 2 (a=2, b=2, f(n)=n, log₂(2)=1)
- **Result**: T(n) = Θ(n log n)

#### QuickSort
- **Average case**: T(n) = 2T(n/2) + Θ(n) = Θ(n log n)
- **Worst case**: T(n) = T(n-1) + Θ(n) = Θ(n²)
- **Stack depth**: O(log n) thanks to smaller-first recursion

#### Deterministic Select
- **Recurrence**: T(n) ≤ T(n/5) + T(7n/10) + Θ(n)
- **Akra-Bazzi**: p where 1/5^p + (7/10)^p = 1, solution p=1
- **Result**: T(n) = Θ(n)

#### Closest Pair
- **Recurrence**: T(n) = 2T(n/2) + Θ(n)
- **Master Theorem**: Case 2
- **Result**: T(n) = Θ(n log n)

### Metrics
The program collects the following metrics:
- **Execution time** (nanoseconds)
- **Maximum recursion depth**
- **Number of comparisons**
- **Number of allocations**

Results are saved to `performance_report.csv` for further analysis.

## Testing

### Sorting Correctness
- Testing on random and adversarial arrays
- Verification of bounded recursion depth for QuickSort
- Edge cases (empty arrays, duplicates)

### Deterministic Select
- Compare result with `Arrays.sort(a)[k]` across 100 random trials
- Edge case testing

### Closest Pair
- Validation against O(n²) algorithm on small n (≤ 2000)
- Use fast version on large n

## Dependencies

- **Java 11+**
- **JUnit 5** for testing
- **JMH** for benchmarks
- **Maven** for build

## Generate Plots

To generate performance analysis plots:
```bash
python quick_plots.py
```

This will create `algorithm_analysis_plots.png` with:
- Time vs input size (linear and log-log scales)
- Recursion depth vs input size
- Comparisons vs input size
- Performance summary statistics

## Authors

Project implemented as part of divide-and-conquer algorithms study with focus on practical performance analysis and implementation correctness.
