package com.insa.lifraison;

import com.insa.lifraison.controller.Controller;
import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.view.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public Scene load(String scenePath, String stylePath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(scenePath));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);

            URL style = Objects.requireNonNull(getClass().getResource(stylePath));
            scene.getStylesheets().add(style.toExternalForm());

            return scene;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene home = load("view/scene.fxml", "style/main.css");
        Scene load = load("view/load.fxml", "style/main.css");

        stage.setTitle("LIFraison");
        stage.setScene(home);
        stage.show();

        // ViewController view = loader.getController();
        // Controller controller = new Controller(view);
    }
}