package li.ken;

import java.util.List;

// Useful utility functions.
class Utilities {
    // Returns the mean value if a list of values.
    static double mean(List<Double> values) {
        double sum = 0.0;
        for (double d : values) {
            sum += d;
        }
        return sum/values.size();
    }

    // Returns the standard deviation of a list of values.
    static double standardDeviation(List<Double> values) {
        double mean = mean(values);
        return standardDeviation(values, mean);
    }

    // Returns the standard deviation of a list of values given a pre-calculated mean.
    static double standardDeviation(List<Double> values, double mean) {
        double temp = 0.0;
        for (double d : values) {
            temp += (d - mean) * (d - mean);
        }
        double variance = temp / (values.size() - 1);
        return Math.sqrt(variance);
    }

    // Converts from minutes to milliseconds.
    static long minutesToMillis(int minutes) {
        return minutes * 60 * 1000;
    }
}
