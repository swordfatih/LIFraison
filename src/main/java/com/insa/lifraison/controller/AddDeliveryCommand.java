package com.insa.lifraison.controller;

import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Tour;

/**
 * AddDeliveryCommand is the class which does the specific command of adding a DeliveryRequest
 * {@link com.insa.lifraison.model.DeliveryRequest}
 * The class extends Command {@link com.insa.lifraison.controller.Command}
 */
public class AddDeliveryCommand implements Command {
    /**
     * {@link com.insa.lifraison.model.Tour} of the delivery
     */
    private final Tour tour;
    private final DeliveryRequest deliveryRequest;

    /**
     * Create the command which adds a DeliveryRequest to a CityMap
     * @param tour the tour on which the deliveryRequest is added
     * @param deliveryRequest the DeliveryRequest added to cityMap
     */
    public AddDeliveryCommand(Tour tour, DeliveryRequest deliveryRequest) {
        this.tour = tour;
        this.deliveryRequest = deliveryRequest;
    }

    /**
     * Add the delivery to the tour
     */
    @Override
    public void doCommand() {
        tour.addDelivery(this.deliveryRequest);
    }

    /**
     * Remove the delivery of the tour
     */
    @Override
    public void undoCommand() {
        tour.removeDelivery(this.deliveryRequest);
    }
}
