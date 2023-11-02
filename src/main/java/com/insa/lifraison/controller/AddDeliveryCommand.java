package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;

public class AddDeliveryCommand implements Command {
    private CityMap cityMap;
    private DeliveryRequest deliveryRequest;

    public AddDeliveryCommand(CityMap cityMap, DeliveryRequest deliveryRequest) {
        this.cityMap = cityMap;
        this.deliveryRequest = deliveryRequest;
    }

    @Override
    public void doCommand() {
        cityMap.addDelivery(this.deliveryRequest);
    }

    @Override
    public void undoCommand() {
        cityMap.removeDelivery(this.deliveryRequest);
    }
}
