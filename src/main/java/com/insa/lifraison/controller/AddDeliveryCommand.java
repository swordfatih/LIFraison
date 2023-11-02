package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;

public class AddDeliveryCommand implements Command {
    private CityMap cityMap;
    private DeliveryRequest deliveryRequest;

    /**
     * Create the command which adds a DeliveryRequest to a CityMap
     * @param cityMap the map to which deliveryRequest is added
     * @param deliveryRequest the DeliveryRequest added to cityMap
     */
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
