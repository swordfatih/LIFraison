package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.View;
import javafx.scene.layout.VBox;

public class DeleteTourState implements State{
    @Override
    public void tourButtonClicked(Controller c, CityMap m, Tour t, View v, ListOfCommands l) {
        l.add(new ReverseCommand(new AddTourCommand(m, t)));
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }
}
