package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.View;

public class AddDeliveryState2 implements State {
    private DeliveryRequest currentDelivery;

    /**
     * Change the position of the delivery request
     *
     * @param c The controller
     * @param m the city map
     * @param i the intersection the user have just clicked
     */
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, Tour t, ListOfCommands l){
        currentDelivery.setIntersection(i);
    }

    /**
     * cancel the action
     * @param c the Controller
     * @param m the City map
     */
    @Override
    public void rightClick(Controller c, CityMap m, View view, ListOfCommands l){
        m.clearTemporaryDelivery();
        view.<MainController>getController("main").getInformationController().clearInformations();
        c.setCurrentStateToMain();
    }

    @Override
    public void tourButtonClicked(Controller c, CityMap m, Tour t, View v, ListOfCommands l) {
        DeliveryRequest deliveryRequest = m.getTemporaryDelivery();
        m.clearTemporaryDelivery();
        l.add(new AddDeliveryCommand(t, deliveryRequest));
        v.<MainController>getController("main").getInformationController().clearInformations();
        c.setCurrentState(c.filledMapMainState);
    }

    protected void entryAction(Intersection i, CityMap m, ListOfCommands l){
        currentDelivery = new DeliveryRequest(i);
        m.setTemporaryDelivery(currentDelivery);
    }
}
