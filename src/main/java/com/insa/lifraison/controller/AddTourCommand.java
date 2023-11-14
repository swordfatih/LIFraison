package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class AddTourCommand implements Command {
    private CityMap cityMap;
    private Tour tour;

    /**
     * Create the command which adds a Tour to a CityMap
     * @param cityMap the map to which tour is added
     */
    public AddTourCommand(CityMap cityMap, Tour tour) {
        this.cityMap = cityMap;
        this.tour = tour;
    }

    @Override
    public void doCommand() {
        this.cityMap.addTour(tour);
    }

    @Override
    public void undoCommand() {
        this.cityMap.removeTour(tour);
    }
}
