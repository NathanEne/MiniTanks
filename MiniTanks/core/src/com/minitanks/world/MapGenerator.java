package com.minitanks.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.minitanks.game.entities.Wall;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class MapGenerator {

    public static float screenWidth = 18600;
    public static int screenHeight = 30;


    /**
     * @param min
     * @param max
     * @return a random float between a certain min and max
     */
    public static float randomNumber(float min, float max) {
        Random rn = new Random();
        float number = min + rn.nextFloat() * (max - min);
        return number;
    }



    /**
     * Function generates wall points with various radii that are NOT overlapping
     * @param width
     * @param height
     */
    public static ArrayList<float[]> generateWallPoints(int width, int height, int n) {
        int numberOfPoints = n;
        ArrayList<float[]> WallStartingPoints = new ArrayList<float[]>();

        // Wallpoints will be float array of length 3 containing:
        // x-coordinate, y-coordinate, and radii

        // The distance away from wall radii cannot overlap
        float borderGap = 3f;

        for (int i = 0; i < numberOfPoints; i++) {
            float[] WallPoint = new float[]{};
            boolean isValidp = false;
            while(!isValidp){
                float radii = randomNumber(2, 6);
                float xcoord = randomNumber(0, width);
                float ycoord = randomNumber(0, height);
                WallPoint = new float[] {xcoord, ycoord, radii};

                if (WallPoint[0] + radii > width - borderGap || WallPoint[0] - radii <= borderGap
                        || WallPoint[1] + radii > height - borderGap || WallPoint[1] - radii <= borderGap){
                    continue;
                }

                if (WallStartingPoints.size() > 1){
                    boolean isPastAllPoints = true;
                    for (float[] p : WallStartingPoints){
                        float distance = Vector2.dst(p[0], p[1], WallPoint[0], WallPoint[1]);
                        if (distance < radii + p[2] + 1){
                            isPastAllPoints = false;
                            break;
                        }
                    }
                    if (isPastAllPoints){
                        isValidp = true;
                    }
                }
                else{
                    isValidp = true;
                }
            }

            WallStartingPoints.add(WallPoint);
        }
        return WallStartingPoints;
    }



    /**
     * Create a wall along a line. Line can be angled
     * Assuming p1 and p2 y component zero. Will use z and x plane. +x is up, +z is right
     * @return two float values, specifying the length of wall and angle rotated
     */
    public static float[] generateWallOnLine(Vector3 p1, Vector3 p2){
        float[] points = new float[2];

        // p1 minus p2
        Vector3 line = new Vector3(p2.x - p1.x, 0, p2.z-p1.z);

        // Angle between vectors
        float theta = (float)Math.atan(line.x / line.z);
        points[1] = theta;

        float distance = Vector2.dst(0, 0, line.x, line.z);
        points[0] = distance;
        return points;
    }


    /**
     * Will be called to generate a single chunk.
     * @param w width of area
     * @param h height of area
     * @param n number of nodes
     * @param a the threshold of connecting distance
     * @param mC the max number of connections a node can have
     * @return an ArrayList of float arrays, each float array has: [p1x, p1z, p2x, p2z]
     */
    public static ArrayList<float[]> generateGeometricGraph(float w, float h, int n, float a, float mina, int mC, Vector3 centre){
        ArrayList<float[]> Lines = new ArrayList<float[]>();
        ArrayList<float[]> thisCluster;

        Vector3 ranP = new Vector3(centre.x - 0.25f*screenHeight, 0 ,centre.z - 0.75f*screenWidth);
        for (int i = 0; i < Lines.size(); i++){
            if (ranP.dst(Lines.get(i)[0], 0, Lines.get(i)[1]) < a || ranP.dst(Lines.get(i)[2], 0, Lines.get(i)[2]) < a + 50)
                continue;
        }
        thisCluster = generateCluster(ranP, a, mina, mC, false);
        Lines.addAll(thisCluster);

        ranP = new Vector3(centre.x + 0.25f*screenHeight, 0 ,centre.z + 0.75f*screenWidth);
        for (int i = 0; i < Lines.size(); i++){
            if (ranP.dst(Lines.get(i)[0], 0, Lines.get(i)[1]) < a || ranP.dst(Lines.get(i)[2], 0, Lines.get(i)[2]) < a + 50)
                continue;
        }
        thisCluster = generateCluster(ranP, a, mina, mC, false);
        Lines.addAll(thisCluster);



        return Lines;
    }


    /**
     * Generate a connected shape of random angles and lengths. Using polar coordinates for simplicity.
     * @param node1 the start point
     * @param a the max length of r in polar coordinates
     * @param mina the respective min ^
     * @param mC max number of edges on the graph
     * @param allowConvex bool whether the dot product of new vector can be greater than zero.
     * @return a list of length 4 float arrays specifying the two end points of the line.
     */
    private static ArrayList<float[]> generateCluster(Vector3 node1, float a, float mina, int mC, boolean allowConvex){
        ArrayList<float[]> Cluster = new ArrayList<float[]>();
        int c = 0;

        float prob = 0f;

        Vector3 currPoint = new Vector3(node1);
        Vector3 newPoint;

        // Create another valid point
        newPoint = getNewPoint(mina, a, currPoint);

        Cluster.add(new float[] {newPoint.x, newPoint.z, currPoint.x, currPoint.z});


        do {
            float ran = randomNumber(0, 1);
            float[] lastline = Cluster.get(Cluster.size() - 1);
            Vector3 prev = new Vector3(lastline[2] - lastline[0], 0, lastline[3] - lastline[1]);

            if (ran < prob){
                // Get another connection of same node
                do{
                    newPoint = getNewPoint(mina, a, currPoint);
                } while (new Vector3(newPoint.x - currPoint.x, 0, newPoint.z - currPoint.z).nor().dot(new Vector3(prev).scl(-1).nor()) > 0.4f);
            }
            else{
                currPoint = new Vector3(newPoint);
                do{
                    newPoint = getNewPoint(mina, a, currPoint);
                } while (new Vector3(newPoint.x - currPoint.x, 0, newPoint.z - currPoint.z).nor().dot(new Vector3(prev).nor()) > 0.4f);
            }
            Cluster.add(new float[] {newPoint.x, newPoint.z, currPoint.x, currPoint.z});
            c++;
        } while (c < mC);
        // 50% probability of choosing another connection for this node or extend off the newly created.

        return Cluster;
    }


    private static Vector3 getNewPoint(float mina, float a, Vector3 current){
        float[] randomPolarCoord = new float[] {randomNumber(0, (float)Math.PI*2), randomNumber(mina, a)}; // theta and r
        return new Vector3(current.x + randomPolarCoord[1]*(float)Math.sin(randomPolarCoord[0]), 0, current.z + randomPolarCoord[1]*(float)Math.cos(randomPolarCoord[0]));
    }
}
