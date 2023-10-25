package com.insa.lifraison;

import com.insa.lifraison.controller.Controller;
import com.insa.lifraison.view.View;
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
        Controller controller = new Controller();
        View view = new View(stage, controller);
        controller.setView(view);

        view.loadScene("home", "home.fxml", "style/home.css");
        view.loadScene("map", "map.fxml", "style/map.css");

        stage.setTitle("LIFraison");
        view.navigate("home");
        stage.show();
    }
}