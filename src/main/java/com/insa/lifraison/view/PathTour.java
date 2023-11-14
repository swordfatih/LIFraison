package com.insa.lifraison.view;

import com.insa.lifraison.model.Tour;
import javafx.scene.shape.Path;

public class PathTour extends Path {

    private final Tour tour;

    public PathTour(Tour tour) {
        super();
        this.tour = tour;
    }

    public Tour getTour() {
        return tour;
    }
}
