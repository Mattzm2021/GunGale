package com.mattzm.gungale.util.math;

public class ModMathHelper {
    public static float tickToSecond(int tick) {
        return (int) (tick / 2.0f) / 10.0f;
    }

    public static double getRadius(double degree) {
        return degree / 180.0 * Math.PI;
    }

    public static double getDegree(double radius) {
        return radius * 180.0 / Math.PI;
    }
}