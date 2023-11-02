package com.insa.lifraison.view;

import com.insa.lifraison.model.*;
import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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
        for(Tour tour : map.getTours()) {
            for(TourStep tourStep : tour.getTourSteps()) {
                for(Segment segment : tourStep.getSegments()) {
                    addSegmentLine(this.mapForeground, segment, Color.GRAY);
                }
            }
            for(DeliveryRequest delivery : tour.getDeliveries()) {
                addIntersectionPoint(this.mapForeground, delivery.getDestination(), Color.GRAY);
            }
        }
    }

    public void addSegmentLine(Pane pane, Segment segment, Color color){
        double yOrigin = -scale * segment.origin.latitude + latitudeOffset;
        double xOrigin = scale * segment.origin.longitude + longitudeOffset;
        double yDest = -scale * segment.destination.latitude + latitudeOffset;
        double xDest = scale * segment.destination.longitude + longitudeOffset;
        Line line = new Line(xOrigin,yOrigin,xDest,yDest);
        line.setFill(color);
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
        this.mapForeground.getChildren().add(deliveryPoint);
    }
}
