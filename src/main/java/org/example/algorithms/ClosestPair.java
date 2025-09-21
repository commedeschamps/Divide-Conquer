package org.example.algorithms;

import org.example.utils.Metrics;
import java.util.Arrays;
import java.util.Comparator;


public class ClosestPair {

    public static class Point {
        public final double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceTo(Point other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public String toString() {
            return String.format("(%.2f, %.2f)", x, y);
        }
    }

    public static class PointPair {
        public final Point p1, p2;
        public final double distance;

        public PointPair(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = p1.distanceTo(p2);
        }

        @Override
        public String toString() {
            return String.format("Distance: %.4f between %s and %s", distance, p1, p2);
        }
    }

    public static PointPair findClosestPair(Point[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Need at least 2 points");
        }

        // Sort points by x-coordinate
        Point[] sortedByX = points.clone();
        Arrays.sort(sortedByX, Comparator.comparingDouble(p -> p.x));
        Metrics.incrementAllocations(points.length);

        // Pre-sort by y-coordinate for strip operations
        Point[] sortedByY = points.clone();
        Arrays.sort(sortedByY, Comparator.comparingDouble(p -> p.y));
        Metrics.incrementAllocations(points.length);

        return closestPairRec(sortedByX, sortedByY, 0, points.length - 1);
    }

    private static PointPair closestPairRec(Point[] byX, Point[] byY, int left, int right) {
        Metrics.incrementDepth();

        try {
            int n = right - left + 1;

            // Base case: brute force for small arrays
            if (n <= 3) {
                return bruteForceClosest(byX, left, right);
            }

            // Divide
            int mid = left + (right - left) / 2;
            Point midPoint = byX[mid];

            // Split points by y-coordinate for recursive calls
            Point[] leftByY = new Point[mid - left + 1];
            Point[] rightByY = new Point[right - mid];
            Metrics.incrementAllocations(leftByY.length + rightByY.length);

            int leftIdx = 0, rightIdx = 0;
            for (Point p : byY) {
                if (p.x <= midPoint.x && leftIdx < leftByY.length) {
                    leftByY[leftIdx++] = p;
                } else if (rightIdx < rightByY.length) {
                    rightByY[rightIdx++] = p;
                }
            }

            // Conquer: find closest pairs in left and right halves
            PointPair leftClosest = closestPairRec(byX, leftByY, left, mid);
            PointPair rightClosest = closestPairRec(byX, rightByY, mid + 1, right);

            // Find the closer of the two
            PointPair closest = (leftClosest.distance <= rightClosest.distance) ? leftClosest : rightClosest;

            // Check strip around the dividing line
            PointPair stripClosest = closestInStrip(byY, midPoint.x, closest.distance);

            return (stripClosest != null && stripClosest.distance < closest.distance) ? stripClosest : closest;

        } finally {
            Metrics.decrementDepth();
        }
    }

    private static PointPair bruteForceClosest(Point[] points, int left, int right) {
        PointPair closest = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                Metrics.incrementComparisons();
                double distance = points[i].distanceTo(points[j]);
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = new PointPair(points[i], points[j]);
                }
            }
        }

        return closest;
    }

    private static PointPair closestInStrip(Point[] sortedByY, double midX, double delta) {
        // Create strip of points within delta distance from midline
        Point[] strip = new Point[sortedByY.length];
        int stripSize = 0;

        for (Point p : sortedByY) {
            if (Math.abs(p.x - midX) < delta) {
                strip[stripSize++] = p;
            }
        }

        if (stripSize < 2) return null;

        Metrics.incrementAllocations(stripSize);

        PointPair closest = null;
        double minDistance = delta;

        // Check each point against at most 7 neighbors
        for (int i = 0; i < stripSize; i++) {
            for (int j = i + 1; j < stripSize && (strip[j].y - strip[i].y) < minDistance; j++) {
                Metrics.incrementComparisons();
                double distance = strip[i].distanceTo(strip[j]);
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = new PointPair(strip[i], strip[j]);
                }
            }
        }

        return closest;
    }

    // Utility method for testing - generates random points
    public static Point[] generateRandomPoints(int n, double maxCoordinate) {
        Point[] points = new Point[n];
        java.util.Random random = new java.util.Random();

        for (int i = 0; i < n; i++) {
            double x = random.nextDouble() * maxCoordinate;
            double y = random.nextDouble() * maxCoordinate;
            points[i] = new Point(x, y);
        }

        return points;
    }
}
