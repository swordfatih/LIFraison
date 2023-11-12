package com.insa.lifraison.view;

import com.insa.lifraison.model.Tour;
import javafx.scene.control.Button;

public class ButtonTour extends Button {
    private final Tour tour;

    ButtonTour(Tour tour) {
        super();
        this.tour = tour;
        setText("Tour " + tour.getId());
        setTextFill(tour.getColor());
    }

    public Tour getTour() {
        return tour;
    }
}
