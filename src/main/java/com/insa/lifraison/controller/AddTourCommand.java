package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;

/**
 * AddTourCommand is the class which does the specific command of adding a Tour
 * {@link com.insa.lifraison.model.Tour}
 * The class extends Command {@link com.insa.lifraison.controller.Command}
 */
public class AddTourCommand implements Command {
    private final CityMap cityMap;
    private final Tour tour;

    /**
     * Create the command which adds a Tour to a CityMap
     * @param cityMap the map to which tour is added
     * @param tour the tour
     */
    public AddTourCommand(CityMap cityMap, Tour tour) {
        this.cityMap = cityMap;
        this.tour = tour;
    }

    /**
     * Add the tour to the CityMap
     */
    @Override
    public void doCommand() {
        this.cityMap.addTour(tour);
    }

    /**
     * Remove the tour from the CityMap
     */
    @Override
    public void undoCommand() {
        this.cityMap.removeTour(tour);
    }
}
