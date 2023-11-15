package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.View;

public class DeleteTourState implements State{
    @Override
    public void tourButtonClicked(Controller c, CityMap m, Tour t, View v, ListOfCommands l) {
        if(m.getTours().size() == 1) {
            CompoundCommand command = new CompoundCommand();
            command.addCommand(new ReverseCommand(new AddTourCommand(m, t)));
            command.addCommand(new AddTourCommand(m, new Tour()));
            l.add(command);
            leavingAction(c, m);
        } else {
            l.add(new ReverseCommand(new AddTourCommand(m, t)));
            if(m.getTours().size() == 1 && m.getNumberDeliveries() == 0){
                c.setCurrentState(c.loadedMapState);
            }
        }

    }

    @Override
    public void rightClick(Controller c, CityMap m, View view, ListOfCommands l){
        leavingAction(c, m);
    }

    public void leavingAction(Controller c, CityMap m) {
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }
}
