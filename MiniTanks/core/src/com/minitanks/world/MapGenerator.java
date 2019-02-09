package com.minitanks.world;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class MapGenerator {
    /*
     *   3 Random modes of map generation:
     *          Asymmetric
     *          Horizontal/Vertically symmetric
     *          Oblique symmetry
     *
     *   Random shapes templates:
     *          Right angle
     *          Single Line
     *          Hexagon Top
     *          120 degree 'V'
     *
     *   1. Choose 5-10 random points within the map (leaving a gap border).
     *          Instantiate a variant of one of the shape templates
     *              What we can randomize:
     *                  Rotation, length of line segments
     *          After chosen point, block off this radius for next points
     *
     *   2. Then take end points and make a connection with only those nearby
     *   3. Randomly destroy a few line segments
     *   4. Rotate if symmetry was required
     *
     */


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
     * Number of wall points generated on map is dependent on map size
     *
     * @param width
     * @param height
     * @return an int of the number of wall starting points to generated on map
     */

    public static int generateNumberOfPoints(int width, int height) {
        Random rn = new Random();
        int numberOfPoints;
        if (width * height <= 900) {
            numberOfPoints = rn.nextInt((8 - 5) + 1) + 5;
        } else {
            numberOfPoints = rn.nextInt((10 - 8) + 1) + 8;
        }
        return numberOfPoints;
    }

    /**
     * @param width
     * @param height
     * @param numberOfPoints Return an array of wall points with their coordinates and their radii size
     */
    public static ArrayList<float[]> generateWallPoints(int width, int height, int numberOfPoints) {
        ArrayList<float[]> WallStartingPoints = new ArrayList<float[]>();

        // Wall Point will be float array of length 3 containing:
        // x-coordinate, y-coordinate and radii


        for (int i = 0; i < numberOfPoints; i++) {
            float[] WallPoint = new float[]{};
            boolean isValidp = false;
            while(!isValidp){
                float radii = randomNumber(1, 4);
                float xcoord = randomNumber(0, width);
                float ycoord = randomNumber(0, height);
                WallPoint = new float[]
                {xcoord, ycoord, radii};

                if (WallPoint[0] + radii > width - 2 || WallPoint[0] - radii <= 2
                        || WallPoint[1] + radii > height - 2 || WallPoint[1] - radii <= 2){
                    continue;
                }

                if (WallStartingPoints.size() > 1){
                    boolean isPastAllPoints = true;
                    for (float[] p : WallStartingPoints){
                        float distance =  (float)Math.sqrt(Math.pow(p[0] - xcoord, 2.0) + Math.pow(p[1] - ycoord, 2.0));
                        if (distance < radii + p[2]){
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


    public static void main(String[] args) {
        int numberOfPoints = generateNumberOfPoints( 30, 30);
        ArrayList<float[]> WallStartingPoints = generateWallPoints(30, 30, 6);
        for (float[] p : WallStartingPoints){
            System.out.println(p[0]);
            System.out.println(p[1]);
            System.out.println(p[2]);

        }
    }
}
