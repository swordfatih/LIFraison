package com.insa.lifraison.view;

import com.insa.lifraison.model.Tour;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Class ButtonTour extends from {@link javafx.scene.control.Button}
 * Create a button for a specific tour of the model
 */
public class ButtonTour extends Button {
    /**
     * {@link com.insa.lifraison.model.Tour}
     */
    private final Tour tour;

    /**
     * Constructor of ButtonTour
     * @param tour the tour associated
     * @param color the color of the tour
     */
    ButtonTour(Tour tour, Paint color) {
        super();
        this.tour = tour;
        setText("Tour " + tour.getId());
        setTextFill(color);
    }

    /**
     * Give the tour associated with the button
     * @return tour
     */
    public Tour getTour() {
        return tour;
    }
}
