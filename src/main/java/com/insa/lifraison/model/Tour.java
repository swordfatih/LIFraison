package com.insa.lifraison.model;

import com.insa.lifraison.observer.Observable;

import java.util.*;

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

    public boolean addDelivery(DeliveryRequest deliveryRequest) {
        return deliveries.add(deliveryRequest);
    }

    public boolean removeDelivery(DeliveryRequest deliveryRequest) {
        return deliveries.remove(deliveryRequest);
    }

    public ArrayList<DeliveryRequest> getDeliveries() {
        return deliveries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tour tour = (Tour) o;
        return Objects.equals(deliveries, tour.deliveries) && Objects.equals(tourSteps, tour.tourSteps);
    }
    
    public void setTourSteps(LinkedList<TourStep> tourSteps) { 
        this.tourSteps = tourSteps; 
    }

    public LinkedList<TourStep> getTourSteps() { 
        return tourSteps; 
    }
}
