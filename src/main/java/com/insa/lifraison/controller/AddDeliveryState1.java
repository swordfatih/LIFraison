package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.View;

/**
 * AddDeliveryState1 implements a State {@link com.insa.lifraison.controller.State}
 * It corresponds to the state when the user had clicked on the "addDelivery" button
 * but hasn't created a deliveryRequest yet.
 */
public class AddDeliveryState1 implements State {

    /**
     * Create a delivery request at an intersection given by the user. Go to AddDeliveryState2
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
     * Cancel the action and go back to precedent state.
     * @param c the Controller
     * @param m the city map
     */
    @Override
    public void rightClick(Controller c, CityMap m, View view, ListOfCommands l){
        view.<MainController>getController("main").getInformationController().clearInformations();
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }
}