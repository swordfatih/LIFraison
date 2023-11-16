package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;
import com.insa.lifraison.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.geometry.Point2D;


public class MapController extends ViewController {
    @FXML
    private ScrollPane mapScrollPane;

    @FXML
    private StackPane mapPane;

    @FXML
    private VBox controlBox;

    @FXML
    private Button undoButton;

    @FXML
    private Button redoButton;

    private MapPaneDrawer mapDrawer;

    public MapBoxInformation informations;

    private final double zoomFactor = 1.2;

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
        this.informations.setController(controller);
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
        mapPane.setOnMouseClicked(this::mapClick);
        informations = new MapBoxInformation();
        controlBox.getChildren().add(informations);

        mapScrollPane.addEventFilter(ScrollEvent.ANY, this::onScroll);
    }

    @FXML
    private void onScroll(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            zoomIn(event);
        } else {
            zoomOut(event);
        }
    }

    @FXML
    private void loadMap(ActionEvent event) {
        event.consume();
        this.controller.loadMap();
        this.controller.changeMap();
    }

    public void setMap(CityMap map) {
        this.mapDrawer.setMap(map);
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
    private void saveDeliveries(ActionEvent event) {
        event.consume();
        this.controller.save();
    }
    /**
     * Give the intersection clicked by the user to the controller
     * @param event mouseListener on the map
     */
    public void mapClick(MouseEvent event){
        // left click
        if(event.getButton() == MouseButton.PRIMARY){
            Intersection intersection = mapDrawer.findNearestIntersection(event.getX(), event.getY());
            DeliveryRequest delivery = mapDrawer.findNearestDelivery(event.getX(), event.getY());
            this.controller.leftClick(intersection, delivery);

        } else if(event.getButton() == MouseButton.SECONDARY){
            this.controller.rightClick();
        }
    }

    public void zoomIn(ScrollEvent event) {
        Point2D cursorPoint = getMousePointInView(event);
        double zoomFactor = 1.2;

        mapPane.setScaleX(mapPane.getScaleX() * zoomFactor);
        mapPane.setScaleY(mapPane.getScaleY() * zoomFactor);

        recenter(cursorPoint, zoomFactor);
    }

    public void zoomOut(ScrollEvent event) {
        if (mapPane.getScaleX() > 1) {
            Point2D cursorPoint = getMousePointInView(event);
            mapPane.setScaleX(mapPane.getScaleX() / zoomFactor);
            mapPane.setScaleY(mapPane.getScaleY() / zoomFactor);

            recenter(cursorPoint, zoomFactor);
        }
    }







    private void recenter(Point2D cursorPoint, double zoomFactor) {
        double hValue = mapScrollPane.getHvalue();
        double vValue = mapScrollPane.getVvalue();

        double newWidth = mapPane.getWidth() * zoomFactor;
        double newHeight = mapPane.getHeight() * zoomFactor;

        double deltaX = cursorPoint.getX() - (hValue * (newWidth - mapScrollPane.getViewportBounds().getWidth()));
        double deltaY = cursorPoint.getY() - (vValue * (newHeight - mapScrollPane.getViewportBounds().getHeight()));

        mapScrollPane.setHvalue(deltaX / newWidth);
        mapScrollPane.setVvalue(deltaY / newHeight);
    }



    private Point2D getMousePointInView(ScrollEvent event) {
        // Assuming mapScrollPane is the viewport for your mapPane
        double mouseX = event.getX();
        double mouseY = event.getY();

        double centerX = mapScrollPane.getHvalue() * (mapPane.getWidth() - mapScrollPane.getViewportBounds().getWidth());
        double centerY = mapScrollPane.getVvalue() * (mapPane.getHeight() - mapScrollPane.getViewportBounds().getHeight());

        double relativeMouseX = mouseX + centerX;
        double relativeMouseY = mouseY + centerY;

        return new Point2D(relativeMouseX, relativeMouseY);
    }




}
