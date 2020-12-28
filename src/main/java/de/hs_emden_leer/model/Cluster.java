package de.hs_emden_leer.model;

import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Model class for a cluster
 *
 * @author Sven Wiemers
 */
public class Cluster {
    // X-Coordinate
    private double xCoordinate;
    // Y-Coordinate
    private double yCoordinate;
    // Color of the cluster
    private Color color;


    /**
     * Generates a cluster with random coordinates with the given ranges
     *
     * @param rangeLowX  low X-Coordinate range
     * @param rangeHighX high X-Coordinate range
     * @param rangeLowY  low Y-Coordinate range
     * @param rangeHighY high Y-Coordinate range
     * @return a cluster with random coordinates with the given ranges
     */
    public Cluster getRandomCluster(int rangeLowX, int rangeHighX, int rangeLowY, int rangeHighY) {
        // initialize a new random
        Random random = new Random();

        // solve problems with negative values
        if (rangeLowX < 0) {
            rangeHighX -= rangeLowX;
        }
        if (rangeLowY < 0) {
            rangeHighY -= rangeLowY;
        }

        // generate random coordinates
        setxCoordinate(Math.random() * rangeHighX + rangeLowX);
        setyCoordinate(Math.random() * rangeHighY + rangeLowY);

        // return the cluster with random coordinates
        return this;
    }

    // getter & setters + toString method

    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "xCoordinate=" + xCoordinate +
                ", yCoordinate=" + yCoordinate +
                ", color=" + color +
                '}';
    }

    public static Cluster copyCluster(Cluster cluster) {
        Cluster newCluster = new Cluster();
        newCluster.setyCoordinate(cluster.getyCoordinate());
        newCluster.setxCoordinate(cluster.getxCoordinate());
        newCluster.setColor(cluster.getColor());
        return newCluster;
    }
}
