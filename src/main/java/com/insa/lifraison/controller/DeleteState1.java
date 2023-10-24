package com.insa.lifraison.controller;

import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.CityMap;

public class DeleteState1 implements State {
    /**
     * click on the Intersection you want to delete
     * @param c The controller
     * @param m the cityMap
     * @param i the Intersection
     */
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i){
        boolean suppressionValid = m.removeDeliveryAt(i);
        if (suppressionValid){
            if (m.getNumberDeliveries() == 0){
                c.setCurrentState(c.loadedMapState);
            }
            // otherwise, we can still delete deliveries
        }
    }

    /**
     * Cancel the action
     * @param c the controller
     * @param m the cityMap
     */
    @Override
    public void rightClick(Controller c, CityMap m){
        c.setCurrentState(c.loadedDeliveryState);
    }
}
