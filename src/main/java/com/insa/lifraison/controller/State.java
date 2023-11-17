package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.View;

import java.time.LocalTime;

/**
 * Interface of the states
 */
public interface State {

    /**
     * function launched when entering the state
     * @param m
     * @param view
     */
    default void entryAction(CityMap m, View view) {}

    /**
     * function launched when exiting the state
     * @param m
     * @param view
     */
    default void exitAction(CityMap m, View view) {}

    default void loadMap(Controller c, CityMap m, View view, ListOfCommands l){}

    default void loadDeliveries(Controller c, CityMap m, View view, ListOfCommands l){}

    default void addDelivery(Controller c, CityMap m, View view){}

    default void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, Tour t,  ListOfCommands l){}

    default void rightClick(Controller c, CityMap m, View view, ListOfCommands l){}

    default void confirm(Controller c, CityMap m, View view, ListOfCommands l){}

    default void cancel(Controller c, View view){}

    default void removeDelivery(Controller c, CityMap m, View view){}

    default void computePlan(CityMap m, ListOfCommands l){}

    default void undo(Controller c, CityMap m, ListOfCommands l){}

    default void redo(Controller c, CityMap m, ListOfCommands l){}

    default void saveDeliveries(CityMap m, View view){}

    default void saveRoadmap(Controller c){}

    default void addTour(Controller c, CityMap m, ListOfCommands l){}

    default void removeTour(Controller c, CityMap m){}

    default void tourButtonClicked(Controller c, CityMap m, Tour t, View v, ListOfCommands l){}

    default void timeWindowChanged(CityMap m, LocalTime timeWindowStart, LocalTime timeWindowEnd) {}
}
