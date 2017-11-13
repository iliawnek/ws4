package design.kenli;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main {

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
            System.out.println("original: " + dataset.countTweets());
            dataset.filterClusterSize(10);
            System.out.println("cluster size > 10: " + dataset.countTweets());
            dataset.filterClusterUserDiversity(0.90); // 0.95 is better in 7days
            System.out.println("cluster user diversity > 0.9: " + dataset.countTweets());
            int threshold = 3;
            int leniency = 5;
            dataset.markBurstingClusters(5, threshold, leniency);
//            dataset.markBurstingClusters(10, threshold, leniency);
//            dataset.markBurstingClusters(20, threshold, leniency);
            dataset.markBurstingClusters(30, threshold, leniency);
            dataset.markBurstingClusters(60, threshold, leniency);
//            dataset.markBurstingClusters(120, threshold, leniency);
//            dataset.markBurstingClusters(240, threshold, leniency);
            dataset.filterNonBurstingClusters();
            System.out.println("bursting: " + dataset.countTweets());

            // Check windows.
//            Entity entity = dataset.getRandomEntity();
//            Entity entity = dataset.getEntity("missy elliot");
//            ArrayList<Window> windows = entity.getWindows(5, 5, 5, 10);
//            System.out.println("entity: " + entity.getName());
//            System.out.println("tweets: " + entity.countTweets());
//            System.out.println("5-minute windows: " + windows.size());
//
//            ArrayList<String> toPrint = new ArrayList<>();
//
//            for (Window w : windows) {
//                toPrint.add(
//                        w.getStart() + " - " +
//                                w.getEnd() + " - " +
//                                w.getTweetCount() +
//                                (w.isBursting() ? " - burst" : "")
//                );
//            }
//
//            for (Cluster c : entity.getClusters()) {
//                long clusterStart = (long) c.getCentroidTime();
//                long startBound = clusterStart;
//                long endBound = clusterStart + Utilities.minutesToMillis(25);
//                toPrint.add(
//                        startBound + " - " +
//                        c.getId() + " start");
//                toPrint.add(
//                        endBound + " - " +
//                        c.getId() + " end");
//            }
//
//            Collections.sort(toPrint);
//            for (String s : toPrint) System.out.println(s);

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
