package org.example;
package ui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import model.Station;
import service.MetroService;

import java.util.List;
import java.util.Map;

public class Controller {

    @FXML private ComboBox<String> startComboBox;
    @FXML private ComboBox<String> endComboBox;
    @FXML private Canvas mapCanvas;
    @FXML private TextArea routeTextArea;

    private MetroMapCanvas mapDrawer;
    private MetroService metroService;

    @FXML
    public void initialize() {
        metroService = new MetroService();
        mapDrawer = new MetroMapCanvas(mapCanvas);

        List<Station> allStations = metroService.getAllStations();
        for (Station s : allStations) {
            startComboBox.getItems().add(s.getName());
            endComboBox.getItems().add(s.getName());
        }

        Map<String, List<Station>> lines = metroService.getStationsByLine();
        List<int[]> connections = metroService.getConnections();
        mapDrawer.drawMetroMap(lines, connections);
    }

    @FXML
    public void onFindRoute() {
        String start = startComboBox.getValue();
        String end = endComboBox.getValue();
        if (start == null || end == null || start.equals(end)) {
            routeTextArea.setText("Выберите разные станции.");
            return;
        }

        List<Station> path = metroService.findShortestPath(start, end);
        mapDrawer.drawMetroMap(metroService.getStationsByLine(), metroService.getConnections());
        mapDrawer.highlightRoute(path);

        StringBuilder sb = new StringBuilder("Маршрут:\n");
        for (Station s : path) {
            sb.append(s.getName()).append(" (").append(s.getLine()).append(")\n");
        }
        routeTextArea.setText(sb.toString());
    }
}
