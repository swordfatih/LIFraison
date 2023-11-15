package com.insa.lifraison;

import com.insa.lifraison.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class
 */
public class MainApplication extends Application {
    /**
     * Entry point of the project
     * @param args No argument required
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Controller controller = new Controller(stage);
    }
}