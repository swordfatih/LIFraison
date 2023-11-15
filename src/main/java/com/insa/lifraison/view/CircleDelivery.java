package com.insa.lifraison.view;

import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.DeliveryState;
import com.insa.lifraison.model.Tour;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class CircleDelivery extends Circle {

    private final Tour tour;
    private final DeliveryRequest deliveryRequest;

    CircleDelivery(double xCoordinate, double yCoordinate, double radius, Paint color, DeliveryRequest deliveryRequest, Tour tour) {
        super(xCoordinate, yCoordinate, radius, color);
        this.deliveryRequest = deliveryRequest;
        this.tour = tour;
        updateStroke();
    }

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
