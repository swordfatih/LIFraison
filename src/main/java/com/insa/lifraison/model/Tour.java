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
     * list of {@link com.insa.lifraison.model.TourSegment} of the optimal tour after it is computed
     */
    private List<TourSegment> tourSegments;

    public Tour() {
        deliveries = new ArrayList<>();
        tourSegments = new LinkedList<>();
    }

    public void addDelivery(DeliveryRequest deliveryRequest) {
        deliveries.add(deliveryRequest);
    }

    public boolean removeDelivery(DeliveryRequest deliveryRequest) {
        return deliveries.remove(deliveryRequest);
    }
}
