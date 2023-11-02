package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.view.View;

public interface State {
    public default void loadMap(Controller c, CityMap m, View view, ListOfCommands l){};

    public default void loadDeliveries(Controller c, CityMap m, View view, ListOfCommands l){};

    public default void addDelivery(Controller c, View view){};

    public default void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, ListOfCommands l){};

    public default void rightClick(Controller c, CityMap m, View view, ListOfCommands l){};

    public default void confirm(Controller c, CityMap m, View view, ListOfCommands l){};

    public default void deleteDelivery(Controller c, View view){};

    public default void computePlan(CityMap m, ListOfCommands l){};

    public default void undo(Controller c, CityMap m, ListOfCommands l){};

    public default void redo(Controller c, CityMap m, ListOfCommands l){};

}
