package com.insa.lifraison.model;

import com.insa.lifraison.observer.Observable;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Object that stores all the deliveries for a courier.
 * This class extends from {@link com.insa.lifraison.observer.Observable}
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

    private int id;
    private Color color;

    /**
     * Constructor of Tour
     */
    public Tour() {
        id = -1;
        color = null;
        deliveries = new ArrayList<>();
        tourSteps = new LinkedList<>();
    }

    void setId(int id) {
        this.id = id;
    }

    void setColor(Color color) {
        this.color = color;
    }

    public boolean addDelivery(DeliveryRequest deliveryRequest) {
        boolean hasChanged = deliveries.add(deliveryRequest);
        if(hasChanged){
            notifyObservers(NotifType.ADD, deliveryRequest);
        }
        return hasChanged;
    }

    public boolean removeDelivery(DeliveryRequest deliveryRequest) {
        boolean hasChanged = deliveries.remove(deliveryRequest);
        if (hasChanged) notifyObservers(NotifType.REMOVE, deliveryRequest);
        return hasChanged;
    }
    public ArrayList<DeliveryRequest> getDeliveries(){
        return deliveries;
    }
    
    public void setTourSteps(LinkedList<TourStep> tourSteps) { 
        this.tourSteps = tourSteps;
        notifyObservers(NotifType.UPDATE);
    }

    public LinkedList<TourStep> getTourSteps() { 
        return tourSteps; 
    }

    public int getId() {
        return this.id;
    }

    public Paint getColor() {
        return  this.color;
    }
}
