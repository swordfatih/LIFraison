package com.insa.lifraison.controller;

import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.MenuController;
import com.insa.lifraison.view.View;

public class DeleteDeliveryState1 implements State {
    /**
     * click on the Intersection you want to delete
     * @param c The controller
     * @param m the cityMap
     * @param i the Intersection
     */
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, ListOfCommands l){
        m.selectDelivery(d);
        c.setCurrentState(c.deleteDeliveryState2);
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
        c.setCurrentState(c.loadedDeliveryState);
    }

    @Override
    public void confirm(Controller c, CityMap m, View view, ListOfCommands l){
        view.<MainController>getController("main").getInformationController().clearInformations();
        c.setCurrentState(c.loadedDeliveryState);
    }
}
