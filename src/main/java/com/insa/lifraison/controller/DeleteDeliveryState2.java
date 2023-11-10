package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.view.MapController;
import com.insa.lifraison.view.View;

public class DeleteDeliveryState2 implements State {
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
    }**/

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
        view.<MapController>getController("map").informations.clearInformations();
        c.setCurrentState(c.loadedDeliveryState);
    }

    @Override
    public void confirm(Controller c, CityMap m, View view, ListOfCommands l){
        l.add(new ReverseCommand(new AddDeliveryCommand(m, m.getSelectedDelivery(), 0)));
        m.clearDeliverySelection();
        view.<MapController>getController("map").informations.clearInformations();
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
            System.out.println("retour a l'etat sans delivery");
        }
    }
}
