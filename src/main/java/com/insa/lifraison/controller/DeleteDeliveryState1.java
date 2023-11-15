package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.View;

public class DeleteDeliveryState1 implements State {

    @Override
    public void entryAction(CityMap m, View view) {
        view.<MainController>getController("main").getInformationController().displayDeleteDeliveryInformations();
    }

    @Override
    public void exitAction(CityMap m, View view) {
        view.<MainController>getController("main").getInformationController().clearInformations();
    }

    /**
     * click on the Intersection you want to delete
     *
     * @param c The controller
     * @param m the cityMap
     * @param i the Intersection
     */
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, Tour tour, ListOfCommands l){
        if(d != null) {
            l.add(new ReverseCommand(new AddDeliveryCommand(tour, d)));
            if(m.getNumberDeliveries() == 0) {
                c.setCurrentStateToMain();
            }
        }

    }

    /**
     * Cancel the action
     *
     * @param c    the controller
     * @param m    the cityMap
     * @param view
     */
    @Override
    public void rightClick(Controller c, CityMap m, View view, ListOfCommands l){
        c.setCurrentState(c.filledMapMainState);
    }

    @Override
    public void confirm(Controller c, CityMap m, View view, ListOfCommands l){
        c.setCurrentState(c.filledMapMainState);
    }
}
