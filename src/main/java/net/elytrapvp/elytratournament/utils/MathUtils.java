package net.elytrapvp.elytratournament.utils;

import org.bukkit.util.Vector;

public class MathUtils {
    public static int percent(double currentValue, double maxValue){
        double percent = (currentValue/maxValue) *100;
        return (int) percent;
    }

    public static Vector rotateVector(Vector vector, double whatAngle) {
        double sin = Math.sin(whatAngle);
        double cos = Math.cos(whatAngle);
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;

        return vector.setX(x).setZ(z);
    }
}