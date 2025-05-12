package org.example;
package ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.Station;

import java.util.List;
import java.util.Map;

public class MetroMapCanvas {
    private final Canvas canvas;
    private final GraphicsContext gc;

    public MetroMapCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void drawMetroMap(Map<String, List<Station>> lines, List<int[]> connections) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Сначала рисуем соединения
        gc.setLineWidth(3);
        for (int[] conn : connections) {
            Station s1 = Station.getById(conn[0]);
            Station s2 = Station.getById(conn[1]);
            gc.setStroke(Color.GRAY);
            gc.strokeLine(s1.getX(), s1.getY(), s2.getX(), s2.getY());
        }

        // Затем рисуем станции по линиям
        for (String line : lines.keySet()) {
            gc.setFill(getColorByLine(line));
            for (Station s : lines.get(line)) {
                gc.fillOval(s.getX() - 5, s.getY() - 5, 10, 10);
            }
        }
    }

    public void highlightRoute(List<Station> route) {
        gc.setLineWidth(4);
        gc.setStroke(Color.RED);
        for (int i = 0; i < route.size() - 1; i++) {
            Station s1 = route.get(i);
            Station s2 = route.get(i + 1);
            gc.strokeLine(s1.getX(), s1.getY(), s2.getX(), s2.getY());
        }

        for (Station s : route) {
            gc.setFill(Color.RED);
            gc.fillOval(s.getX() - 6, s.getY() - 6, 12, 12);
        }
    }

    private Color getColorByLine(String line) {
        return switch (line) {
            case "Красная" -> Color.RED;
            case "Синяя" -> Color.BLUE;
            case "Зеленая" -> Color.GREEN;
            case "Желтая" -> Color.GOLD;
            case "Кольцевая" -> Color.BROWN;
            default -> Color.BLACK;
        };
    }
}
