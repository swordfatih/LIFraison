package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;
import com.insa.lifraison.model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MapController extends ViewController {
    @FXML
    private StackPane mapPane;

    @FXML
    private VBox controlBox;

    @FXML
    private Button undoButton;

    @FXML
    public VBox courierList;

    @FXML
    private Button redoButton;

    @FXML
    private ScrollPane mapScrollPane;

    private MapPaneDrawer mapDrawer;

    public MapBoxInformation informations;

    public MapTourList tourList;

    private int tourNumber;

    private final double zoomFactor = 1.2;

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
        this.informations.setController(controller);
        this.mapDrawer.setController(controller);
        this.tourList.setController(controller);
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
        
        mapDrawer = new MapPaneDrawer(mapPane.getPrefWidth(), mapPane.getPrefHeight());
        mapPane.getChildren().add(mapDrawer);
        informations = new MapBoxInformation();
        tourList = new MapTourList();

        controlBox.getChildren().add(informations);
        controlBox.getChildren().add(tourList);

        mapScrollPane.addEventFilter(ScrollEvent.ANY, this::onScrollEvent);
    }

    private void onScrollEvent(ScrollEvent event){
        if (event.getDeltaY() > 0) {
            zoomIn();
        } else {
            zoomOut();
        }
        event.consume();
    }

    @FXML
    private void loadMap(ActionEvent event) {
        event.consume();
        this.controller.loadMap();
        this.controller.changeMap();
    }

    public void setMap(CityMap map) {
        this.mapDrawer.setMap(map);
        this.tourList.setMap(map);
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
        this.controller.deleteTour();
    }

    @FXML
    private void saveDeliveries(ActionEvent event) {
        event.consume();
        this.controller.save();
    }

    public void clearInformations(){
        informations.getChildren().clear();
    }
    /**
     * Give the intersection clicked by the user to the controller
     * @param event mouseListener on the map
     */

    public void mapClick(MouseEvent event){
        // left click
        /*if(event.getButton() == MouseButton.PRIMARY){
            /*Intersection intersection = mapDrawer.findNearestIntersection(event.getX(), event.getY());
            DeliveryRequest delivery = mapDrawer.findNearestDelivery(event.getX(), event.getY());
            this.controller.leftClick(intersection);

        } else*/ if(event.getButton() == MouseButton.SECONDARY){
            this.controller.rightClick();
        }
    }

    public void zoomIn() {
        mapPane.setScaleX(mapPane.getScaleX() * zoomFactor);
        mapPane.setScaleY(mapPane.getScaleY() * zoomFactor);
    }

    public void zoomOut() {
        if (mapPane.getScaleX() > 1) {
            mapPane.setScaleX(mapPane.getScaleX() / zoomFactor);
            mapPane.setScaleY(mapPane.getScaleY() / zoomFactor);
        }
    }
}
