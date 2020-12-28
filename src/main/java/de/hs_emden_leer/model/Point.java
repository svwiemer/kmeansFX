package de.hs_emden_leer.model;

/**
 * Model class for a single (data-)point
 *
 * @author Sven Wiemers
 */
public class Point {
    // X-Coordinate
    private double xCoordinate;
    // Y-Coordinate
    private double yCoordinate;
    // Assigned cluster
    private Cluster cluster;

    // constructor
    public Point(float x, float y) {
        setxCoordinate(x);
        setyCoordinate(y);
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

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public String toString() {
        return "[" + xCoordinate + "," + yCoordinate + "] " + "Cluster: " + cluster;
    }
}
