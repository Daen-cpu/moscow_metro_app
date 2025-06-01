package org.example.util;

public class MetroStationsFetcherTest {
    public static void main(String[] args) {
        try {
            MetroStationsFetcher.fetchMetroStations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
