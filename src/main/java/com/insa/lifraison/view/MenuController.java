package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;

public class MenuController extends ViewController {
    /**
     * {@link javafx.scene.control.Button}
     */
    @FXML
    private Button loadMapButton;
    /**
     * {@link javafx.scene.control.Button}
     */
    @FXML
    private Button loadDeliveriesButton;
    /**
     * {@link javafx.scene.control.Button}
     */
    @FXML
    private Button addDeliveryButton;
    /**
     * {@link javafx.scene.control.Button}
     */
    @FXML
    private Button removeDeliveryButton;
    /**
     * {@link javafx.scene.control.Button}
     */
    @FXML
    private Button undoButton;
    /**
     * {@link javafx.scene.control.Button}
     */
    @FXML
    private Button redoButton;
    /**
     * {@link javafx.scene.control.Button}
     */
    @FXML
    private Button addTourButton;
    /**
     * {@link javafx.scene.control.Button}
     */
    @FXML
    private Button removeTourButton;
    /**
     * {@link javafx.scene.control.Button}
     */
    @FXML
    private Button computePlanButton;
    /**
     * {@link javafx.scene.control.Button}
     */
    @FXML
    private Button saveButton;
    /**
     * {@link javafx.scene.layout.GridPane}
     */
    @FXML
    private GridPane buttons;

    /**
     * initialize the home window
     * This methods is automatically called at the creation of the class
     */
    public void initialize() {
        KeyCombination kcUndo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        KeyCombination kcRedo = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
        KeyCombination kcCancel = new KeyCodeCombination(KeyCode.ESCAPE);

        // on Scene loaded event
        undoButton.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.getAccelerators().put(kcUndo, () -> this.controller.undo());
                newScene.getAccelerators().put(kcRedo, () -> this.controller.redo());
                newScene.getAccelerators().put(kcCancel, () -> this.controller.rightClick());
            }
        });
        buttons.getChildren().forEach((button) -> button.setDisable(true));;
    }

    /**
     * Called after a click on the "Load Map" button
     * Notify the controller
     * @param event the input event
     */
    @FXML
    private void loadMap(ActionEvent event) {
        event.consume();
        this.controller.loadMap();
    }

    /**
     * Called after a click on the "Load Delivery" button
     * Notify the controller
     * @param event the input event
     */
    @FXML
    private void loadDeliveries(ActionEvent event) {
        event.consume();
        this.controller.loadDeliveries();
    }

    /**
     * Called after a click on the "Add Delivery" button
     * Notify the controller
     * @param event the input event
     */
    @FXML
    private void addDelivery(ActionEvent event){
        event.consume();
        this.controller.addDelivery();
    }

    /**
     * Called after a click on the "Remove Delivery" button
     * Notify the controller
     * @param event the input event
     */
    @FXML
    private void removeDelivery(ActionEvent event) {
        event.consume();
        this.controller.removeDelivery();
    }

    /**
     * Called after a click on the "Undo" button
     * Notify the controller
     * @param event the input event
     */
    @FXML
    private void undo(ActionEvent event) {
        event.consume();
        this.controller.undo();
    }

    /**
     * Called after a click on the "Redo" button
     * Notify the controller
     * @param event the input event
     */
    @FXML
    private void redo(ActionEvent event) {
        event.consume();
        this.controller.redo();
    }

    /**
     * Called after a click on the "Compute Plan" button
     * Notify the controller
     * @param event the input event
     */
    @FXML
    private void computePlan(ActionEvent event) {
        event.consume();
        this.controller.computePlan();
    }

    /**
     * Called after a click on the "Add Tour" button
     * Notify the controller
     * @param event the input event
     */
    @FXML
    private void addTour(ActionEvent event) {
        event.consume();
        this.controller.addTour();
    }

    /**
     * Called after a click on the "Remove Tour" button
     * Notify the controller
     * @param event the input event
     */
    @FXML
    private void removeTour(ActionEvent event) {
        event.consume();
        this.controller.removeTour();
    }

    /**
     * Called after a click on the "Save Deliveries" button
     * Notify the controller
     * @param event the input event
     */
    @FXML
    private void saveDeliveries(ActionEvent event) {
        event.consume();
        this.controller.saveDeliveries();
    }

    /**
     * Called after a click on the "Save Roadmap" button
     * Notify the controller
     * @param event the input event
     */
    @FXML
    private void saveRoadmap(ActionEvent event) {
        event.consume();
        this.controller.saveRoadmap();
    }
}
