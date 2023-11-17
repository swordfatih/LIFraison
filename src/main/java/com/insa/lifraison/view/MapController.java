package com.insa.lifraison.view;

import com.insa.lifraison.model.*;
import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.observer.Observer;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

import java.util.LinkedList;
import java.util.Objects;

import static java.lang.Math.atan2;
import static java.lang.Math.min;

/**
 * Class MainController extends from {@link com.insa.lifraison.view.ViewController}
 * It manage the main app window. It's based on the main.fxml file
 */
public class MapController extends ViewController implements Observer {
    /**
     * {@link javafx.scene.layout.Pane}
     */
    @FXML
    private Pane pane;
    /**
     * {@link javafx.scene.control.ScrollPane}
     */
    @FXML
    private ScrollPane scrollPane;
    /**
     * {@link com.insa.lifraison.model.CityMap}
     */
    private CityMap map;
    /**
     * {@link com.insa.lifraison.model.Tour}
     */
    private LinkedList<Tour> tours;
    /**
     * {@link java.lang.Number}
     */
    private Number mapWidth, mapHeight;
    private double scale;
    private double longitudeOffset;
    private double latitudeOffset;

    private final double zoomFactor = 1.2;
    private final double deliveryPointSize = 6;
    private final double intersectionPointSize = 3;

    /**
     * initialize the home window
     * This methods is automatically called at the creation of the class
     */
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
     * @param notifType the type of notification
     * @param observable the observable which has notified the observer
     * @param arg optional information about the update
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
                    circleDelivery.updateStroke();
                    if(deliveryRequest.isSelected() || circleDelivery.getTour() == null) {
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
                else if(arg instanceof DeliveryRequest newDelivery){
                    newDelivery.addObserver(this);
                    if (observable instanceof Tour tour) {
                        drawDeliveryPoint(newDelivery, tour, tour.getColor());
                    } else {
                        drawDeliveryPoint(newDelivery, null, map.getSelectionColor());
                    }
                }
            }
            case REMOVE -> {
                if(arg instanceof Tour){
                    eraseTour((Tour) arg);
                }else if(arg instanceof DeliveryRequest newDelivery){
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

    /**
     *
     * @param tour
     */
    public void drawTour(Tour tour){
        //initialisation of the path
        PathTour path = new PathTour(tour);
        path.getProperties().put("tour", tour);
        path.setId("Tour" + tour.getId());
        path.setStrokeWidth(5);
        path.setMouseTransparent(true);
        if(tour.isSelected()) {
            path.setStroke(map.getSelectionColor());
        } else {
            path.setStroke(tour.getColor());
        }

        //construction of th path
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

        //Addition to the pane
        this.pane.getChildren().add(path);

        //Addition of the direction to the pane
        for (TourStep tourStep : tour.getTourSteps()) {
            for (Segment segment : tourStep.segments) {
                Polygon direction = drawSegmentDirection(segment, tour.getColor().darker().darker(), 5, 10);
                direction.getProperties().put("tour", tour);
                direction.setMouseTransparent(true);
            }
        }

        //Addition of the deliveries to the pane
        for (DeliveryRequest delivery : tour.getDeliveries()) {
            if(tour.isSelected()) {
                drawDeliveryPoint(delivery, tour, map.getSelectionColor());
            } else {
                drawDeliveryPoint(delivery, tour, tour.getColor());
            }
        }
    }

    /**
     * Remove the visual of a tour
     * @param tour the tour to erase
     */
    private void eraseTour(Tour tour) {
        this.pane.getChildren().removeIf(node -> {
            Tour t = (Tour) node.getProperties().get("tour");
            return t != null && t.equals(tour);
        });

        for(DeliveryRequest deliveryRequest : tour.getDeliveries()) {
            eraseDeliveryRequest(deliveryRequest);
        }
    }

    /**
     * clear the map view and redraw everything
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

    /**
     * convert the intersection coordinate to screen coordinate
     * return a LineTo for the path
     * @param i an intersection {@link com.insa.lifraison.model.Intersection}
     * @return the corresponding LineTo {@link javafx.scene.shape.LineTo}
     */
    private LineTo getLineTo(Intersection i){
        double yCoordinate = -scale * i.latitude + latitudeOffset;
        double xCoordinate = scale * i.longitude + longitudeOffset;
        return new LineTo(xCoordinate, yCoordinate);
    }

    /**
     * convert the intersection coordinate to screen coordinate
     * return a MoveTo for the path
     * @param i an intersection {@link com.insa.lifraison.model.Intersection}
     * @return the corresponding LineTo {@link javafx.scene.shape.MoveTo}
     */
    private MoveTo getmoveTo(Intersection i){
        double yCoordinate = -scale * i.latitude + latitudeOffset;
        double xCoordinate = scale * i.longitude + longitudeOffset;
        return new MoveTo(xCoordinate, yCoordinate);
    }

    /**
     * Highlight an intersection of the delivery
     * @param intersection The Intersection to draw
     * @param radius the radius size of the circle
     * @param color the color of the circle
     */
    public void drawIntersectionPoint(Intersection intersection, double radius, Color color){
        double yCoordinate = -scale * intersection.latitude + latitudeOffset;
        double xCoordinate = scale * intersection.longitude + longitudeOffset;
        CircleIntersection circleIntersection = new CircleIntersection(xCoordinate, yCoordinate, radius, color, intersection);
        circleIntersection.setOnMouseClicked(this::onIntersectionClick);

        this.pane.getChildren().add(circleIntersection);
    }

    /**
     * show the delivery request on the map with a colored dot
     * @param deliveryRequest the deliveryRequest {@link com.insa.lifraison.model.DeliveryRequest}
     * @param tour the tour containing the delivery request {@link com.insa.lifraison.model.Tour}
     * @param color the color {@link javafx.scene.paint.Color}
     */
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

    /**
     * remove the dot of the delivery from the graphical view
     * @param deliveryRequest the delivery request to remove {@link com.insa.lifraison.model.DeliveryRequest}
     */
    public void eraseDeliveryRequest(DeliveryRequest  deliveryRequest){
        CircleDelivery foundCircle = findCircleDelivery(deliveryRequest);
        if(foundCircle != null ){
            this.pane.getChildren().remove(foundCircle);
        }
    }

    /**
     * get the CircleDelivery corresponding to the DeliveryRequest
     * @param deliveryRequest the delivery Request we are looking for
     * @return the CircleDelivery if it exists or null
     */
    private CircleDelivery findCircleDelivery(DeliveryRequest deliveryRequest) {
        for (javafx.scene.Node node : this.pane.getChildren()) {
            if (node instanceof CircleDelivery && Objects.equals(((CircleDelivery) node).getDeliveryRequest(),deliveryRequest)) {
                return (CircleDelivery) node;
            }
        }
        return null; // Circle with the specified id not found
    }

    /**
     * Called after a click on a CircleIntersection
     * notify the controller
     * @param event the input event
     */
    private void onIntersectionClick(MouseEvent event){
        if(event.getButton() == MouseButton.PRIMARY) {
            CircleIntersection clicked = (CircleIntersection) event.getSource();
            controller.leftClick(clicked.getIntersection(), null, null);
        }
    }

    /**
     * Called after a click on a CircleDelivery
     * notify the controller
     * @param event the input event
     */
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
     * @param color The color
     * @param strokeWidth the strokeWidth
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

    /**
     * Highlight the direction of a segment
     * @param segment The segment to highlight
     * @param color The color
     * @param baseWidth the baseWidth
     * @param height the height
     */
    public Polygon drawSegmentDirection(Segment segment, Color color, double baseWidth, double height) {
        double yOrigin = -scale * segment.origin.latitude + latitudeOffset;
        double xOrigin = scale * segment.origin.longitude + longitudeOffset;
        double yDest = -scale * segment.destination.latitude + latitudeOffset;
        double xDest = scale * segment.destination.longitude + longitudeOffset;

        Polygon direction = new Polygon(xOrigin + height, yOrigin, xOrigin, yOrigin - baseWidth, xOrigin, yOrigin + baseWidth);
        direction.setFill(color);

        double angle = (atan2(xDest - xOrigin, yDest - yOrigin) * (-180 / Math.PI) + 360) % 360 + 90;
        direction.getTransforms().add(new Rotate(angle, xOrigin, yOrigin));

        this.pane.getChildren().add(direction);

        return direction;
    }

    /**
     * modify the zoom base on the value of zoomFactor
     */
    public void zoomIn(ScrollEvent event) {
        Point2D cursorPoint = getMousePointInView(event);
        double zoomFactor = 1.2;

        pane.setScaleX(pane.getScaleX() * zoomFactor);
        pane.setScaleY(pane.getScaleY() * zoomFactor);

        recenter(cursorPoint, zoomFactor);
    }

    /**
     * modify the zoom base on the value of zoomFactor
     */
    public void zoomOut(ScrollEvent event) {
        if (pane.getScaleX() > 1) {
            Point2D cursorPoint = getMousePointInView(event);

            pane.setScaleX(pane.getScaleX() / zoomFactor);
            pane.setScaleY(pane.getScaleY() / zoomFactor);

            recenter(cursorPoint, zoomFactor);
        }
    }

    /**
     * called when the mouseWheel is moved on the map Pane
     * @param event the input event
     */
    private void onScrollEvent(ScrollEvent event){
        if (event.getDeltaY() > 0) {
            zoomIn(event);
        } else {
            zoomOut(event);
        }
        event.consume();
    }

    private void recenter(Point2D cursorPoint, double zoomFactor) {
        double hValue = this.scrollPane.getHvalue();
        double vValue = this.scrollPane.getVvalue();

        double newWidth = this.pane.getWidth() * zoomFactor;
        double newHeight = this.pane.getHeight() * zoomFactor;

        double deltaX = cursorPoint.getX() - (hValue * (newWidth - this.scrollPane.getViewportBounds().getWidth()));
        double deltaY = cursorPoint.getY() - (vValue * (newHeight - this.scrollPane.getViewportBounds().getHeight()));

        this.scrollPane.setHvalue(deltaX / newWidth);
        this.scrollPane.setVvalue(deltaY / newHeight);
    }

    private Point2D getMousePointInView(ScrollEvent event) {
        // Assuming mapScrollPane is the viewport for your mapPane
        double mouseX = event.getX();
        double mouseY = event.getY();

        double centerX = this.scrollPane.getHvalue() * (this.pane.getWidth() - this.scrollPane.getViewportBounds().getWidth());
        double centerY = this.scrollPane.getVvalue() * (this.pane.getHeight() - this.scrollPane.getViewportBounds().getHeight());

        double relativeMouseX = mouseX + centerX;
        double relativeMouseY = mouseY + centerY;

        return new Point2D(relativeMouseX, relativeMouseY);
    }
}
