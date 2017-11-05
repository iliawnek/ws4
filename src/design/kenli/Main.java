package design.kenli;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {
        // Only accept one argument.
        if (args.length != 1) {
            System.out.println("Takes one and only one argument: path to .csv tweet data file.");
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

//            ArrayList<Double> values = new ArrayList<>();
//            for (Cluster cluster : dataset.getClusters()) values.add(cluster.getUserDiversity());
//            Collections.sort(values);
//            for (double value : values) System.out.println(value);

            // Remove noise.
            dataset.filterClusterSize(10);
            dataset.filterClusterUserDiversity(0.90); // 0.95 is better in 7days

            // TODO: Detect events.


            // Write modified tweet data to file.
            Main.outputCSV("output", dataset);
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

    // TODO: create output directory if it doesn't exist
    // TODO: https://stackoverflow.com/questions/8668905/directory-does-not-exist-with-filewriter
    private static void outputCSV(String filename, Dataset dataset) throws IOException {
        // Sort clusters by ID.
        ArrayList<Cluster> clusters = dataset.getClusters();
        clusters.sort(Comparator.comparingInt(Cluster::getId));

        ArrayList<String> lines = new ArrayList<>();
        for (Cluster cluster : clusters) {
            lines.addAll(cluster.toCSV());
        }
        writeLines(filename, lines);
    }

    private static void writeLines(String filename, ArrayList<String> lines) throws IOException {
        FileWriter writer = new FileWriter("output/" + filename + ".csv");
        for (String line : lines) {
            writer.write(line + "\n");
        }
        writer.close();
    }
}
