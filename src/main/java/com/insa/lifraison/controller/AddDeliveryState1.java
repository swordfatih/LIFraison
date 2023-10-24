package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;

public class AddDeliveryState1 implements State {
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i){
        DeliveryRequest deliveryRequest = new DeliveryRequest(i);
        c.setCurrentState();
    }
}
