package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;

public class AddTourCommand implements Command {
    private CityMap cityMap;
    private Tour tour;

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
