package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;

public class AddDeliveryState1 implements State {
    /**
     * click on the intersection where you want to add a delivery request
     * @param c the controller
     * @param m the cityMap
     * @param i the Intersection where the user clicks
     */
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i){
        c.addDeliveryState2.createDelivery(i);
        c.setCurrentState(c.addDeliveryState2);
    }

    /**
     * cancel the action
     * @param c the Controller
     * @param m the city map
     */
    @Override
    public void rightClick(Controller c, CityMap m){
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }
}