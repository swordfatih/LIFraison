package com.insa.lifraison.view;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
/**
 * Class HomeController extends from {@link com.insa.lifraison.view.ViewController}
 * Create a window with a "Load Map" button, based on the home.fxml file
 */
public class HomeController extends ViewController {
    /**
     * {@link javafx.scene.control.Label}
     */
    @FXML
    private Label label;

    /**
     * initialize the home window
     * This methods is automatically called at the creation of the class
     */
    public void initialize() {
        FadeTransition ft = new FadeTransition(Duration.millis(500), label);
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);

        label.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if(newValue) {
                ft.play();
            }
        });
    }

    /**
     * This function is automatically called when there is a click on the "Load File" button
     * @param event the input event
     */
    @FXML
    private void loadMap(ActionEvent event) {
        event.consume();
        this.controller.loadMap();
    }
}
