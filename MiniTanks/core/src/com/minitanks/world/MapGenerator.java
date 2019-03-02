package com.minitanks.world;

import com.badlogic.gdx.math.Vector2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    public static int Width = 30;
    public static int Height = 30;
    public static boolean isSymmetric = true;
    public static boolean isHorizSym = true;
    // Float to describe how much the shapes should vary from their default state. 0 is no variance, 1 is max variance
    public static float Variance = 0;

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
    public static int negativepositivezero() {
        Random rn = new Random();
        int number = -1 + rn.nextInt(3);
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
        int numberOfPoints;
        if (width * height <= 900) {
            numberOfPoints = (int)randomNumber(5, 8);
        } else {
            numberOfPoints = (int)randomNumber(8, 12);
        }
        return numberOfPoints;
    }

    /**
     * Function generates wall points with various radii that are NOT overlapping
     * @param width
     * @param height
     */
    public static ArrayList<float[]> generateWallPoints(int width, int height) {
        int numberOfPoints = generateNumberOfPoints(width, height);
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
     * @param coordx
     * @param coordy
     * @param radius Takes in wall starting coordinates with their selective radius
     * @return float[]
     * Returns new coordinates for wall endpoints to create a wall structure within a circumference
     */
    public static float[] generateVcoordinates(float coordx, float coordy, float radius) {
        float delta_x1 = randomNumber(-radius, radius);
        float delta_x2 = randomNumber(-radius, radius);
        float x1 = coordx + delta_x1;
        float x2 = coordx + delta_x2;
        float delta_y1 = negativepositivezero() * ((radius) * (radius) - (delta_x1) * (delta_x1));
        float delta_y2 = negativepositivezero() * ((radius) * (radius) - (delta_x2) * (delta_x2));
        float y1 = coordy + delta_y1;
        float y2 = coordy + delta_y2;
        float[] float_array = new float[4];
        float_array[0] = x1;
        float_array[1] = y1;
        float_array[2] = x2;
        float_array[3] = y2;
        return float_array;
    }

}
