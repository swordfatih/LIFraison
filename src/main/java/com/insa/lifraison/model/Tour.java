package com.insa.lifraison.model;

import com.insa.lifraison.observer.Observable;

import java.util.*;

/**
 * Object that stores all the deliveries for a courier.
 * Provides a method that can calculate the optimal solution for the tour.
 */
public class Tour extends Observable {
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

    public Iterator<DeliveryRequest> getDeliveriesIterator() {
        return deliveries.iterator();
    }

    public boolean addDelivery(DeliveryRequest deliveryRequest) {
        boolean change = deliveries.add(deliveryRequest);
        if(change) notifyObservers(NotifType.ADD, deliveryRequest);
        return change;
    }

    public boolean removeDelivery(DeliveryRequest deliveryRequest) {
        boolean change = deliveries.remove(deliveryRequest);
        if(change) notifyObservers(NotifType.REMOVE, deliveryRequest);
        return change;
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
