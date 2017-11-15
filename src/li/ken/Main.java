package li.ken;

import java.io.*;
import java.util.ArrayList;

public class Main {

    // Perform event detection on an input tweet dataset .csv file.
    public static void main(String[] args) {
        // Only accept one argument.
        if (args.length != 1) {
            System.out.println("Only one argument: path to .csv tweet file");
            return;
        }

        String inputFilename = args[0];
        try {
            // Import tweet data from file.
            BufferedReader reader = new BufferedReader(new FileReader(inputFilename));
            Dataset dataset = new Dataset();
            String line;
            while ((line = reader.readLine()) != null) {
               dataset.addTweet(line);
            }
            reader.close();

            // Algorithm parameters.
            final int minClusterSize = 10;
            final double minDiversity = 0.9;
            final int[] windowSizes = {5, 30, 60};
            final int threshold = 5;
            final int filterSize = 20;
            final int leniency = 3;
            final int minBurstFactor = 2;

            // Filter clusters.
            System.out.println("original: " + dataset.size());
            dataset.filterClusterSize(minClusterSize);
            System.out.println("cluster size > " + minClusterSize + ": " + dataset.size());
            dataset.filterClusterUserDiversity(minDiversity);
            System.out.println("cluster user diversity > " + minDiversity + ": " + dataset.size());
            for (int windowSize : windowSizes)
                dataset.markBurstingClusters(windowSize, threshold, filterSize, leniency, minBurstFactor);
            dataset.filterNonBurstingClusters();
            System.out.println("non-bursting: " + dataset.size());

            // Write modified tweet data to file.
            Main.writeLines("output", dataset.toCSV());
        }
        catch (FileNotFoundException e) {
            System.out.printf("Unable to open %s.\n", inputFilename);
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.printf("Error reading %s.\n", inputFilename);
            e.printStackTrace();
        }
    }

    // Writes a list of strings to "./output.csv", where each string is treated as a line.
    private static void writeLines(String filename, ArrayList<String> lines) throws IOException {
        FileWriter writer = new FileWriter(filename + ".csv");
        for (String line : lines) {
            writer.write(line + "\n");
        }
        writer.close();
    }
}
