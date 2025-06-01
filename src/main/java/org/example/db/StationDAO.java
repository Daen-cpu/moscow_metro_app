package org.example.db;


import org.example.model.Station;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.*;

public class StationDAO {

    public List<Station> getAllStations() {
        List<Station> stations = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT stations.id, stations.name, lines.name AS line_name, stations.latitude, stations.longitude " +
                     "FROM stations JOIN lines ON stations.line_id = lines.id")) {

            while (rs.next()) {
                stations.add(new Station(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("line_name"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stations;
    }


    public Station getByName(String name) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT stations.id, stations.name, lines.name AS line_name, " +
                             "stations.latitude, stations.longitude " +
                             "FROM stations JOIN lines ON stations.line_id = lines.id WHERE stations.name = ?")) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Station(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("line_name"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Map<Integer, Station> getStationMap() {
        Map<Integer, Station> map = new HashMap<>();
        for (Station s : getAllStations()) {
            map.put(s.getId(), s);
        }
        return map;
    }

    public List<int[]> getConnections() {
        List<int[]> connections = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT station1_id, station2_id FROM connections")) {

            while (rs.next()) {
                connections.add(new int[]{
                        rs.getInt("station1_id"),
                        rs.getInt("station2_id")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connections;
    }

    public Map<String, List<Station>> getStationsGroupedByLine() {
        Map<String, List<Station>> map = new HashMap<>();
        for (Station s : getAllStations()) {
            map.computeIfAbsent(s.getLine(), k -> new ArrayList<>()).add(s);
        }
        return map;
    }

    // BFS-алгоритм поиска кратчайшего пути
    public List<Station> bfsPath(Station start, Station end) {
        Map<Integer, Station> stationMap = getStationMap();
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] conn : getConnections()) {
            graph.computeIfAbsent(conn[0], k -> new ArrayList<>()).add(conn[1]);
            graph.computeIfAbsent(conn[1], k -> new ArrayList<>()).add(conn[0]);
        }

        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> prev = new HashMap<>();
        Set<Integer> visited = new HashSet<>();

        queue.add(start.getId());
        visited.add(start.getId());

        while (!queue.isEmpty()) {
            int current = queue.poll();
            if (current == end.getId()) break;

            for (int neighbor : graph.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    prev.put(neighbor, current);
                }
            }
        }

        List<Station> path = new LinkedList<>();
        for (Integer at = end.getId(); at != null; at = prev.get(at)) {
            path.add(0, stationMap.get(at));
        }

        if (path.get(0).getId() != start.getId()) {
            return new ArrayList<>();
        }

        return path;
    }
}
