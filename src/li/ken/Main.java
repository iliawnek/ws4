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

            // Remove noise.
            System.out.println("original: " + dataset.size());
            dataset.filterClusterSize(10);
            System.out.println("cluster size > 10: " + dataset.size());
            dataset.filterClusterUserDiversity(0.90);
            System.out.println("cluster user diversity > 0.9: " + dataset.size());
            int threshold = 5;
            int filterSize = 20;
            int leniency = 3;
            dataset.markBurstingClusters(5, threshold, filterSize, leniency, 2);
            dataset.markBurstingClusters(30, threshold, filterSize, leniency, 2);
            dataset.markBurstingClusters(60, threshold, filterSize, leniency, 2);
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
