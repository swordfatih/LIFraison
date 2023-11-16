package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.model.TourStep;
import com.insa.lifraison.observer.Observable;

import java.util.ArrayList;
import java.util.LinkedList;
/**
 * ComputePlanCommand is a class which permits to compute all tours of the CityMap
 * The class implements {@link com.insa.lifraison.controller.Command}
 */
public class ComputePlanCommand implements Command {
    private final CityMap cityMap;

    /**
     * Create the command that compute the delivery plan of a cityMap
     * @param cityMap the cityMap which delivery plan is computed
     */
    public ComputePlanCommand(CityMap cityMap) {
        this.cityMap = cityMap;
    }

    @Override
    public void doCommand() {
        cityMap.computePlan();
    }

    @Override
    public void undoCommand() {
        cityMap.clearPlan();
    }
}
