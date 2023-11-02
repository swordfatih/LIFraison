package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.view.MapController;
import com.insa.lifraison.view.View;

public class DeleteDeliveryState2 implements State {
    /**
     * click on the Intersection you want to delete
     * @param c The controller
     * @param m the cityMap
     * @param i the Intersection
     */
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, ListOfCommands l){
        m.selectDelivery(d);
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
        m.clearDeliverySelection();
        view.<MapController>getController("map").clearInformations();
        c.setCurrentState(c.loadedDeliveryState);
    }

    @Override
    public void confirm(Controller c, CityMap m, View view, ListOfCommands l){
        l.add(new ReverseCommand(new AddDeliveryCommand(m, m.getSelectedDelivery())));
        m.clearDeliverySelection();
        view.<MapController>getController("map").clearInformations();
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
            System.out.println("retour a l'etat sans delivery");
        }
    }
}
