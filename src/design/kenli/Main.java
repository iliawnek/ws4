package design.kenli;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) return;

        String filename = args[0];

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            Dataset dataset = new Dataset();
            String line;
            while ((line = reader.readLine()) != null) {
               dataset.addTweet(line);
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.printf("Unable to open %s.\n", filename);
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.printf("Error reading %s.\n", filename);
            e.printStackTrace();
        }
    }
}
