package com.insa.lifraison.model;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

/**
 * Object that stores all the deliveries for a courier.
 * Provides a method that can calculate the optimal solution for the tour.
 */
public class Tour {
    /**
     * list of {@link DeliveryRequest} assigned to the courier
     */
    private ArrayList<DeliveryRequest> deliveries;

    /**
     * list of {@link TourStep} of the optimal tour after it is computed
     */
    private LinkedList<TourStep> tourSteps;

    public Tour() {
        deliveries = new ArrayList<>();
        tourSteps = new LinkedList<>();
    }

    public void addDelivery(DeliveryRequest deliveryRequest) {
        deliveries.add(deliveryRequest);
    }

    public boolean removeDelivery(DeliveryRequest deliveryRequest) {
        return deliveries.remove(deliveryRequest);
    }

    public ArrayList<DeliveryRequest> getDeliveries() {
        return deliveries;
    }

    public void setTourSteps(LinkedList<TourStep> tourSteps) { this.tourSteps = tourSteps; }

    public LinkedList<TourStep> getTourSteps() { return tourSteps; }
}
