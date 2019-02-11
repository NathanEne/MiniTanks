package com.minitanks.world;

public class ShapeGeneration {
    /*
     *  Implement static methods to generator the various shapes on the map
     */

    // The amount the length of shapes can vary, zero means no variance
    private static float variance = 0f;

    public static float getVariance() {
        return variance;
    }

    public static void setVariance(float variance) {
        ShapeGeneration.variance = variance;
    }




    public static float[] rightAngle(float r){
        return new float[]{};
    }

    public static float[] cutHexagon(float r){
        return new float[]{};
    }

    public static float[] line(float r){
        return new float[]{};
    }

    public static float[] open120(float r){
        return new float[]{};
    }
}
