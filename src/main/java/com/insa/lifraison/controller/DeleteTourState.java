package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.view.View;
import javafx.scene.layout.VBox;

public class DeleteTourState implements State{
    @Override
    public void handleTourButton(Controller c, CityMap m, int index, View v, VBox container, ListOfCommands l) {
        l.add(new DeleteTourCommand(index, container));
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }
}
