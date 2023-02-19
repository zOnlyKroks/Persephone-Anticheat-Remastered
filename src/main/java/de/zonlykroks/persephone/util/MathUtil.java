package de.zonlykroks.persephone.util;

import org.bukkit.Location;

import java.text.DecimalFormat;
import java.util.Collection;

public class MathUtil {

    public static double getAverage(final Collection<? extends Number> data) {
        return data.stream().mapToDouble(Number::doubleValue).average().orElse(0D);
    }

    public static double trim(int degree, double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.#" + "#".repeat(Math.max(0, degree - 1)));
        return Double.parseDouble(twoDForm.format(d).replaceAll(",", "."));
    }

    public static double getHorizontalDistance(final Location one, final Location two) {
        double toReturn = 0.0;
        final double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        final double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        final double sqrt = Math.sqrt(xSqr + zSqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }

    public static boolean isRoughlyEqual(double a, double b) {
        return Math.abs(a - b) < 0.001;
    }

    public static double magnitude(final double... points) {
        double sum = 0.0;

        for (final double point : points) {
            sum += point * point;
        }

        return Math.sqrt(sum);
    }
}
