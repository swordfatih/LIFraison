package com.insa.lifraison.view;

import com.insa.lifraison.model.CityMap;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.File;

public class MapController extends ViewController {
    CityMap map;

    @FXML
    private Label label;

    public void initialize() {
        label.setText("Map page");
    }

    public void setMap(CityMap map, File file) {
        this.map = map;

        this.label.setText(file.getAbsolutePath());
    }
}
