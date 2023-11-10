package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;
import com.insa.lifraison.model.*;
import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.observer.Observer;
import javafx.animation.FillTransition;
import javafx.animation.StrokeTransition;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import static java.lang.Math.min;

public class MapPaneDrawer extends Pane implements Observer {
    private Controller controller;

    private Pane foregroundMap, backgroudMap;
    private CityMap map;

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
        foregroundMap = new Pane();
        backgroudMap = new Pane();
        foregroundMap.setPrefHeight(height);
        foregroundMap.setPrefWidth(width);
        backgroudMap.setPrefWidth(width);
        backgroudMap.setPrefHeight(height);
        this.getChildren().add(foregroundMap);
        this.getChildren().add(backgroudMap);
    }

    /**
     * add the controller to this
     * @param controller the app controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Redraw the map when the model is modified
     * @param notifType : the type of update
     */
    @Override
    public void update(Observable.NotifType notifType, Observable observable, Object arg){
        switch (notifType) {
            case FULL_UPDATE -> {
                updateBackground();
                updateForeground();
            }
            case ADD -> {
                if(arg instanceof Tour){

                }else if(arg instanceof DeliveryRequest deliveryRequest){
                    drawIntersectionPoint(this, deliveryRequest.getDestination(), Color.BLUE, deliveryPointSize);
                }
            }
            case DELETE -> {
                
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
    }

    /**
     * redraw the background of the map meaning roads and the warehouse
     */
    public void updateBackground(){
        //computing scale
        double sizeLatitude = this.map.getMaxLatitude()-this.map.getMinLatitude();
        double sizeLongitude = this.map.getMaxLongitude()-this.map.getMinLongitude();
        double XScale = backgroudMap.getPrefWidth() / sizeLongitude;
        double YScale = backgroudMap.getPrefHeight() / sizeLatitude;
        scale = min(XScale,YScale);
        longitudeOffset = -scale * this.map.getMinLongitude();
        latitudeOffset = scale * this.map.getMaxLatitude();

        backgroudMap.getChildren().clear();

        //adding map segments
        for (Segment segment : this.map.getSegments()){
            drawSegmentLine(backgroudMap, segment, Color.BLACK, 2);
        }

        //adding map intersections
        for (Intersection intersection : this.map.getIntersections()){
            drawIntersectionPoint(backgroudMap, intersection, Color.DARKGREY, intersectionPointSize);
        }

        // adding the warehouse
        Warehouse warehouse = this.map.getWarehouse();
        if(warehouse != null) {
            drawIntersectionPoint(backgroudMap, warehouse.intersection, Color.RED, deliveryPointSize);
        }
    }

    /**
     * redraw the foreground meaning delivery points and tours
     */
    void updateForeground(){
        foregroundMap.toFront();
        foregroundMap.getChildren().clear();

        Color[] colorChoice = {Color.BLUE, Color.ORANGE, Color.LAWNGREEN};
        int i = 0;
        for(Tour tour : map.getTours()) {
            Color colorValue = colorChoice[i%colorChoice.length];
            for(TourStep tourStep : tour.getTourSteps()) {
                for(Segment segment : tourStep.segments) {
                    drawSegmentLine(foregroundMap, segment, colorValue, 4);
                }
            }
            for(DeliveryRequest delivery : tour.getDeliveries()) {
                if (map.getSelectedDelivery() == delivery){
                    drawIntersectionPoint(foregroundMap, delivery.getDestination(), Color.PURPLE, deliveryPointSize);
                } else {
                    drawIntersectionPoint(foregroundMap, delivery.getDestination(), colorValue, deliveryPointSize);
                }
            }
            i++;
        }
        Tour tour1 = map.getTours().get(map.getTours().size()-1);
        if (!tour1.getTourSteps().isEmpty()) {
            Path path1 = new Path();
            Intersection tmp = map.getWarehouse().intersection;
            path1.getElements().add(getmoveTo(tmp));
            for (TourStep tourStep : tour1.getTourSteps()) {
                for (Segment segment : tourStep.segments) {
                    if (segment.destination == tmp) {
                        tmp = segment.origin;
                    } else {
                        tmp = segment.destination;
                    }
                    path1.getElements().add(getLineTo(tmp));
                }
            }
            StrokeTransition filling = new StrokeTransition(Duration.seconds(5), Color.BLUE, Color.RED);
            //path1.setStroke(Color.RED);
            filling.setShape(path1);
            path1.setStrokeWidth(2);
            this.getChildren().add(path1);
            filling.playFromStart();
        }

/*        Path path = new Path();
        path.setStrokeWidth(5);
        path.setStroke(Color.BLUE);
        path.getElements().add(new MoveTo(200, 200));
        path.getElements().add(new LineTo(100, 100));
        path.getElements().add(new LineTo(200, 100));
        this.getChildren().add(path);*/
    }

    /**
     * Highlight an intersection of the delivery
     * @param board The pane where the intersection must be drawn
     * @param intersection The Intersection to draw
     * @param color
     * @param pointSize
     */
    public void drawIntersectionPoint(Pane board, Intersection intersection, Color color, double pointSize){
        double yCoordinate = -scale * intersection.latitude + latitudeOffset;
        double xCoordinate = scale * intersection.longitude + longitudeOffset;
        Circle deliveryPoint = new Circle(xCoordinate, yCoordinate, pointSize);
        deliveryPoint.setOnMouseClicked(this::onIntersectionClick);
        deliveryPoint.setFill(color);
        deliveryPoint.setId(intersection.id);
        board.getChildren().add(deliveryPoint);
    }

    private void onIntersectionClick(MouseEvent event){
        System.out.println("truc : " + event.getSource());
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
     * find the nearest intersection of a clicked point
     * @param mouseX mouseX
     * @param mouseY mouseY
     * @return the nearest Intersection
     */
    public Intersection findNearestIntersection(double mouseX, double mouseY) {
        double longitudePos = (mouseX - longitudeOffset) / scale;
        double latitudePos = -(mouseY - latitudeOffset) / scale;
        return map.getClosestIntersection(longitudePos, latitudePos);
    }

    /**
     * find the nearest Delivery of a clicked point
     * @param mouseX mouseX
     * @param mouseY mouseY
     * @return the nearest DeliveryRequest
     */
    public DeliveryRequest findNearestDelivery(double mouseX, double mouseY) {
        double longitudePos = (mouseX - longitudeOffset) / scale;
        double latitudePos = -(mouseY - latitudeOffset) / scale;
        return map.getClosestDelivery(longitudePos, latitudePos);
    }
}
