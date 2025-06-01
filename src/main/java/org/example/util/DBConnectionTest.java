package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/metro_db_final";
        String user = "daen";
        String password = "123";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM stations")) {

            while (rs.next()) {
                System.out.println("Station: " + rs.getString("name") + ", Line: " + rs.getString("line"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
