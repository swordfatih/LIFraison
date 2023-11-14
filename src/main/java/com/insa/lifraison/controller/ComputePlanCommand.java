package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.model.TourStep;
import com.insa.lifraison.observer.Observable;

import java.util.ArrayList;
import java.util.LinkedList;
/**
 * ComputePlanCommand is a class which permits to compute all tours of the CityMap
 * The class extends Command {@link com.insa.lifraison.controller.Command}
 */
public class ComputePlanCommand implements Command {
    private CityMap cityMap;
    private ArrayList<LinkedList<TourStep>> tours;

    /**
     * Create the command that compute the delivery plan of a cityMap
     * @param cityMap the cityMap which delivery plan is computed
     */
    public ComputePlanCommand(CityMap cityMap) {
        this.cityMap = cityMap;
        this.tours = new ArrayList<>();
    }

    @Override
    public void doCommand() {
        tours.clear();
        for(int i = 0; i < cityMap.getTours().size(); i++) {
            Tour tour = cityMap.getTours().get(i);
            tours.add(tour.getTourSteps());
            tour.setTourSteps(cityMap.computePath(tour));
        }
        cityMap.notifyObservers(Observable.NotifType.UPDATE);
    }

    @Override
    public void undoCommand() {
        for(int i = 0; i < tours.size(); i++) {
            cityMap.getTours().get(i).setTourSteps(tours.get(i));
        }
        cityMap.notifyObservers(Observable.NotifType.UPDATE);
    }
}
