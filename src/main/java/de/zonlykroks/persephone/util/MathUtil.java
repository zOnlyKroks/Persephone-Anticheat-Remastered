package de.zonlykroks.persephone.util;


public class MathUtil {

    public static double magnitude(final double... points) {
        double sum = 0.0;

        for (final double point : points) {
            sum += point * point;
        }

        return Math.sqrt(sum);
    }
}
