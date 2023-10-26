package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;

public class AddDeliveryState2 implements State {
    private DeliveryRequest currentDelivery;

    /**
     * Change the position of the delivery request
     * @param c The controller
     * @param m the city map
     * @param i the intersection the user have just clicked
     */
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i){
        currentDelivery.updateDesination(i);
    }

    /**
     * cancel the action
     * @param c the Controller
     * @param m the City map
     */
    @Override
    public void rightClick(Controller c, CityMap m){
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }

    @Override
    public void confirm(Controller c, CityMap m){
        m.addDelivery(currentDelivery);
        c.setCurrentState(c.loadedDeliveryState);
    }

    protected void createDelivery(Intersection i){
        currentDelivery = new DeliveryRequest(i);
    }
}
