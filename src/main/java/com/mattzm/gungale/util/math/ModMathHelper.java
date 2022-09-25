package com.mattzm.gungale.util.math;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class ModMathHelper {
    public static float tickToSecond(int tick) {
        return twoDigitsFloat((float) tick / 20.0f);
    }

    public static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    public static double getFloatPart(double num) {
        return num - MathHelper.floor(num);
    }

    public static float getFloatPart(float num) {
        return num - MathHelper.floor(num);
    }

    public static float oneDigitFloat(float num) {
        int a = Math.round(num * 10);
        return a / 10.0f;
    }

    public static double twoDigitsDouble(double num) {
        int a = (int) Math.round(num * 100);
        return a / 100.0;
    }

    public static float twoDigitsFloat(float num) {
        int a = Math.round(num * 100);
        return a / 100.0f;
    }

    public static double getRangeRandom(double max, double min) {
        return new Random().nextDouble() * (max - min) + min;
    }

    public static double radiusToDegree(double radius) {
        return radius / Math.PI * 180;
    }

    public static double degreeToRadius(double degree) {
        return degree / 180 * Math.PI;
    }

    public static float degreeToRadius(float degree) {
        return degree / 180 * (float) Math.PI;
    }

    public static double VFovToHFov(double VFov) {
        return radiusToDegree(Math.atan(Math.tan(degreeToRadius(VFov / 2)) * 16 / 9) * 2);
    }

    public static double getFovWithMagnification(double VFov, int magnification) {
        double tan = Math.sin(degreeToRadius(VFov / 2.0)) / Math.cos(degreeToRadius(VFov / 2)) / magnification;
        return Math.min(radiusToDegree(Math.atan(tan)) * 2, 60);
    }
}