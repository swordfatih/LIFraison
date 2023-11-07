package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;
import com.insa.lifraison.view.ViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class View {
    private final Stage stage;
    private final Map<String, Pair<Scene, ViewController>> scenes;
    private final Controller controller;

    public View(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.scenes = new HashMap<>();
    }

    public void loadScene(String name, String scenePath, String stylePath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(scenePath));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);

            URL style = Objects.requireNonNull(getClass().getResource(stylePath));
            scene.getStylesheets().add(style.toExternalForm());

            ViewController controller = loader.getController();
            controller.setController(this.controller);

            this.scenes.put(name, new Pair<>(scene, controller));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings (value="unchecked")
    public <T extends ViewController> T getController(String name) {
        return (T) this.scenes.get(name).getValue();
    }

    public void navigate(String name) {
        this.stage.setScene(this.scenes.get(name).getKey());
    }

    public Stage getStage() {
        return this.stage;
    }
}
