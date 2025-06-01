package org.example.service;


import org.example.db.StationDAO;
import org.example.model.Station;

import java.util.*;

public class MetroService {
    private final StationDAO stationDAO = new StationDAO();

    public List<Station> getAllStations() {
        return stationDAO.getAllStations();
    }

    public Map<String, List<Station>> getStationsByLine() {
        return stationDAO.getStationsGroupedByLine();
    }

    public List<int[]> getConnections() {
        return stationDAO.getConnections();
    }

    public List<Station> findShortestPath(String startName, String endName) {
        Station start = stationDAO.getByName(startName);
        Station end = stationDAO.getByName(endName);
        return stationDAO.bfsPath(start, end);
    }
}
