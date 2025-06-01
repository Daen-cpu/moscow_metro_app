package org.example.ui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import org.example.db.StationDAO;
import org.example.model.Station;

import java.util.List;
import java.util.Map;

public class Controller {

    @FXML
    private Canvas canvas;

    @FXML
    private ComboBox<String> startCombo;

    @FXML
    private ComboBox<String> endCombo;

    @FXML
    private TextArea routeArea;

    private MetroMapCanvas mapCanvas;
    private StationDAO dao;
    private Map<String, List<Station>> lines;
    private List<int[]> connections;
    private Map<Integer, Station> stationMap;

    @FXML
    public void initialize() {
        dao = new StationDAO();
        lines = dao.getStationsGroupedByLine();
        connections = dao.getConnections();
        stationMap = dao.getStationMap();

        Station.setStationMap(stationMap);

        mapCanvas = new MetroMapCanvas(canvas);
        mapCanvas.drawMetroMap(lines, connections);

        // Заполнение ComboBox
        List<Station> allStations = dao.getAllStations();
        for (Station s : allStations) {
            startCombo.getItems().add(s.getName());
            endCombo.getItems().add(s.getName());
        }
    }

    @FXML
    public void onFindRouteClicked() {
        String from = startCombo.getValue();
        String to = endCombo.getValue();
        if (from == null || to == null) return;

        Station start = dao.getByName(from);
        Station end = dao.getByName(to);
        if (start == null || end == null) return;

        List<Station> route = dao.bfsPath(start, end);

        if (route.isEmpty()) {
            routeArea.setText("Маршрут не найден.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Station s : route) {
                sb.append(s.getName()).append(" (").append(s.getLine()).append(")").append("\n");
            }
            routeArea.setText(sb.toString());
            mapCanvas.drawMetroMap(lines, connections);
            mapCanvas.highlightRoute(route);
        }
    }
}
