package com.insa.lifraison;

import com.insa.lifraison.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class
 */
public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Controller controller = new Controller(stage);
    }
}