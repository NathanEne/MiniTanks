package com.minitanks.world;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

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


    public static ArrayList<Vector2> generateMap(int width, int height){
        ArrayList<Vector2> walls = new ArrayList<Vector2>();


        return walls;
    }
}
