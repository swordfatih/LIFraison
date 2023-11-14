package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.*;

public class MenuController extends ViewController {
    @FXML
    private Button undoButton;

    private int tourNumber;

    @Override
    public void setController(Controller controller) {
        super.setController(controller);
    }

    public void initialize() {
        KeyCombination kcUndo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        KeyCombination kcRedo = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);

        // on Scene loaded event
        undoButton.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.getAccelerators().put(kcUndo, () -> this.controller.undo());
                newScene.getAccelerators().put(kcRedo, () -> this.controller.redo());
            }
        });
    }

    @FXML
    private void loadMap(ActionEvent event) {
        event.consume();
        this.controller.loadMap();
        this.controller.changeMap();
    }

    @FXML
    private void loadDeliveries(ActionEvent event) {
        event.consume();
        this.controller.loadDeliveries();
    }

    /**
     * Go to state AddDelivery in the controller and add informations about this state
     * @param event user click on the button addDelivery
     */
    @FXML
    private void addDelivery(ActionEvent event){
        event.consume();
        this.controller.addDelivery();
    }

    @FXML
    private void deleteDelivery(ActionEvent event) {
        event.consume();
        this.controller.deleteDelivery();
    }

    @FXML
    private void undo(ActionEvent event) {
        event.consume();
        this.controller.undo();
    }

    @FXML
    private void redo(ActionEvent event) {
        event.consume();
        this.controller.redo();
    }

    @FXML
    private void computePlan(ActionEvent event) {
        event.consume();
        this.controller.computePlan();
    }

    @FXML
    private void addTour(ActionEvent event) {
        event.consume();
        tourNumber += 1;
        this.controller.addTour();
    }

    @FXML
    private void removeTour(ActionEvent event) {
        event.consume();
        this.controller.removeTour();
    }

    @FXML
    private void saveDeliveries(ActionEvent event) {
        event.consume();
        this.controller.save();
    }
}
