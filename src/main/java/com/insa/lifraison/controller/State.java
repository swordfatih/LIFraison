package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.view.View;

import java.io.File;

public interface State {
    public default void loadMap(Controller c, CityMap m, View view){};

    public default void loadDeliveries(Controller c, CityMap m){};

    public default void addDelivery(Controller c){};

    public default void leftClick(Controller c, CityMap m, Intersection i){};

    public default void rightClick(Controller c, CityMap m){};

    public default void confirm(Controller c, CityMap m){};

    public default void deleteDelivery(Controller c){};

    public default void compute(Controller c){};

    public default void tourCompute(Controller c){};


}
