package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.View;

/**
 * AddDeliveryState2 implements a State {@link com.insa.lifraison.controller.State}
 * It corresponds to the state when the user had clicked on the "addDelivery" button
 * and has created a delivery request
 */
public class AddDeliveryState2 implements State {
    private DeliveryRequest currentDelivery;

    /**
     * Change the position of the delivery request
     * @param c The controller
     * @param m the city map
     * @param i the intersection the user have just clicked
     */
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, Tour t, ListOfCommands l){
        currentDelivery.setIntersection(i);
    }

    /**
     * Cancel the action
     * @param c the Controller
     * @param m the City map
     */
    @Override
    public void rightClick(Controller c, CityMap m, View view, ListOfCommands l){
        m.clearTemporaryDelivery();
        view.<MainController>getController("main").getInformationController().clearInformations();
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }

    /**
     * Add the delivery request to the chosen tour
     * @param c the Controller
     * @param m the CityMap
     * @param t the chosen tour
     * @param v the view
     * @param l the list of command
     */
    @Override
    public void tourButtonClicked(Controller c, CityMap m, Tour t, View v, ListOfCommands l) {
        DeliveryRequest deliveryRequest = m.getTemporaryDelivery();
        m.clearTemporaryDelivery();
        l.add(new AddDeliveryCommand(t, deliveryRequest));
        v.<MainController>getController("main").getInformationController().clearInformations();
        c.setCurrentState(c.loadedDeliveryState);
    }

    /**
     * Create a new delivery request which is temporary and isn't associated to a tour yet
     * @param i the intersection of the delivery request
     * @param m the CityMap
     * @param l the list of command
     */
    protected void entryAction(Intersection i, CityMap m, ListOfCommands l){
        currentDelivery = new DeliveryRequest(i);
        m.setTemporaryDelivery(currentDelivery);
    }
}
