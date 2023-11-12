package com.insa.lifraison.model;

import com.insa.lifraison.observer.Observable;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Object that stores all the deliveries for a courier.
 * Provides a method that can calculate the optimal solution for the tour.
 */
public class Tour extends Observable{
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
        boolean hasChanged = deliveries.add(deliveryRequest);
        if(hasChanged){
            System.out.println("the delivery added is at : ("+deliveryRequest.getDestination().longitude+";"+deliveryRequest.getDestination().latitude+")");
            notifyObservers(NotifType.ADD, deliveryRequest);
        }
        return hasChanged;
    }

    public boolean removeDelivery(DeliveryRequest deliveryRequest) {
        boolean hasChanged = deliveries.remove((deliveryRequest));
        if (hasChanged) notifyObservers(NotifType.DELETE, deliveryRequest);
        return hasChanged;
    }
    public ArrayList<DeliveryRequest> getDeliveries(){
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
