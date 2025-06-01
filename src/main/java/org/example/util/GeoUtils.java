package org.example.util;

public class GeoUtils {
    private static final double MIN_LAT = 55.5381467;
    private static final double MAX_LAT = 55.9216617;
    private static final double MIN_LON = 37.2872762;
    private static final double MAX_LON = 37.8637729;

    public static double scaleLat(double latitude, double height) {
        return height - ((latitude - MIN_LAT) / (MAX_LAT - MIN_LAT)) * height;
    }

    public static double scaleLon(double longitude, double width) {
        return ((longitude - MIN_LON) / (MAX_LON - MIN_LON)) * width;
    }
}
