package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.View;

import java.time.LocalTime;

public class AddDeliveryState2 implements State {

    @Override
    public void entryAction(CityMap m, View view) {
        view.<MainController>getController("main").enableButton("#addTourButton");

        view.<MainController>getController("main").getInformationController().displayAddDelivery2Informations();
    }

    @Override
    public void exitAction(CityMap m, View view) {
        m.clearTemporaryDelivery();

        view.<MainController>getController("main").disableButton("#addTourButton");

        view.<MainController>getController("main").getInformationController().clearInformations();
    }

    /**
     * Change the position of the delivery request
     *
     * @param c The controller
     * @param m the city map
     * @param i the intersection the user have just clicked
     */
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, Tour t, ListOfCommands l){
        m.getTemporaryDelivery().setIntersection(i);
    }

    /**
     * cancel the action
     * @param c the Controller
     * @param m the City map
     */
    @Override
    public void rightClick(Controller c, CityMap m, View view, ListOfCommands l){
        c.setCurrentStateToMain();
    }

    @Override
    public void tourButtonClicked(Controller c, CityMap m, Tour t, View v, ListOfCommands l) {
        DeliveryRequest deliveryRequest = m.getTemporaryDelivery();
        m.clearTemporaryDelivery();
        l.add(new AddDeliveryCommand(t, deliveryRequest));
        System.out.println(deliveryRequest.getTimeWindowStart() + " to " +deliveryRequest.getTimeWindowEnd());
        c.setCurrentState(c.filledMapMainState);
    }

    @Override
    public void timeWindowChanged(CityMap m, LocalTime timeWindowStart, LocalTime timeWindowEnd) {
        m.getTemporaryDelivery().setTimeWindow(timeWindowStart, timeWindowEnd);
    }

    @Override
    public void addTour(Controller c, CityMap m, ListOfCommands l) {
        l.add(new AddTourCommand(m, new Tour()));
    }
}
