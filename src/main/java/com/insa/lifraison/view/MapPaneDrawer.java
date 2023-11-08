package com.insa.lifraison.view;

import com.insa.lifraison.model.*;
import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.observer.Observer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import static java.lang.Math.min;

public class MapPaneDrawer extends Pane implements Observer {
    private Canvas foregroundMap, backgroudMap;
    private CityMap map;

    private double scale;
    private double longitudeOffset;
    private double latitudeOffset;

    private final double pointSize = 10;

    /**
     * Create 2 canvas = layers to draw the map
     * @param width = the wanted width for the map
     * @param height : the wanted height for the map
     */
    public MapPaneDrawer(double width, double height){
        foregroundMap = new Canvas(width, height);
        backgroudMap = new Canvas(width, height);
        this.getChildren().add(foregroundMap);
        this.getChildren().add(backgroudMap);
    }

    /**
     * Redraw the map when the model is modified
     * @param notifType : the type of update
     */
    @Override
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
        double XScale = backgroudMap.getWidth() / sizeLongitude;
        double YScale = backgroudMap.getHeight() / sizeLatitude;
        scale = min(XScale,YScale);
        longitudeOffset = -scale * this.map.getMinLongitude();
        latitudeOffset = scale * this.map.getMaxLatitude();

        GraphicsContext gcBG = backgroudMap.getGraphicsContext2D();
        gcBG.setFill(Color.TRANSPARENT);
        gcBG.clearRect(0, 0, backgroudMap.getWidth(), backgroudMap.getHeight());

        //adding map segments
        gcBG.setStroke(Color.BLACK);
        for (Segment segment : this.map.getSegments()){
            drawSegmentLine(gcBG, segment);
        }

        // adding the warehouse
        Warehouse warehouse = this.map.getWarehouse();
        gcBG.setFill(Color.RED);
        if(warehouse != null) {
            drawIntersectionPoint(gcBG, warehouse.intersection);
        }
    }

    /**
     * redraw the foreground meaning delivery points and tours
     */
    void updateForeground(){
        foregroundMap.toFront();
        GraphicsContext gcFG = foregroundMap.getGraphicsContext2D();
        gcFG.setFill(Color.TRANSPARENT);
        gcFG.clearRect(0, 0, foregroundMap.getWidth(), foregroundMap.getHeight());

        Color[] colorChoice = {Color.BLUE, Color.ORANGE, Color.LAWNGREEN};
        gcFG.setLineWidth(3);
        int i = 0;
        for(Tour tour : map.getTours()) {
            Color colorValue = colorChoice[i%colorChoice.length];
            gcFG.setFill(colorValue);
            gcFG.setStroke(colorValue);
            for(TourStep tourStep : tour.getTourSteps()) {
                for(Segment segment : tourStep.segments) {
                    drawSegmentLine(gcFG, segment);
                }
            }
            for(DeliveryRequest delivery : tour.getDeliveries()) {
                if (map.getSelectedDelivery() == delivery){
                    gcFG.setFill(Color.PURPLE);
                    drawIntersectionPoint(gcFG, delivery.getDestination());
                    gcFG.setFill(colorValue);
                } else {
                    drawIntersectionPoint(gcFG, delivery.getDestination());
                }
            }
            i++;
        }
    }

    /**
     * Highlight an intersection of the delivery
     * @param gc The graphical context = drawer
     * @param intersection The Intersection to draw
     */
    public void drawIntersectionPoint(GraphicsContext gc, Intersection intersection){
        double yCoordinate = -scale * intersection.latitude + latitudeOffset;
        double xCoordinate = scale * intersection.longitude + longitudeOffset;
        gc.fillOval(xCoordinate - pointSize/2, yCoordinate - pointSize/2, pointSize, pointSize);
    }

    /**
     * Highlight a segment of the tour
     * @param gc The graphical context = drawer
     * @param segment The segment to highlight
     */
    public void drawSegmentLine(GraphicsContext gc, Segment segment){
        double yOrigin = -scale * segment.origin.latitude + latitudeOffset;
        double xOrigin = scale * segment.origin.longitude + longitudeOffset;
        double yDest = -scale * segment.destination.latitude + latitudeOffset;
        double xDest = scale * segment.destination.longitude + longitudeOffset;
        gc.strokeLine(xOrigin, yOrigin, xDest, yDest);
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
