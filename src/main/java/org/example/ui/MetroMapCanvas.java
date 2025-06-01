package org.example.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.model.Station;

import java.util.List;
import java.util.Map;

import static org.example.util.GeoUtils.scaleLat;
import static org.example.util.GeoUtils.scaleLon;

public class MetroMapCanvas {
    private final Canvas canvas;
    private final GraphicsContext gc;

    public MetroMapCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void drawMetroMap(Map<String, List<Station>> lines, List<int[]> connections) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Рисуем соединения
        gc.setLineWidth(3);
        gc.setStroke(Color.GRAY);
        for (int[] conn : connections) {
            Station s1 = Station.getById(conn[0]);
            Station s2 = Station.getById(conn[1]);
            if (s1 == null || s2 == null) continue;

            double x1 = scaleLon(s1.getLongitude(), canvas.getWidth());
            double y1 = scaleLat(s1.getLatitude(), canvas.getHeight());
            double x2 = scaleLon(s2.getLongitude(), canvas.getWidth());
            double y2 = scaleLat(s2.getLatitude(), canvas.getHeight());

            gc.strokeLine(x1, y1, x2, y2);
        }

        // Рисуем станции
        for (String line : lines.keySet()) {
            gc.setFill(getColorByLine(line));
            for (Station s : lines.get(line)) {
                double x = scaleLon(s.getLongitude(), canvas.getWidth());
                double y = scaleLat(s.getLatitude(), canvas.getHeight());
                gc.fillOval(x - 5, y - 5, 10, 10);
            }
        }
    }

    public void highlightRoute(List<Station> route) {
        if (route == null || route.size() < 2) return;

        gc.setLineWidth(4);
        gc.setStroke(Color.RED);

        for (int i = 0; i < route.size() - 1; i++) {
            Station s1 = route.get(i);
            Station s2 = route.get(i + 1);

            double x1 = scaleLon(s1.getLongitude(), canvas.getWidth());
            double y1 = scaleLat(s1.getLatitude(), canvas.getHeight());
            double x2 = scaleLon(s2.getLongitude(), canvas.getWidth());
            double y2 = scaleLat(s2.getLatitude(), canvas.getHeight());

            gc.strokeLine(x1, y1, x2, y2);
        }

        for (Station s : route) {
            double x = scaleLon(s.getLongitude(), canvas.getWidth());
            double y = scaleLat(s.getLatitude(), canvas.getHeight());
            gc.setFill(Color.RED);
            gc.fillOval(x - 6, y - 6, 12, 12);
        }
    }

    private Color getColorByLine(String line) {
        return switch (line.toLowerCase()) {
            case "сокольническая линия" -> Color.RED;
            case "арбатско-покровская линия" -> Color.BLUE;
            case "замоскворецкая линия" -> Color.web("2DBE2C");
            case "калужско-рижская линия" -> Color.ORANGE;
            case "кольцевая линия" -> Color.BROWN;
            case "филёвская линия" -> Color.LIGHTBLUE;
            case "таганско-краснопресненская линия" -> Color.web("800080");
            case "люблинско-дмитровская линия" -> Color.YELLOWGREEN;
            case "серпуховско-тимирязевская линия" -> Color.GRAY;
            case "бкл" -> Color.web("82c0c0");
            case "некрасовская линия" -> Color.PINK;
            case "троицкая линия" -> Color.web("99CC00");
            case "солнцевская линия" -> Color.YELLOW;
            case "бутовская линия" -> Color.web("bac8e8");
            default -> Color.BLACK;
        };
    }
}
