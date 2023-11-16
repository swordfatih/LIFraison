package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.View;
/**
 * DeleteTourState implements a State {@link com.insa.lifraison.controller.State}
 * It corresponds to the state when the user can delete one or more tours
 */
public class DeleteTourState implements State{

    @Override
    public void entryAction(CityMap m, View view) {
        view.<MainController>getController("main").getInformationController().displayDeleteTourInformation();
    }

    @Override
    public void exitAction(CityMap m, View view) {
        view.<MainController>getController("main").getInformationController().clearInformation();
    }

    /**
     * Delete a tour from the CityMap
     * @param c the Controller
     * @param m the CityMap
     * @param t the Tour which is deleted
     * @param v the view
     * @param l the list of commands
     */
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

    /**
     * Return to the main state
     * @param c the Controller
     * @param m the CityMap
     * @param l the list of commands
     */
    @Override
    public void rightClick(Controller c, CityMap m, View view, ListOfCommands l){
        c.setCurrentStateToMain();
    }

    /**
     * Return to the main state
     * @param c the Controller
     * @param m the CityMap
     * @param l the list of commands
     */
    @Override
    public void confirm(Controller c, CityMap m, View view, ListOfCommands l) {
        c.setCurrentStateToMain();
    }

}
