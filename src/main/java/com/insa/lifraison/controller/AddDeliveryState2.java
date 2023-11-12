package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.MenuController;
import com.insa.lifraison.view.View;

public class AddDeliveryState2 implements State {
    private DeliveryRequest currentDelivery;

    /**
     * Change the position of the delivery request
     * @param c The controller
     * @param m the city map
     * @param i the intersection the user have just clicked
     */
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, ListOfCommands l){
        m.moveDelivery(currentDelivery, i);
    }

    /**
     * cancel the action
     * @param c the Controller
     * @param m the City map
     */
    @Override
    public void rightClick(Controller c, CityMap m, View view, ListOfCommands l){
        l.cancel();
        view.<MainController>getController("main").getInformationController().clearInformations();
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }

    @Override
    public void confirm(Controller c, CityMap m, View view, ListOfCommands l){
        m.clearDeliverySelection();
        m.notifyObservers(Observable.NotifType.LIGHT_UPDATE);
        view.<MainController>getController("main").getInformationController().clearInformations();
        c.setCurrentState(c.loadedDeliveryState);
    }

    protected void entryAction(Intersection i, CityMap m, ListOfCommands l){
        currentDelivery = new DeliveryRequest(i);
        m.selectDelivery(currentDelivery);
        l.add(new AddDeliveryCommand(m, currentDelivery));
    }
}
