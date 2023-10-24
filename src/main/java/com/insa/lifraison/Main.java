package com.insa.lifraison;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

/**
 * Main class
 */
public class Main extends Application {
    /**
     * Entry point of the project
     * @param args No argument required
     */
    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL scenePath = Objects.requireNonNull(getClass().getResource("scene.fxml"));
        Parent root = FXMLLoader.load(scenePath);

        Scene scene = new Scene(root);
        URL stylePath = Objects.requireNonNull(getClass().getResource("styles.css"));
        scene.getStylesheets().add(stylePath.toExternalForm());

        stage.setTitle("L'IF'raison");
        stage.setScene(scene);
        stage.show();
    }
}