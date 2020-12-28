package de.hs_emden_leer.util;

import de.hs_emden_leer.model.Cluster;
import de.hs_emden_leer.model.Point;

import java.util.ArrayList;

/**
 * This class serves some utils to assign (data-)points to clusters, to center clusters in the middle of their partition
 * and check if a cluster have a (data-)point.
 *
 * @author Sven Wiemers
 */
public class KMeansUtils {

    /**
     * Assigns (data-)points to clusters with using the Euclidean distance
     *
     * @param clusters the arraylist which contains the clusters
     * @param points   the arraylist with the (data-)points
     */
    public static void assignPoints2Clusters(ArrayList<Cluster> clusters, ArrayList<Point> points) {

        // iterate each point
        for (Point point : points) {
            // variable to check the minimum distance
            double minimumDistance = -1;
            // variable for buffering the current distance
            double currentDistance = 0;
            // variabel for buffering the nearest cluster
            Cluster nearestCluster = null;

            // iterate each cluster
            for (Cluster cluster : clusters) {
                // calculate the Euclidean distance between the actual cluster and point
                currentDistance = calculateDistance(cluster, point);

                // check if the distance is the closest
                if (minimumDistance < 0) {
                    minimumDistance = currentDistance;
                    nearestCluster = cluster;
                } else if (currentDistance <= minimumDistance) {
                    minimumDistance = currentDistance;
                    nearestCluster = cluster;
                }
            }
            // set the nearest cluster to the point
            point.setCluster(nearestCluster);
        }
    }

    /**
     * Centers a cluster to the middle of the partition. Uses the arithmetic average between all (data-)points
     * of the cluster
     *
     * @param cluster cluster that should be centered
     * @param points  (data-)points to center the cluster
     * @return the centered cluster
     */
    public static Cluster centerCluster(Cluster cluster, ArrayList<Point> points) {
        double sumX = 0;
        double sumY = 0;
        double nrOfPoints = 0;

        // calculate the total value of x- and y-Coordinates and the total number of points that are in the cluster
        for (Point point : points) {
            if (point.getCluster() == cluster) {
                sumX += point.getxCoordinate();
                sumY += point.getyCoordinate();
                nrOfPoints++;
            }
        }
        // calculate the arithmetic average for the given cluster
        cluster.setxCoordinate(sumX / nrOfPoints);
        cluster.setyCoordinate(sumY / nrOfPoints);
        // return the cluster
        return cluster;
    }

    /**
     * Calculates the Euclidean distances between a cluster and a (data-)point
     *
     * @param cluster the cluster for calculating the Euclidean distance
     * @param point   the (data-)point for calculating the Euclidean distance
     * @return the calculated (double) distance
     */
    private static double calculateDistance(Cluster cluster, Point point) {
        // set coordinates to shorter variables
        double q1 = cluster.getxCoordinate();
        double q2 = cluster.getyCoordinate();
        double p1 = point.getxCoordinate();
        double p2 = point.getyCoordinate();

        // calculate the Euclidean distances and return the result
        return Math.sqrt(Math.pow(q1 - p1, 2) + Math.pow(q2 - p2, 2));
    }

    /**
     * Checks if a list of clusters have at least one assigned (data-)point
     *
     * @param clusters the (ArrayList<Cluster>) clusters that should be checked
     * @param points   the (ArrayList<Point>) points that are used for the check
     * @return a boolean if every given cluster have at least one assigned (data-)point
     */
    public static boolean checkIfClusterHaveAPoint(ArrayList<Cluster> clusters, ArrayList<Point> points) {
        // variable to buffer the result
        boolean clusterHasAPoint;
        // iterate each cluster
        for (Cluster cluster : clusters) {
            // variable to buffer the result
            clusterHasAPoint = false;
            // iterate each point
            for (Point point : points) {
                // check if the cluster is assigned to a (data-)point. Breaks the loop if a points is found
                if (point.getCluster() == cluster) {
                    clusterHasAPoint = true;
                    break;
                }
            }
            // if no (data-)point with the current cluster is found, return false
            if (!clusterHasAPoint) {
                return false;
            }
        }
        // if all clusters have at least one (data-)point, return true
        return true;
    }
}
