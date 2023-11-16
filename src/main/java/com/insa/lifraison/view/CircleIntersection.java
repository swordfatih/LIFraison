package com.insa.lifraison.view;

import com.insa.lifraison.model.Intersection;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
/**
 * Class CircleIntersection extends from {@link javafx.scene.shape.Circle}
 * Create a circle for a specific intersection
 */
public class CircleIntersection extends Circle {
    /**
     * {@link com.insa.lifraison.model.Intersection}
     */
    private final Intersection intersection;

    /**
     * Constructor of CircleIntersection
     * @param xCoordinate xPosition in pixel on the map Pane
     * @param yCoordinate yPosition in pixel on the map Pane
     * @param radius radius size in pixel of the circle
     * @param color color of the circle
     * @param intersection the intersection associated to the circle
     */
    CircleIntersection(double xCoordinate, double yCoordinate, double radius, Color color, Intersection intersection) {
        super(xCoordinate, yCoordinate, radius, color);
        this.intersection = intersection;
    }

    public Intersection getIntersection() {
        return intersection;
    }
}
