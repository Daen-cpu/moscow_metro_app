package org.example.model;

import java.util.Map;

public class Station {
    private final int id;
    private final String name;
    private final String line;
    private final double latitude;
    private final double longitude;

    // Статическая карта для поиска станции по ID
    private static Map<Integer, Station> stationMap;

    public Station(int id, String name, String line, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.line = line;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLine() {
        return line;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // Статические методы для работы с картой станций
    public static void setStationMap(Map<Integer, Station> map) {
        stationMap = map;
    }

    public static Station getById(int id) {
        return stationMap != null ? stationMap.get(id) : null;
    }

    @Override
    public String toString() {
        return name + " (" + line + ")";
    }
}
