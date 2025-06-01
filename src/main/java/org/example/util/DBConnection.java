package org.example.util;

import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/metro_db_final";
    private static final String USER = "daen";
    private static final String PASSWORD = "123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Метод для добавления станций в базу данных
    public static void addStation(String name, int lineId, double latitude, double longitude) {
        String sql = "INSERT INTO stations (name, line_id, latitude, longitude) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setInt(2, lineId);
            statement.setDouble(3, latitude);
            statement.setDouble(4, longitude);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getLineIdByName(String name) {
        String sql = "SELECT id FROM lines WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // или выбросить исключение
    }




}
