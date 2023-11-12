package com.insa.lifraison.view;

import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircleIntersection extends Circle {
    private final Intersection intersection;

    CircleIntersection(double xCoordinate, double yCoordinate, double radius, Color color, Intersection intersection) {
        super(xCoordinate, yCoordinate, radius, color);
        this.intersection = intersection;
    }

    public Intersection getIntersection() {
        return intersection;
    }
}
