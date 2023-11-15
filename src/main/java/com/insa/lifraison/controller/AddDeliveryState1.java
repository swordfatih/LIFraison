package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.View;

public class AddDeliveryState1 implements State {

    @Override
    public void entryAction(CityMap m, View view) {
        view.<MainController>getController("main").enableButton("#addTourButton");

        view.<MainController>getController("main").getInformationController().displayAddDelivery1Informations();
    }

    @Override
    public void exitAction(CityMap m, View view) {
        view.<MainController>getController("main").disableButton("#addTourButton");

        view.<MainController>getController("main").getInformationController().clearInformations();
    }
    /**
     * click on the intersection where you want to add a delivery request
     *
     * @param c the controller
     * @param m the cityMap
     * @param i the Intersection where the user clicks
     */
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, Tour t, ListOfCommands l){
        m.setTemporaryDelivery(new DeliveryRequest(i));
        c.setCurrentState(c.addDeliveryState2);
    }

    @Override
    public void addTour(Controller c, CityMap m, ListOfCommands l) {
        l.add(new AddTourCommand(m, new Tour()));
    }

    /**
     * cancel the action
     * @param c the Controller
     * @param m the city map
     */
    @Override
    public void rightClick(Controller c, CityMap m, View view, ListOfCommands l){
        c.setCurrentStateToMain();
    }
}