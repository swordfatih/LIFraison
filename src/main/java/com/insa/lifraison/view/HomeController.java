package com.insa.lifraison.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;

public class HomeController extends ViewController {
    @FXML
    private Label label;

    public void initialize() {
        label.setText("Home page");
    }

    @FXML
    private void loadMap(ActionEvent event) {
        event.consume();

        this.controller.loadMap();
    }
}
