package com.insa.lifraison.view;

import com.insa.lifraison.model.*;
import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.observer.Observer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.io.File;
import java.util.LinkedList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MapController extends ViewController implements Observer {
    private CityMap map;
    private double scale;
    private double latitudeOffset;
    private double longitudeOffset;

    @FXML
    private Label label;

    @FXML
    private Pane mapBackground;

    @FXML
    private Pane mapForeground;

    @FXML
    private VBox informations;

    public void initialize() {
        label.setText("Map page");
    }

    @FXML
    private void loadMap(ActionEvent event) {
        event.consume();

        this.controller.loadMap();
    }

    public void setMap(CityMap map) {
        this.map = map;
        this.map.addObserver(this);
    }

    public void update(Observable.NotifType notifType){
        switch (notifType) {
            case FULL_UPDATE -> {
                updateBackground();
                updateForeground();
            }
            case LIGHT_UPDATE -> {
                updateForeground();
            }
        }
    }

    public void updateBackground(){
        //computing scale
        double sizeLatitude = this.map.getMaxLatitude()-this.map.getMinLatitude();
        double sizeLongitude = this.map.getMaxLongitude()-this.map.getMinLongitude();
        double XScale = 940.0 / sizeLongitude;
        double YScale = 700.0/sizeLatitude;
        scale = min(XScale,YScale);
        longitudeOffset = -scale * this.map.getMinLongitude();
        latitudeOffset = scale * this.map.getMaxLatitude();

        this.mapBackground.getChildren().clear();

        //adding map segments
        for (Segment segment : this.map.getSegments()){
            addSegmentLine(this.mapBackground, segment, Color.BLACK);
        }

        // adding the warehouse
        Warehouse warehouse = this.map.getWarehouse();
        if(warehouse != null) {
            addIntersectionPoint(this.mapBackground, warehouse.getIntersection(), Color.RED);
        }
    }

    void updateForeground(){
        this.mapForeground.getChildren().clear();
        for(Tour tour : map.getTours()) {
            for(TourStep tourStep : tour.getTourSteps()) {
                for(Segment segment : tourStep.getSegments()) {
                    addSegmentLine(this.mapForeground, segment, Color.BLUE);
                }
            }
            for(DeliveryRequest delivery : tour.getDeliveries()) {
                Color color;
                if(delivery == map.getSelectedDelivery()) {
                    color = Color.PURPLE;
                } else {
                    color = Color.BLUE;
                }
                addIntersectionPoint(this.mapForeground, delivery.getDestination(), color);
            }
        }
    }

    public void addSegmentLine(Pane pane, Segment segment, Color color){
        double yOrigin = -scale * segment.origin.latitude + latitudeOffset;
        double xOrigin = scale * segment.origin.longitude + longitudeOffset;
        double yDest = -scale * segment.destination.latitude + latitudeOffset;
        double xDest = scale * segment.destination.longitude + longitudeOffset;
        Line line = new Line(xOrigin,yOrigin,xDest,yDest);
        line.setStroke(color);
        pane.getChildren().add(line);
    }

    @FXML
    private void loadDeliveries(ActionEvent event) {
        event.consume();

        this.controller.loadDeliveries();
    }
    public void addIntersectionPoint(Pane pane, Intersection intersection, Color color){
        double yCoordinate = -scale * intersection.latitude + latitudeOffset;
        double xCoordinate = scale * intersection.longitude + longitudeOffset;
        Circle deliveryPoint = new Circle(xCoordinate,yCoordinate,5);
        deliveryPoint.setFill(color);
        deliveryPoint.setId(intersection.getId());
        pane.getChildren().add(deliveryPoint);
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

    public void displayAddDeliveryInformations() {
        informations.getChildren().clear();
        Label info = new Label("Click anywhere on the map to create a new delivery");
        Button confirm = new Button("confirm");
        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                controller.confirm();
            }
        });
        informations.getChildren().addAll(info, confirm);
    }

    @FXML
    private void deleteDelivery(ActionEvent event) {
        event.consume();

        this.controller.deleteDelivery();
    }

    public void displayDeleteDeliveryInformations() {
        informations.getChildren().clear();
        Label info = new Label("Click on the map to select a delivery to delete");
        Button confirm = new Button("confirm");
        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                controller.confirm();
            }
        });
        informations.getChildren().addAll(info, confirm);
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

    public void clearInformations(){
        informations.getChildren().clear();
    }

    /**
     * Give the intersection clicked by the user to the controller
     * @param event mouseListener on the map
     */
    public void mapClick(MouseEvent event){
        // left click
        if(event.getButton() == MouseButton.PRIMARY){
            Double longitudePos = (event.getSceneX() -320 - longitudeOffset) / scale;
            Double latitudePos = -(event.getSceneY() - latitudeOffset) / scale;

            Intersection intersection = this.map.getClosestIntersection(longitudePos, latitudePos);
            DeliveryRequest delivery = this.map.getClosestDelivery(longitudePos, latitudePos);
            this.controller.leftClick(intersection, delivery);

        } else if(event.getButton() == MouseButton.SECONDARY){
            this.controller.rightClick();
        }
    }

}
