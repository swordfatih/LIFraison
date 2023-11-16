package com.insa.lifraison.view;

import com.insa.lifraison.model.Tour;
import javafx.scene.shape.Path;

/**
 * Class CircleDelivery extends from {@link javafx.scene.shape.Path}
 * Create a path for a specific tour
 */
public class PathTour extends Path {
    /**
     * {@link com.insa.lifraison.model.Tour}
     */
    private final Tour tour;

    /**
     * constructor : set the tour
     * @param tour the tour corresponding to the path
     */
    public PathTour(Tour tour) {
        super();
        this.tour = tour;
    }

    public Tour getTour() {
        return tour;
    }
}
