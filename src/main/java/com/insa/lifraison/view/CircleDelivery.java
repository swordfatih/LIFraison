package com.insa.lifraison.view;

import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Tour;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
/**
 * Class CircleDelivery extends from {@link javafx.scene.shape.Circle}
 * Create a circle for a specific delivery
 */
public class CircleDelivery extends Circle {
    /**
     * {@link com.insa.lifraison.model.Tour}
     */
    private final Tour tour;
    /**
     * {@link com.insa.lifraison.model.DeliveryRequest}
     */
    private final DeliveryRequest deliveryRequest;

    /**
     * Constructor of CircleDelivery
     * @param xCoordinate xPosition in pixel on the map Pane
     * @param yCoordinate yPosition in pixel on the map Pane
     * @param radius radius size in pixel of the circle
     * @param color color of the circle
     * @param deliveryRequest the delivery request associated to the circle
     * @param tour the tour that contains this delivery
     */
    CircleDelivery(double xCoordinate, double yCoordinate, double radius, Paint color, DeliveryRequest deliveryRequest, Tour tour) {
        super(xCoordinate, yCoordinate, radius, color);
        this.deliveryRequest = deliveryRequest;
        this.tour = tour;
        updateStroke();
    }

    /**
     * modify the color stroke of the circle depending on his state
     */
    public void updateStroke() {
        switch (this.deliveryRequest.getState()) {
            case NotCalculated:
                setStrokeWidth(2);
                setStroke(Color.YELLOW);
                break;
            case Late :
                setStrokeWidth(2);
                setStroke(Color.PINK);
                break;
            case NotPossible:
                setStrokeWidth(2);
                setStroke(Color.RED);
                break;
            case Ok :
                setStrokeWidth(2);
                setStroke(Color.LIGHTGREEN);
                break;
        }
    }

    public DeliveryRequest getDeliveryRequest() {
        return deliveryRequest;
    }

    public Tour getTour() {
        return tour;
    }
}