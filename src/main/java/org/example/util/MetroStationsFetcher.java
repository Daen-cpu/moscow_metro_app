package org.example.util;

import org.example.util.ColorToLineMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MetroStationsFetcher {

    private static final String OVERPASS_URL = "https://overpass-api.de/api/interpreter";
    private static final String OVERPASS_QUERY = """
        [out:json];
        area["name"="Москва"]->.a;
        node["station"="subway"](area.a);
        out body;
        """;

    public static void fetchMetroStations() throws IOException {
        String jsonResponse = fetchDataFromOverpass();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray elements = jsonObject.getJSONArray("elements");

        int addedCount = 0;

        for (int i = 0; i < elements.length(); i++) {
            JSONObject element = elements.getJSONObject(i);
            JSONObject tags = element.optJSONObject("tags");
            if (tags == null) continue;

            String name = tags.optString("name", null);
            String colorRaw = tags.optString("colour", null);

            if (name == null || colorRaw == null) continue;

            String lineName = ColorToLineMapper.getLineNameFromColor(colorRaw);
            if (lineName.equals("Неизвестная линия")) {
                System.out.printf("Неизвестный цвет: %s — \"%s\" пропущена.%n", colorRaw, name);
                continue;
            }

            int lineId = DBConnection.getLineIdByName(lineName);
            if (lineId == -1) {
                System.out.printf("Линия не найдена в БД: %s — \"%s\" пропущена.%n", lineName, name);
                continue;
            }

            double lat = element.getDouble("lat");
            double lon = element.getDouble("lon");

            DBConnection.addStation(name, lineId, lat, lon);
            addedCount++;
        }

        System.out.println("Станций добавлено: " + addedCount);
    }

    private static String fetchDataFromOverpass() throws IOException {
        URL url = new URL(OVERPASS_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = OVERPASS_QUERY.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = reader.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}
