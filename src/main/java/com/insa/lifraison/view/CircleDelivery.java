package com.insa.lifraison.view;

import com.insa.lifraison.model.DeliveryRequest;
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
    }

    public DeliveryRequest getDeliveryRequest() {
        return deliveryRequest;
    }

    public Tour getTour() {
        return tour;
    }
}
