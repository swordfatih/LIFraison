package com.insa.lifraison.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;;

public class ViewController {
    @FXML
    private Label label;

    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }

    public void setLabelText(String label) {
        this.label.setText(label);
    }
}
