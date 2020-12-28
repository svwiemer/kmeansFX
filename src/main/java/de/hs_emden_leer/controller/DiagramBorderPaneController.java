package de.hs_emden_leer.controller;

import de.hs_emden_leer.model.Cluster;
import de.hs_emden_leer.model.Point;
import de.hs_emden_leer.model.Points;
import de.hs_emden_leer.util.KMeansUtils;
import de.hs_emden_leer.util.Utils;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

/**
 * Controller to managed the borderpane with the diagram. Draws the diagram, the (data-)points and clusters.
 * Contains further methods to generate the presentation of the k-means algorithm.
 *
 * @author Sven Wiemers
 */
public class DiagramBorderPaneController {

    // "Start/Reset"-Button, generate new clusters and set all needed variables back
    @FXML
    private Button startResetButton;

    // "Center Clusters (step)"-Button, calculates the centered clusters (only one step)
    @FXML
    private Button centerStepClustersButton;

    // "Center Clusters"-Button, calculates the centered clusters (all steps)
    @FXML
    private Button centerClustersButton;

    // Spinner to define the k-value
    @FXML
    private Spinner<Integer> kValueSpinner;

    // Canvas to hold the diagram
    @FXML
    private Canvas diagramCanvas;

    // offset for the diagram (for the border)
    private final double DIAGRAM_OFFSET = 25;
    // graphicscontext to draw on the canvas
    private GraphicsContext gc;
    // Color array for the cluster
    private Color[] clusterColors = {Color.RED, Color.BLUE, Color.GREEN, Color.GOLD, Color.CHOCOLATE, Color.LIGHTPINK, Color.CORAL, Color.ORANGE, Color.OLIVE};
    // Cluster ArrayList
    private ArrayList<Cluster> clusters = new ArrayList<>();
    // ArrayList to hold the previousClusters (need to check if clusters moved)
    private ArrayList<Cluster> previousClusters = new ArrayList<>();
    // ArrayList with (data-)points
    private ArrayList<Point> points = new ArrayList<>();
    // variable to check if clusters already initiliazed
    private boolean clustersInitiliazed = false;
    // variable to check if the calculating is done
    private boolean calculatingDone = false;
    // variable to count calculating steps
    private int steps = 1;

    /**
     * Initialize the view
     */
    public void initialize() {
        // add eventhandlers
        centerStepClustersButton.setOnAction(event -> centerStepClusters());
        centerClustersButton.setOnAction(event -> centerClusters());
        startResetButton.setOnAction(event -> resetAll());
        // initialize spinner and set defaults
        kValueSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 3));
        // eventhandler for the spinner
        kValueSpinner.valueProperty().addListener((obs, oldValue, newValue) ->
                resetAll());
        // initliaze the graphicContext
        gc = diagramCanvas.getGraphicsContext2D();
        // initialize the canvas (by reseting it)
        resetCanvas();
    }

    /**
     * Initialize the clusters and ensures that every cluster have at least one assigned (data-)point
     */
    private void initializeClusters() {
        // variable for buffering
        boolean clusterHaveAPoint = false;
        // do, while every cluster have at least one assigned (data-)point
        do {
            // create new list and delete old clusters (if exisiting)
            clusters = new ArrayList<>();

            // creates as many clusters as selected in the k-variable/kValueSpinner
            for (int i = 0; i < kValueSpinner.getValue(); i++) {
                // generate a cluster in the range that is in the file give
                // ATTENTION: Hardcoded! Works only with the *.csv file that was given in the BI-Course
                Cluster cluster = new Cluster().getRandomCluster(-20, 100, 0, 120);
                // set the color of the cluster
                cluster.setColor(clusterColors[i]);
                // adds cluster to list
                clusters.add(cluster);
            }

            // assign points to clusters
            KMeansUtils.assignPoints2Clusters(clusters, points);

            // check if each cluster have at least one assigned (data-)point. if not, delete clusters and start the loop
            // again, if every cluster have one assign (data-)point, end the loop.
            if (!KMeansUtils.checkIfClusterHaveAPoint(clusters, points)) {
                clusters = new ArrayList<>();
                clusterHaveAPoint = true;
            } else {
                clusterHaveAPoint = false;
            }

        } while (clusterHaveAPoint);
        // buffer the clusters, for later checks
        copyClusters2PreviousClusters();
        // indicate that the clusters have been initiated
        clustersInitiliazed = true;
    }

    /**
     * Assings the clusters to the (data-)points and draw a new diagram
     */
    private void assingCluster2PointsAndVisualize() {
        // delete old diagram
        resetCanvas();
        // check if clusters already initialized, if not generate new ones
        if (!clustersInitiliazed) {
            initializeClusters();
        } else {
            // assign the (data-)points to the clusters
            KMeansUtils.assignPoints2Clusters(clusters, points);
        }
        // visualize the result
        drawPointsAndClusters();
    }

    /**
     * Calculates one step to center the clusters
     */
    private void centerStepClusters() {
        try {
            // set (data-)points
            points = Points.getInstance().getPoints();
            // copy the previous cluster
            copyClusters2PreviousClusters();
            // calculate new centers for each cluster
            for (Cluster cluster : clusters) {
                KMeansUtils.centerCluster(cluster, points);
            }

            // draw the results
            assingCluster2PointsAndVisualize();

            // if steps > 1, tell the user, the cluster(center) has moved (not realy the truth, but in the most cases)
            if (steps > 1) {
                checkIfClustersMoved();
            } else {
                drawMessage("Clusters moved! [" + steps + " steps]", Color.BLACK);
            }

            // don't count steps if calculating is done
            if (!calculatingDone) {
                steps++;
            }
        } catch (NullPointerException e) {
            // Excpetionhandling if no (data-)points are loaded
            resetCanvas();
            drawMessage("Keine Daten gefunden, bitte Datei öffnen!", Color.RED);
        }
    }

    /**
     * Make a copy of the cluster
     */
    private void copyClusters2PreviousClusters() {
        previousClusters = new ArrayList<>();
        for (Cluster cluster : clusters) {
            previousClusters.add(Cluster.copyCluster(cluster));
        }
    }

    /**
     * Center the clusters until done
     */
    private void centerClusters() {
        try {
            while (!calculatingDone) {
                centerStepClusters();
            }
        } catch (NullPointerException e) {
            // Excpetionhandling if no (data-)points are loaded
            resetCanvas();
            drawMessage("Keine Daten gefunden, bitte Datei öffnen!", Color.RED);
        }
    }

    /**
     * Check if the cluster has moved or not
     *
     * @return a boolean. ture = cluster moved, false = cluster did not move
     */
    private boolean checkIfClustersMoved() {
        // Check for each cluster
        for (int i = 0; i < clusters.size(); i++) {
            // Check using the coordinates if cluster(center) has moved
            if ((clusters.get(i).getxCoordinate() != previousClusters.get(i).getxCoordinate()) || (clusters.get(i).getyCoordinate() != previousClusters.get(i).getyCoordinate())) {
                // visualize the result
                drawMessage("Clusters moved! [" + steps + " steps]", Color.BLACK);
                return true;
            }
        }
        // visualize the result
        drawMessage("Calculating done!  [" + steps + " steps needed]", Color.BLACK);
        // if no cluster(center) has moved, no further calculation isn needed
        calculatingDone = true;
        return false;
    }

    /**
     * Draws the outlines of the diagram
     * ATTENTION: Hardcoded! Works only with 800x800 canvas!
     */
    private void drawDiagramOutlines() {
        gc.setFill(Color.WHITE);
        gc.fillRect(DIAGRAM_OFFSET, DIAGRAM_OFFSET, diagramCanvas.getWidth() - DIAGRAM_OFFSET * 2, diagramCanvas.getHeight() - DIAGRAM_OFFSET * 2);
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(DIAGRAM_OFFSET, DIAGRAM_OFFSET, diagramCanvas.getWidth() - DIAGRAM_OFFSET * 2, diagramCanvas.getHeight() - DIAGRAM_OFFSET * 2);
    }

    /**
     * Draws the labels for the diagram
     * <p>
     * ATTENTION: Hardcoded! Works only with 800x800 canvas!
     */
    private void drawDiagramLabels() {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font(8));
        gc.setFill(Color.BLACK);
        // generate vertical labels and strokes
        gc.beginPath();
        for (int i = 0; i <= 12; i++) {
            gc.moveTo(20, Utils.round((DIAGRAM_OFFSET + i * 62.5), 0));
            gc.lineTo(25, Utils.round((DIAGRAM_OFFSET + i * 62.5), 0));
            // generate label
            int label = 120 - (10 * i);
            gc.fillText(String.valueOf(label), 10, Utils.round((DIAGRAM_OFFSET + i * 62.5), 0));
        }
        // generate horizontal labels and strokes
        for (int i = 0; i <= 12; i++) {
            gc.moveTo(Utils.round((DIAGRAM_OFFSET + i * 62.5), 0), 775);
            gc.lineTo(Utils.round((DIAGRAM_OFFSET + i * 62.5), 0), 780);
            // generate label
            int label = -20 + (10 * i);
            gc.fillText(String.valueOf(label), Utils.round((DIAGRAM_OFFSET + i * 62.5), 0), 790);
        }
        gc.closePath();
        gc.stroke();

    }

    /**
     * Draws a message on the top of the diagram
     *
     * @param message the message string
     * @param color   the message color
     */
    public void drawMessage(String message, Color color) {
        gc.setFont(new Font(10));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(color);
        gc.fillText(message, Math.round(diagramCanvas.getWidth() / 2), 12);
    }

    /**
     * Draws each cluster and (data-)point
     */
    private void drawPointsAndClusters() {
        for (Cluster cluster : clusters) {
            drawCluster(cluster);
        }
        for (Point point : points) {
            drawPoint(point);
        }
    }

    /**
     * Draws a point on the diagram (4x4 pixels, filled circles)
     *
     * @param point the point to be drawn
     */
    public void drawPoint(Point point) {
        Cluster cluster = point.getCluster();
        if (cluster != null) {
            gc.setFill(cluster.getColor());
        }
        gc.fillOval(normalizeXCoordinates(point.getxCoordinate()), normalizeYCoordinates(point.getyCoordinate()), 4, 4);
    }

    /**
     * Draws a cluster on the diagram (6x6pixels, stroked rectangles)
     * and label it with the coordinates
     *
     * @param cluster the cluster to be drawn
     */
    public void drawCluster(Cluster cluster) {
        gc.setStroke(cluster.getColor());
        gc.strokeRect(normalizeXCoordinates(cluster.getxCoordinate()) - 1, normalizeYCoordinates(cluster.getyCoordinate()) - 1, 6, 6);
        gc.setFont(new Font(10));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(cluster.getColor());
        gc.fillText("[" + Utils.round(cluster.getxCoordinate(), 2) + "|" + Utils.round(cluster.getyCoordinate(), 2) + "]", normalizeXCoordinates(cluster.getxCoordinate()) + 38, normalizeYCoordinates(cluster.getyCoordinate()) - 8);
    }

    /**
     * Reset the view and resets all important variables, to draw a new diagram, with new values
     */
    private void resetAll() {
        try {
            // load points new
            points = Points.getInstance().getPoints();
            // indicate that the clusters have not been initiated
            clustersInitiliazed = false;
            // indicate that the calculation isn't done
            calculatingDone = false;
            // set the steps back
            steps = 1;
            // reset the canvas
            resetCanvas();
            // generate new clusters, assign and visualize them
            assingCluster2PointsAndVisualize();
            // tell the user what happend
            drawMessage("Reset des Diagramms & neue Cluster generiert. [0 steps]", Color.BLACK);
        } catch (NullPointerException e) {
            // Excpetionhandling if no (data-)points are loaded
            resetCanvas();
            drawMessage("Keine Daten gefunden, bitte Datei öffnen!", Color.RED);
        }
    }

    /**
     * Reset the canvas to an empty diagram
     */
    private void resetCanvas() {
        // clear all
        gc.clearRect(0, 0, diagramCanvas.getWidth(), diagramCanvas.getHeight());
        // draw outlines and labels
        drawDiagramOutlines();
        drawDiagramLabels();
    }

    /**
     * Calculates the normalized x-cooridinates of the data points to draw them on the canvas/diagram
     * <p>
     * ATTENTION: Hardcoded! Works only with 800x800 canvas!
     *
     * @param x the x-coordinate that should be calculated
     * @return the normlized x-coordinate to draw the (data-)points to the canvas/diagram
     */
    private double normalizeXCoordinates(double x) {
        double result;
        if (x >= 0) {
            result = Utils.round(((x / 10 * 62.5 + DIAGRAM_OFFSET + (2 * 62.5)) - 2), 0);
        } else {
            result = Utils.round((DIAGRAM_OFFSET + (2 * 62.5) - (((x * (-1) / 10 * 62.5))) - 2), 0);
        }
        return result;
    }

    /**
     * Calculates the normalized y-cooridinates of the data points to draw them on the canvas/diagram
     * <p>
     * ATTENTION: Hardcoded! Works only with 800x800 canvas!
     *
     * @param y the y-coordinate that should be calculated
     * @return the normlized y-coordinate to draw the (data-)points to the canvas/diagram
     */
    private double normalizeYCoordinates(double y) {
        y = Utils.round((750 - (y / 10 * 62.5 - DIAGRAM_OFFSET) - 2), 0);
        return y;
    }
}
