package com.insa.lifraison.view;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

public class HomeController extends ViewController {
    @FXML
    private Label label;

    public void initialize() {
        FadeTransition ft = new FadeTransition(Duration.millis(500), label);
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);

        Path path = new Path();
        path.getElements().add(new MoveTo(100, 20));
        path.getElements().add(new CubicCurveTo(380, 0, 380, 120, 200, 120));
        path.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));
        path.getElements().add(new CubicCurveTo(380, 340, -500, 240, 100, 20));
        PathTransition pt = new PathTransition();
        pt.setDuration(Duration.millis(6000));
        pt.setPath(path);
        pt.setNode(label);
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pt.setCycleCount(1);
        pt.setAutoReverse(true);

        label.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if(newValue) {
                ft.play();
                pt.play();
            }
        });
    }

    @FXML
    private void loadMap(ActionEvent event) {
        event.consume();

        this.controller.loadMap();
    }
}
