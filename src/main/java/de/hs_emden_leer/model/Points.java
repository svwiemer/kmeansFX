package de.hs_emden_leer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to save the (data-)points. To be able to get access from everywhere, the class is implemented as a singelton.
 *
 * @author Sven Wiemers
 */
public class Points {
    // list with the (data-)points
    private ArrayList<Point> points;
    // variable for singelton
    private static Points instance;

    // constructor
    public Points() {

    }

    // makes the class to a singelton
    public static synchronized Points getInstance() {
        if (instance == null) {
            instance = new Points();
        }
        return instance;
    }

    /**
     * Sets the coordintes to the (data-)points from a given list
     *
     * @param list the list with the coordinates of the (data-)points
     */
    public void setCoordinates(List<List<String>> list) {
        points = new ArrayList<>();
        // iterate through the list, genereate a point and set the coordinates of the points
        for (List<String> innerList : list) {
            // initialize required variables
            float x = 0.0f;
            float y = 0.0f;
            for (int i = 0; i < innerList.size(); i++) {
                // gt x and y values of the points
                // ATTENTION, no error correction!! Works only with lists with two given points
                if (i == 0) {
                    x = Float.valueOf(innerList.get(i));
                } else if (i == 1) {
                    y = Float.valueOf(innerList.get(i));
                }
            }
            // add points to coordinates
            points.add(new Point(x, y));
        }
    }

    /**
     * Getter for the (data-)points
     *
     * @return (data -)points
     */
    public ArrayList<Point> getPoints() {
        return points;
    }

    /**
     * Setter for the (data-)points
     *
     * @param points (data-)points
     */
    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }
}
