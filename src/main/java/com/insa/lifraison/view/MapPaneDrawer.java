package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;
import com.insa.lifraison.model.*;
import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.observer.Observer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.LinkedList;

import static java.lang.Math.min;

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
        System.out.println("New notification");
        switch (notifType) {
            case LIGHT_UPDATE -> {
                updateBackground();
            }
            case ADD -> {
                System.out.println(observable);
                if(arg instanceof Tour){
                    observeTour((Tour) arg);
                }
                else {
                    DeliveryRequest newDelivery = (DeliveryRequest) arg;
                    if (observable instanceof Tour) {
                        System.out.println("draw temporary delivery");
                        drawIntersectionPoint(this, newDelivery.getDestination(), Color.BLUE, 2);
                    } else {
                        System.out.println("draw permanent delivery");
                        drawIntersectionPoint(this, newDelivery.getDestination(), Color.PURPLE, 2);
                    }
                }
            }
            case DELETE -> {
                if(arg instanceof Tour){
                    deleteTour((Tour) arg);
                }else {
                    DeliveryRequest newDelivery = (DeliveryRequest) arg;
                    deleteIntersectionPoint(newDelivery.getDestination());
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
        this.tours = map.getTours();
        for(Tour tour : tours){
            System.out.println("Observe a new tour");
            tour.addObserver(this);
        }
    }

    public void observeTour(Tour tour){
        this.tours.add(tour);
        tour.addObserver(this);
    }

    public void deleteTour(Tour tour){
        this.tours.remove(tour);
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
            drawIntersectionPoint(this, intersection, Color.DARKGREY, 1);
        }

        // adding the warehouse
        Warehouse warehouse = this.map.getWarehouse();
        drawIntersectionPoint(this, warehouse.intersection, Color.RED, 2);

        System.out.println("je dessine tout");
        //draw the delivery which is selected
        DeliveryRequest selectedDelivery = map.getSelectedDelivery();
        if( selectedDelivery != null){
            drawIntersectionPoint(this, selectedDelivery.getDestination(), Color.PURPLE, 2);
        }

        //draw deliveries and path
        Color[] colorChoice = {Color.BLUE, Color.ORANGE, Color.LAWNGREEN};
        Color colorValue;
        int i = 0;
        for (Tour tour : map.getTours()){
            colorValue = colorChoice[(i++)%colorChoice.length];
            for(DeliveryRequest delivery : tour.getDeliveries()) {
                drawIntersectionPoint(this, delivery.getDestination(), colorValue, 2);
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
            path.setStroke(colorValue);
            this.getChildren().add(path);
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
     * @param color
     * @param type 1 for an intersection and 2 for a delivery
     */
    public void drawIntersectionPoint(Pane board, Intersection intersection, Color color, int type){
        double yCoordinate = -scale * intersection.latitude + latitudeOffset;
        double xCoordinate = scale * intersection.longitude + longitudeOffset;
        Circle circle = new Circle(xCoordinate, yCoordinate, 0, color);
        circle.setId(intersection.id);
        if (type == 1){ // the circle represent an intersection
            circle.setRadius(intersectionPointSize);
            circle.setOnMouseClicked(this::onIntersectionClick);
        } else { // the circle represent a delivery
            circle.setRadius(deliveryPointSize);
            circle.setOnMouseClicked(this::onDeliveryClick);
        }
        board.getChildren().add(circle);
    }

    public void deleteIntersectionPoint(Intersection intersection){
        System.out.println("delete a delivery");
        Circle foundCircle = findCircleDeliveryById(this, intersection.id);
        if(foundCircle != null ){
            boolean removed = this.getChildren().remove(foundCircle);
        }
    }

    private Circle findCircleDeliveryById(Pane pane, String id) {
        for (javafx.scene.Node node : pane.getChildren()) {
            if (node instanceof Circle && id.equals(node.getId()) && ((Circle) node).getRadius() > intersectionPointSize) {
                return (Circle) node;
            }
        }
        return null; // Circle with the specified id not found
    }

    private void onIntersectionClick(MouseEvent event){
        if(event.getButton() == MouseButton.PRIMARY) {
            Circle clicked = (Circle) event.getSource();
            String idClicked = clicked.getId();
            /// TODO : a remplacer avec une hashmap <id, intesection> !!!!
            Intersection intersectionFound = null;
            for (Intersection intersection : map.getIntersections()) {
                if (idClicked.equals(intersection.id)) {
                    intersectionFound = intersection;
                    break;
                }
            }
            System.out.println("Intersection clicked found " + intersectionFound);
            controller.leftClick(intersectionFound);
        }
    }

    private void onDeliveryClick(MouseEvent event){
        if(event.getButton() == MouseButton.PRIMARY) {
            Circle clicked = (Circle) event.getSource();
            String idClicked = clicked.getId();
            Intersection intersectionFound = null;
            for (Intersection intersection : map.getIntersections()) {
                if (idClicked.equals(intersection.id)) {
                    intersectionFound = intersection;
                    break;
                }
            }
            System.out.println("delivery clicked found on " + intersectionFound);
            controller.leftClick(intersectionFound);
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


    /**
     * find the nearest intersection of a clicked point
     * @param mouseX mouseX
     * @param mouseY mouseY
     * @return the nearest Intersection

    public Intersection findNearestIntersection(double mouseX, double mouseY) {
        double longitudePos = (mouseX - longitudeOffset) / scale;
        double latitudePos = -(mouseY - latitudeOffset) / scale;
        return map.getClosestIntersection(longitudePos, latitudePos);
    }*/


    /**
     * find the nearest Delivery of a clicked point
     * @param mouseX mouseX
     * @param mouseY mouseY
     * @return the nearest DeliveryRequest

    public DeliveryRequest findNearestDelivery(double mouseX, double mouseY) {
        double longitudePos = (mouseX - longitudeOffset) / scale;
        double latitudePos = -(mouseY - latitudeOffset) / scale;
        return map.getClosestDelivery(longitudePos, latitudePos);
    }*/

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
