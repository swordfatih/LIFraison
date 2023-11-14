package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.view.View;
import javafx.scene.layout.VBox;

public interface State {
    public default void loadMap(Controller c, View view, ListOfCommands l){};

    public default void changeMap(Controller c, View view){};

    public default void loadDeliveries(Controller c, CityMap m, View view, ListOfCommands l){};

    public default void addDelivery(Controller c, CityMap m, View view){};

    public default void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, Tour t,  ListOfCommands l){};

    public default void rightClick(Controller c, CityMap m, View view, ListOfCommands l){};

    public default void confirm(Controller c, CityMap m, View view, ListOfCommands l){};

    public default void cancel(Controller c, View view){};

    public default void deleteDelivery(Controller c, CityMap m, View view){};

    public default void computePlan(CityMap m, ListOfCommands l){};

    public default void undo(Controller c, CityMap m, ListOfCommands l){};

    public default void redo(Controller c, CityMap m, ListOfCommands l){};

    public default void save(CityMap m, View view){};

    public default void addTour(CityMap m, ListOfCommands l){};

    public default void removeTour(Controller c, CityMap m){};

    public default void tourButtonClicked(Controller c, CityMap m, Tour t, View v, ListOfCommands l){};

}
