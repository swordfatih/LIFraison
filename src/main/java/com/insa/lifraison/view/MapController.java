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
import java.util.Iterator;
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
        Iterator<Segment> segmentIterator = this.map.getSegmentsIterator();
        while (segmentIterator.hasNext()){
            addSegmentLine(segmentIterator.next());
        }

        // adding the warehouse
        Warehouse warehouse = this.map.getWarehouse();

        if(warehouse != null) {
            double xWarehouse = scale * warehouse.getIntersection().longitude + longitudeOffset;
            double yWarehouse = -scale * warehouse.getIntersection().latitude + latitudeOffset;
            Circle posWarehouse = new Circle(xWarehouse, yWarehouse, 5);
            posWarehouse.setFill(Color.RED);
            this.mapBackground.getChildren().add(posWarehouse);
        }
    }

    void updateForeground(){
    Iterator<Tour> toursIterator = this.map.getToursIterator();
        while(toursIterator.hasNext()) {
            Iterator<DeliveryRequest> deliveriesIterator = toursIterator.next().getDeliveriesIterator();
            while (deliveriesIterator.hasNext()) {
                addDeliveryPoint(deliveriesIterator.next(), Color.GRAY);
            }
        }
    }

    public void addSegmentLine(Segment segment){
        double yOrigin = -scale * segment.origin.latitude + latitudeOffset;
        double xOrigin = scale * segment.origin.longitude + longitudeOffset;
        double yDest = -scale * segment.destination.latitude + latitudeOffset;
        double xDest = scale * segment.destination.longitude + longitudeOffset;
        this.mapBackground.getChildren().add(new Line(xOrigin,yOrigin,xDest,yDest));
    }

    @FXML
    private void loadDeliveries(ActionEvent event) {
        event.consume();

        this.controller.loadDeliveries();
    }
    public void addDeliveryPoint(DeliveryRequest delivery, Color color){
        double yCoordinate = -scale * delivery.getDestination().latitude + latitudeOffset;
        double xCoordinate = scale * delivery.getDestination().longitude + longitudeOffset;
        Circle deliveryPoint = new Circle(xCoordinate,yCoordinate,5);
        deliveryPoint.setFill(color);
        this.mapForeground.getChildren().add(deliveryPoint);
    }
}
