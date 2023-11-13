package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;
import com.insa.lifraison.model.*;
import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.observer.Observer;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

import java.util.LinkedList;
import java.util.Objects;

import static java.lang.Math.min;
import static java.lang.Math.nextUp;

public class MapPaneDrawer extends Pane implements Observer {
    Controller controller;
    private CityMap map;
    private LinkedList<Tour> tours;

    private double scale;
    private double longitudeOffset;
    private double latitudeOffset;

    private final double deliveryPointSize = 6;
    private final double intersectionPointSize = 3;


    /**
     * Create 2 canvas = layers to draw the map
     * @param width = the wanted width for the map
     * @param height : the wanted height for the map
     */
    public MapPaneDrawer(double width, double height){
        this.setPrefSize(width, height);
    }

    /**
     * Redraw the map when the model is modified
     * @param notifType : the type of update
     */
    @Override
    public void update(Observable.NotifType notifType, Observable observable, Object arg){
        System.out.println(this + " received notif");
        switch (notifType) {
            case UPDATE -> {
                updateBackground();
            }
            case ADD -> {
                if(arg instanceof Tour){
                    Tour tour = (Tour) arg;
                    observeTour(tour);
                    for (DeliveryRequest delivery : tour.getDeliveries()) {
                        drawDeliveryPoint(this, delivery, tour, tour.getColor());
                    }
                }
                else if(arg instanceof DeliveryRequest){
                    DeliveryRequest newDelivery = (DeliveryRequest) arg;
                    if (observable instanceof Tour) {
                        Tour tour = (Tour) observable;
                        System.out.println("draw permanent delivery");
                        drawDeliveryPoint(this, newDelivery, tour, tour.getColor());
                    } else {
                        System.out.println("draw temporary delivery");
                        drawDeliveryPoint(this, newDelivery, null, Color.PURPLE);
                    }
                }
            }
            case REMOVE -> {
                if(arg instanceof Tour){
                    deleteTour((Tour) arg);
                }else {
                    DeliveryRequest newDelivery = (DeliveryRequest) arg;
                    deleteDeliveryPoint(newDelivery);
                }

            }
        }
    }

    /**
     * add the map and the observer for the map
     * @param map : the model to observed
     */
    public void setMap(CityMap map) {
        this.map = map;
        this.map.addObserver(this);
        for(Tour tour : map.getTours()){
            tour.addObserver(this);
        }
        this.updateBackground();
    }

    public void observeTour(Tour tour){
        tour.addObserver(this);
    }

    public void deleteTour(Tour tour){
    }


    /**
     * redraw the background of the map meaning roads and the warehouse
     */
    public void updateBackground(){
        //computing scale
        double sizeLatitude = this.map.getMaxLatitude()-this.map.getMinLatitude();
        double sizeLongitude = this.map.getMaxLongitude()-this.map.getMinLongitude();
        double XScale = this.getPrefWidth() / sizeLongitude;
        double YScale = this.getPrefHeight() / sizeLatitude;
        scale = min(XScale,YScale);
        longitudeOffset = -scale * this.map.getMinLongitude();
        latitudeOffset = scale * this.map.getMaxLatitude();

        this.getChildren().clear();

        //adding map segments
        for (Segment segment : this.map.getSegments()){
            drawSegmentLine(this, segment, Color.BLACK, 2);
        }

        //adding map intersections
        for (Intersection intersection : this.map.getIntersections()){
            drawIntersectionPoint(this, intersection, intersectionPointSize, Color.DARKGREY);
        }

        // adding the warehouse
        Warehouse warehouse = this.map.getWarehouse();
        if(warehouse != null) {
            drawIntersectionPoint(this, warehouse.intersection, deliveryPointSize,Color.RED);

            System.out.println("je dessine tout");
            //draw the delivery which is selected
            DeliveryRequest temporaryDelivery = map.getTemporaryDelivery();
            if (temporaryDelivery != null) {
                drawDeliveryPoint(this, temporaryDelivery, null ,Color.PURPLE);
            }

            //draw deliveries and path

            int i = 0;
            for (Tour tour : map.getTours()) {
                for (DeliveryRequest delivery : tour.getDeliveries()) {
                    drawDeliveryPoint(this, delivery, tour, tour.getColor());
                }
                Path path = new Path();
                Intersection tmp = map.getWarehouse().intersection;
                path.getElements().add(getmoveTo(tmp));
                for (TourStep tourStep : tour.getTourSteps()) {
                    for (Segment segment : tourStep.segments) {
                        if (segment.destination == tmp) {
                            tmp = segment.origin;
                        } else {
                            tmp = segment.destination;
                        }
                        path.getElements().add(getLineTo(tmp));
                    }
                }
                path.setId(Integer.toString(i));
                path.setStrokeWidth(5);
                path.setStroke(tour.getColor());
                this.getChildren().add(path);
            }
        }
    }

    private LineTo getLineTo(Intersection i){
        double yCoordinate = -scale * i.latitude + latitudeOffset;
        double xCoordinate = scale * i.longitude + longitudeOffset;
        return new LineTo(xCoordinate, yCoordinate);
    }

    private MoveTo getmoveTo(Intersection i){
        double yCoordinate = -scale * i.latitude + latitudeOffset;
        double xCoordinate = scale * i.longitude + longitudeOffset;
        return new MoveTo(xCoordinate, yCoordinate);
    }

    /**
     * Highlight an intersection of the delivery
     * @param board The pane where the intersection must be drawn
     * @param intersection The Intersection to draw
     */
    public void drawIntersectionPoint(Pane board, Intersection intersection, double radius, Color color){
        double yCoordinate = -scale * intersection.latitude + latitudeOffset;
        double xCoordinate = scale * intersection.longitude + longitudeOffset;
        CircleIntersection circleIntersection = new CircleIntersection(xCoordinate, yCoordinate, radius, color, intersection);
        circleIntersection.setOnMouseClicked(this::onIntersectionClick);

        board.getChildren().add(circleIntersection);
    }

    public void drawDeliveryPoint(Pane board, DeliveryRequest deliveryRequest, Tour tour, Paint color){
        double yCoordinate = -scale * deliveryRequest.getIntersection().latitude + latitudeOffset;
        double xCoordinate = scale * deliveryRequest.getIntersection().longitude + longitudeOffset;
        CircleDelivery circleDelivery = new CircleDelivery(xCoordinate, yCoordinate, deliveryPointSize, color, deliveryRequest, tour);

        circleDelivery.setOnMouseClicked(this::onDeliveryClick);
        board.getChildren().add(circleDelivery);
    }


    public void deleteDeliveryPoint(DeliveryRequest  deliveryRequest){
        System.out.println("delete a delivery");
        CircleDelivery foundCircle = findCircleDeliveryById(this, deliveryRequest);
        if(foundCircle != null ){
            boolean removed = this.getChildren().remove(foundCircle);
        }
    }

    private CircleDelivery findCircleDeliveryById(Pane pane, DeliveryRequest deliveryRequest) {
        for (javafx.scene.Node node : pane.getChildren()) {
            if (node instanceof CircleDelivery && Objects.equals(((CircleDelivery) node).getDeliveryRequest(),deliveryRequest)) {
                return (CircleDelivery) node;
            }
        }
        return null; // Circle with the specified id not found
    }

    private void onIntersectionClick(MouseEvent event){
        if(event.getButton() == MouseButton.PRIMARY) {
            CircleIntersection clicked = (CircleIntersection) event.getSource();
            System.out.println("Intersection clicked found " + clicked.getIntersection());
            controller.leftClick(clicked.getIntersection(), null, null);
        }
    }

    private void onDeliveryClick(MouseEvent event){
        if(event.getButton() == MouseButton.PRIMARY) {
            CircleDelivery clicked = (CircleDelivery) event.getSource();
            DeliveryRequest deliveryRequest = clicked.getDeliveryRequest();
            controller.leftClick(deliveryRequest.getIntersection(), deliveryRequest,  clicked.getTour());
        }
    }

    /**
     * draw a segment on the pane
     * @param board The pane where the intersection must be drawn
     * @param segment The Segment to draw
     * @param color
     * @param strokeWidth
     */
    public void drawSegmentLine(Pane board, Segment segment, Color color, double strokeWidth){
        double yOrigin = -scale * segment.origin.latitude + latitudeOffset;
        double xOrigin = scale * segment.origin.longitude + longitudeOffset;
        double yDest = -scale * segment.destination.latitude + latitudeOffset;
        double xDest = scale * segment.destination.longitude + longitudeOffset;
        Line segmentLine = new Line(xOrigin, yOrigin, xDest, yDest);
        segmentLine.setStroke(color);
        segmentLine.setStrokeWidth(strokeWidth);
        board.getChildren().add(segmentLine);
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
