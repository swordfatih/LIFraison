package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.MapController;
import com.insa.lifraison.view.View;

public class AddDeliveryState1 implements State {
    /**
     * click on the intersection where you want to add a delivery request
     *
     * @param c the controller
     * @param m the cityMap
     * @param i the Intersection where the user clicks
     */
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, Tour t, ListOfCommands l){
        c.addDeliveryState2.entryAction(i, m, l);
        c.setCurrentState(c.addDeliveryState2);
    }

    /**
     * cancel the action
     * @param c the Controller
     * @param m the city map
     */
    @Override
    public void rightClick(Controller c, CityMap m, View view, ListOfCommands l){
        view.<MapController>getController("map").informations.clearInformations();
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }
}