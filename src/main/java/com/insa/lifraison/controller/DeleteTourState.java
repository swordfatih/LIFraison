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
            c.setCurrentStateToMain();
        } else {
            l.add(new ReverseCommand(new AddTourCommand(m, t)));
            if(m.getTours().size() == 1 && m.getNumberDeliveries() == 0){
                c.setCurrentState(c.emptyMapMainState);
            }
        }

    }

    @Override
    public void rightClick(Controller c, CityMap m, View view, ListOfCommands l){
        c.setCurrentStateToMain();
    }

}
