package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Tour;

public class AddDeliveryCommand implements Command {
    private Tour tour;
    private DeliveryRequest deliveryRequest;

    /**
     * Create the command which adds a DeliveryRequest to a CityMap
     * @param deliveryRequest the DeliveryRequest added to cityMap
     */
    public AddDeliveryCommand(Tour tour, DeliveryRequest deliveryRequest) {
        this.tour = tour;
        this.deliveryRequest = deliveryRequest;
    }

    @Override
    public void doCommand() {
        tour.addDelivery(this.deliveryRequest);
    }

    @Override
    public void undoCommand() {
        tour.removeDelivery(this.deliveryRequest);
    }
}
