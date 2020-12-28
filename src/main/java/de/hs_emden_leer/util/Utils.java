package de.hs_emden_leer.util;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An class to serve utils for the kmeansFX-App
 *
 * @author Sven Wiemers
 */
public class Utils {
    // used in the readFile method, to separate the csv values
    private static final String COMMA__DELIMITER = ",";

    /**
     * Open the fileChooser, to choose a file
     *
     * @return the selected *.csv file
     */
    public static File openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        // set title
        fileChooser.setTitle("Wähle die Datei");
        // set the initial directory to the users home directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // only allow to open *.csv files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
        // open the fileChooser and return the selected *.csv file
        return fileChooser.showOpenDialog(new Stage());
    }

    /**
     * Reads a *.csv file and returns the content in a List
     *
     * @param file a *.csv file (may selected with the fileChooser)
     * @return the content of the *.csv file in a two-dimensional list
     */
    public static List<List<String>> readFile(File file) {
        List<List<String>> records = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            // read the contained lines and split them with the COMMA__DELIMITER
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA__DELIMITER);
                records.add(Arrays.asList(values));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Rounds the given value to a new value with given decimal points
     *
     * @param value         the (double) value that should be round
     * @param decimalPoints the (int) decimalpoints that the new value should have
     * @return the rounded double value
     */
    public static double round(double value, int decimalPoints) {
        double d = Math.pow(10, decimalPoints);
        return Math.round(value * d) / d;
    }

}
