package design.kenli;

import java.util.List;

class Utilities {
    static double mean(List<Double> values) {
        double sum = 0.0;
        for (double d : values) {
            sum += d;
        }
        return sum/values.size();
    }

    static double standardDeviation(List<Double> values) {
        double mean = mean(values);
        return standardDeviation(values, mean);
    }

    static double standardDeviation(List<Double> values, double mean) {
        double temp = 0.0;
        for (double d : values) {
            temp += (d - mean) * (d - mean);
        }
        double variance = temp / (values.size() - 1);
        return Math.sqrt(variance);
    }

    static long minutesToMillis(int minutes) {
        return minutes * 60 * 1000;
    }
}
