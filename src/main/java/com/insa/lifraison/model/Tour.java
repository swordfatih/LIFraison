package com.insa.lifraison.model;

import com.insa.lifraison.observer.Observable;
import javafx.scene.paint.Color;

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

    /**
     * Set id of a tour
     * @param id the id
     */
    void setId(int id) {
        this.id = id;
    }

    /**
     * Set color of a tour
     * @param color the color
     */
    void setColor(Color color) {
        this.color = color;
    }

    /**
     * Add a {@link com.insa.lifraison.model.DeliveryRequest} to a tour.
     * If the delivery is added notify {@link com.insa.lifraison.observer.Observer}
     * @param deliveryRequest the delivery added to the tour
     * @return <code>true</code> the delivery was added successfully
     *         <code>false</code> otherwise.
     */
    public boolean addDelivery(DeliveryRequest deliveryRequest) {
        boolean hasChanged = deliveries.add(deliveryRequest);
        if(hasChanged){
            notifyObservers(NotifType.ADD, deliveryRequest);
        }
        return hasChanged;
    }

    /**
     * Remove a {@link com.insa.lifraison.model.DeliveryRequest} to a tour.
     * If the delivery is removed notify {@link com.insa.lifraison.observer.Observer}
     * @param deliveryRequest the delivery removed to the tour
     * @return <code>true</code> the delivery was removed successfully
     *         <code>false</code> otherwise.
     */
    public boolean removeDelivery(DeliveryRequest deliveryRequest) {
        boolean hasChanged = deliveries.remove(deliveryRequest);
        if (hasChanged) notifyObservers(NotifType.REMOVE, deliveryRequest);
        return hasChanged;
    }

    /**
     * Give all deliveries of a tour
     * @return ArrayList<DeliveryRequest> deliveries
     */
    public ArrayList<DeliveryRequest> getDeliveries(){
        return deliveries;
    }

    /**
     * Set a list of {@link com.insa.lifraison.model.TourStep} to a tour.
     * Notify  {@link com.insa.lifraison.observer.Observer}
     * @param tourSteps the list of tourStep
     */
    public void setTourSteps(LinkedList<TourStep> tourSteps) { 
        this.tourSteps = tourSteps;
        notifyObservers(NotifType.UPDATE);
    }

    /**
     * @return  LinkedList<TourStep> tourSteps of the tour
     */
    public LinkedList<TourStep> getTourSteps() { 
        return tourSteps; 
    }

    /**
     * @return id of the tour
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return color of the tour
     */
    public Color getColor() {
        return  this.color;
    }

    public void clearPath() {
        this.tourSteps = new LinkedList<>();
        for(DeliveryRequest deliveryRequest : deliveries) {
            deliveryRequest.setState(DeliveryState.NotCalculated);
        }
        notifyObservers(NotifType.UPDATE);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Tour tr){
            boolean b1 = (this.getColor() == tr.getColor());
            boolean b2 = (this.getId() == tr.getId());
            boolean b3 = this.getDeliveries().containsAll(tr.getDeliveries()) && tr.getDeliveries().containsAll(this.getDeliveries());
            boolean b4 = this.getTourSteps().containsAll(tr.getTourSteps()) && tr.getTourSteps().containsAll(this.getTourSteps());
            return b1 && b2 && b3 && b4;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder s;
        s = new StringBuilder("id : " + this.getId());
        s.append("color : ").append(this.getColor());
        for (DeliveryRequest del : this.deliveries){
            s.append("\n\t").append(del.toString());
        }
        return s.toString();
    }
}
