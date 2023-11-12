package com.insa.lifraison.view;

import com.insa.lifraison.model.*;
import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.observer.Observer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import static java.lang.Math.min;

public class MapController extends ViewController implements Observer {
    @FXML
    private StackPane pane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Canvas foreground;
    @FXML
    private Canvas background;
    private CityMap map;
    private double scale;
    private double longitudeOffset;
    private double latitudeOffset;
    private final double pointSize = 10;
    private final double zoomFactor = 1.2;


    public void initialize() {
        scrollPane.addEventFilter(ScrollEvent.ANY, this::onScrollEvent);
    }

    /**
     * Redraw the map when the model is modified
     * @param notifType : the type of update
     */
    @Override
    public void update(Observable.NotifType notifType) {
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
        double XScale = this.background.getWidth() / sizeLongitude;
        double YScale = this.background.getHeight() / sizeLatitude;
        scale = min(XScale,YScale);
        longitudeOffset = -scale * this.map.getMinLongitude();
        latitudeOffset = scale * this.map.getMaxLatitude();

        GraphicsContext gcBG = this.background.getGraphicsContext2D();
        gcBG.setFill(Color.TRANSPARENT);
        gcBG.clearRect(0, 0, this.background.getWidth(), this.background.getHeight());

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
        foreground.toFront();
        GraphicsContext gcFG = foreground.getGraphicsContext2D();
        gcFG.setFill(Color.TRANSPARENT);
        gcFG.clearRect(0, 0, foreground.getWidth(), foreground.getHeight());

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

    /**
     * Give the intersection clicked by the user to the controller
     * @param event mouseListener on the map
     */
    public void mapClick(MouseEvent event){
        // left click
        if(event.getButton() == MouseButton.PRIMARY){
            Intersection intersection = findNearestIntersection(event.getX(), event.getY());
            DeliveryRequest delivery = findNearestDelivery(event.getX(), event.getY());
            this.controller.leftClick(intersection, delivery);
        } else if(event.getButton() == MouseButton.SECONDARY){
            this.controller.rightClick();
        }
    }

    public void zoomIn() {
        pane.setScaleX(pane.getScaleX() * zoomFactor);
        pane.setScaleY(pane.getScaleY() * zoomFactor);
    }

    public void zoomOut() {
        if (pane.getScaleX() > 1) {
            pane.setScaleX(pane.getScaleX() / zoomFactor);
            pane.setScaleY(pane.getScaleY() / zoomFactor);
        }
    }

    private void onScrollEvent(ScrollEvent event){
        if (event.getDeltaY() > 0) {
            zoomIn();
        } else {
            zoomOut();
        }
        event.consume();
    }
}
