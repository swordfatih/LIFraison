package com.insa.lifraison.view;

import com.insa.lifraison.model.Tour;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ButtonTour extends Button {
    private final Tour tour;

    ButtonTour(Tour tour, Paint color) {
        super();
        this.tour = tour;
        setText("Tour " + tour.getId());
        setTextFill(color);
    }

    public Tour getTour() {
        return tour;
    }
}
