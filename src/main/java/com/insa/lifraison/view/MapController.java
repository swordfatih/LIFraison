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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import java.util.LinkedList;
import java.util.Objects;
import static java.lang.Math.min;

public class MapController extends ViewController implements Observer {
    @FXML
    private Pane pane;
    @FXML
    private ScrollPane scrollPane;
    private CityMap map;
    private double scale;
    private double longitudeOffset;
    private double latitudeOffset;
    private final double pointSize = 10;
    private final double zoomFactor = 1.2;
    private LinkedList<Tour> tours;
    private final double deliveryPointSize = 6;
    private final double intersectionPointSize = 3;
    private Number mapWidth, mapHeight;

    public void initialize() {
        scrollPane.addEventFilter(ScrollEvent.ANY, this::onScrollEvent);

        this.scrollPane.widthProperty().addListener(((obs, oldVal, newVal) -> {
           this.mapWidth = newVal;
           refresh();
        }));

        this.scrollPane.heightProperty().addListener(((obs, oldVal, newVal) -> {
            this.mapHeight = newVal;
            refresh();
        }));
    }

    /**
     * Redraw the map when the model is modified
     * @param notifType : the type of update
     */
    @Override
    public void update(Observable.NotifType notifType, Observable observable, Object arg){
        switch (notifType) {
            case UPDATE -> {
                if(observable instanceof CityMap) {
                    refresh();
                }
                else if(observable instanceof Tour) {
                    eraseTour((Tour)observable);
                    drawTour((Tour)observable);
                }
                else if(observable instanceof DeliveryRequest) {
                    DeliveryRequest deliveryRequest = (DeliveryRequest) observable;
                    CircleDelivery circleDelivery = findCircleDelivery(deliveryRequest);
                    circleDelivery.setCenterX(scale * deliveryRequest.getIntersection().longitude + longitudeOffset);
                    circleDelivery.setCenterY(-scale * deliveryRequest.getIntersection().latitude + latitudeOffset);
                    if(deliveryRequest.isSelected()) {
                        circleDelivery.setFill(map.getSelectionColor());
                    } else {
                        circleDelivery.setFill(circleDelivery.getTour().getColor());
                    }
                }
            }
            case ADD -> {
                if(observable instanceof CityMap && arg instanceof Tour){
                    Tour tour = (Tour) arg;
                    tour.addObserver(this);
                    for(DeliveryRequest deliveryRequest : tour.getDeliveries()) {
                        deliveryRequest.addObserver(this);
                    }
                    drawTour(tour);
                }
                else if(arg instanceof DeliveryRequest){
                    DeliveryRequest newDelivery = (DeliveryRequest) arg;
                    newDelivery.addObserver(this);
                    if (observable instanceof Tour) {
                        Tour tour = (Tour) observable;
                        drawDeliveryPoint(newDelivery, tour, tour.getColor());
                    } else {
                        drawDeliveryPoint(newDelivery, null, map.getSelectionColor());
                    }
                }
            }
            case REMOVE -> {
                if(arg instanceof Tour){
                    eraseTour((Tour) arg);
                }else if(arg instanceof DeliveryRequest){
                    DeliveryRequest newDelivery = (DeliveryRequest) arg;
                    eraseDeliveryRequest(newDelivery);
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
        this.refresh();
    }

    public void drawTour(Tour tour){
        PathTour path = new PathTour(tour);
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
        path.setId("Tour" + tour.getId());
        path.setStrokeWidth(5);
        if(tour.isSelected()) {
            path.setStroke(map.getSelectionColor());
        } else {
            path.setStroke(tour.getColor());
        }
        this.pane.getChildren().add(path);
        for (DeliveryRequest delivery : tour.getDeliveries()) {
            if(tour.isSelected()) {
                drawDeliveryPoint(delivery, tour, map.getSelectionColor());
            } else {
                drawDeliveryPoint(delivery, tour, tour.getColor());
            }
        }
    }

    private void eraseTour(Tour tour) {
        PathTour pathTour = findPathTour(tour);
        if(pathTour != null) {
            this.pane.getChildren().remove(pathTour);
        }
        for(DeliveryRequest deliveryRequest : tour.getDeliveries()) {
            eraseDeliveryRequest(deliveryRequest);
        }
    }

    private PathTour findPathTour(Tour tour) {
        for (javafx.scene.Node node : this.pane.getChildren()) {
            if (node instanceof PathTour && Objects.equals(((PathTour) node).getTour(),tour)) {
                return (PathTour) node;
            }
        }
        return null;
    }

    /**
     * redraw the background of the map meaning roads and the warehouse
     */
    public void refresh(){
        //computing scale
        double sizeLatitude = this.map.getMaxLatitude() - this.map.getMinLatitude();
        double sizeLongitude = this.map.getMaxLongitude() - this.map.getMinLongitude();

        double XScale = (this.mapWidth == null ? this.pane.getPrefWidth() : this.mapWidth.intValue()) / sizeLongitude;
        double YScale = (this.mapHeight == null ? this.pane.getPrefWidth() : this.mapHeight.intValue()) / sizeLatitude;

        scale = min(XScale,YScale);
        longitudeOffset = -scale * this.map.getMinLongitude();
        latitudeOffset = scale * this.map.getMaxLatitude();

        this.pane.getChildren().clear();

        //adding map segments
        for (Segment segment : this.map.getSegments()){
            drawSegmentLine(segment, Color.BLACK, 2);
        }

        //adding map intersections
        for (Intersection intersection : this.map.getIntersections()){
            drawIntersectionPoint(intersection, intersectionPointSize, Color.DARKGREY);
        }

        // adding the warehouse
        Warehouse warehouse = this.map.getWarehouse();
        drawIntersectionPoint(warehouse.intersection, deliveryPointSize,Color.RED);

        //draw the delivery which is selected
        DeliveryRequest temporaryDelivery = map.getTemporaryDelivery();
        if (temporaryDelivery != null) {
            drawDeliveryPoint(temporaryDelivery, null ,map.getSelectionColor());
        }

        //draw deliveries and path
        for (Tour tour : map.getTours()) {
            drawTour(tour);
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
     * @param intersection The Intersection to draw
     */
    public void drawIntersectionPoint(Intersection intersection, double radius, Color color){
        double yCoordinate = -scale * intersection.latitude + latitudeOffset;
        double xCoordinate = scale * intersection.longitude + longitudeOffset;
        CircleIntersection circleIntersection = new CircleIntersection(xCoordinate, yCoordinate, radius, color, intersection);
        circleIntersection.setOnMouseClicked(this::onIntersectionClick);

        this.pane.getChildren().add(circleIntersection);
    }

    public void drawDeliveryPoint(DeliveryRequest deliveryRequest, Tour tour, Paint color){
        double yCoordinate = -scale * deliveryRequest.getIntersection().latitude + latitudeOffset;
        double xCoordinate = scale * deliveryRequest.getIntersection().longitude + longitudeOffset;
        CircleDelivery circleDelivery;
        if(deliveryRequest.isSelected()) {
            circleDelivery = new CircleDelivery(xCoordinate, yCoordinate, deliveryPointSize, map.getSelectionColor(), deliveryRequest, tour);
        } else {
            circleDelivery = new CircleDelivery(xCoordinate, yCoordinate, deliveryPointSize, color, deliveryRequest, tour);
        }

        circleDelivery.setOnMouseClicked(this::onDeliveryClick);
        this.pane.getChildren().add(circleDelivery);
    }

    public void eraseDeliveryRequest(DeliveryRequest  deliveryRequest){
        CircleDelivery foundCircle = findCircleDelivery(deliveryRequest);
        if(foundCircle != null ){
            this.pane.getChildren().remove(foundCircle);
        }
    }

    private CircleDelivery findCircleDelivery(DeliveryRequest deliveryRequest) {
        for (javafx.scene.Node node : this.pane.getChildren()) {
            if (node instanceof CircleDelivery && Objects.equals(((CircleDelivery) node).getDeliveryRequest(),deliveryRequest)) {
                return (CircleDelivery) node;
            }
        }
        return null; // Circle with the specified id not found
    }

    private void onIntersectionClick(MouseEvent event){
        if(event.getButton() == MouseButton.PRIMARY) {
            CircleIntersection clicked = (CircleIntersection) event.getSource();
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
     * Highlight a segment of the tour
     * @param segment The segment to highlight
     */
    public void drawSegmentLine(Segment segment, Color color, double strokeWidth){
        double yOrigin = -scale * segment.origin.latitude + latitudeOffset;
        double xOrigin = scale * segment.origin.longitude + longitudeOffset;
        double yDest = -scale * segment.destination.latitude + latitudeOffset;
        double xDest = scale * segment.destination.longitude + longitudeOffset;
        Line segmentLine = new Line(xOrigin, yOrigin, xDest, yDest);
        segmentLine.setStroke(color);
        segmentLine.setStrokeWidth(strokeWidth);
        this.pane.getChildren().add(segmentLine);
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
