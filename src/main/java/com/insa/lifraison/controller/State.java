package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;

public interface State {
    public default void loadMap(Controller c, CityMap m){};

    public default void loadDeliveries(){};

    public default void addDelivery(){};

    public default void leftClick(){};

    public default void rightClick(){};

    public default void confirm(){};

    public default void cancel (){};

    public default void deleteDelivery(){};

    public default void compute(){};


}
