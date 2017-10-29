package design.kenli;

import java.io.*;
import java.util.ArrayList;
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

            // TODO: filter out the noise.

            // Write modified tweet data to file.
            Main.outputCSV("unchanged", dataset);
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

    private static void outputCSV(String filename, Dataset dataset) throws IOException {
        // Sort clusters by ID.
        ArrayList<Cluster> clusters = new ArrayList<>();
        for (Entity entity : dataset.getEntities()) {
            for (Cluster cluster : entity.getClusters()) {
                clusters.add(cluster);
            }
        }
        clusters.sort(Comparator.comparingInt(Cluster::getId));

        String outputFilename = filename + ".csv";
        FileWriter writer = new FileWriter(outputFilename);
        for (Cluster cluster : clusters) {
            for (String line : cluster.toCSV()) {
                writer.write(line);
            }
        }

        writer.close();
    }
}
