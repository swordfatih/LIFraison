package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.View;

public class DeleteDeliveryState1 implements State {
    /**
     * click on the Intersection you want to delete
     *
     * @param c The controller
     * @param m the cityMap
     * @param i the Intersection
     */
    /**
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i, ListOfCommands l){
        m.selectDelivery(d);
        c.setCurrentState(c.deleteDeliveryState2);
    }**/

    @Override
    public void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, Tour tour, ListOfCommands l){
        if(d != null) {
            l.add(new ReverseCommand(new AddDeliveryCommand(tour, d)));
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
        view.<MainController>getController("main").getInformationController().clearInformations();
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }

    @Override
    public void confirm(Controller c, CityMap m, View view, ListOfCommands l){
        view.<MainController>getController("main").getInformationController().clearInformations();
        c.setCurrentState(c.loadedDeliveryState);
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }
}
