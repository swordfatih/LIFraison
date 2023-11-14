package com.insa.lifraison.model;

import com.insa.lifraison.observer.Observable;

import java.time.LocalTime;

/**
 * Object that includes an {@link com.insa.lifraison.model.Intersection} to deliver.
 */
public class DeliveryRequest extends Observable {
    /**
     * The final arrival time of this delivery after considering all the deliveries from the same courier.
     */
    private LocalTime timeWindowStart;
    private LocalTime timeWindowEnd;

    /**
     * Point to which this delivery must be made
     */
    private Intersection intersection;

    public DeliveryRequest(Intersection intersection) {
        this.timeWindowStart = null;
        this.timeWindowEnd = null;
        this.intersection = intersection;
    }

    public DeliveryRequest(LocalTime timeWindowStart, LocalTime timeWindowEnd, Intersection intersection) {
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
        this.intersection = intersection;
    }

    public LocalTime getTimeWindowStart(){
        return timeWindowStart;
    }
    public LocalTime getTimeWindowEnd(){
        return timeWindowEnd;
    }

    public Intersection getIntersection(){
        return this.intersection;
    }

    public void setTimeWindow(LocalTime timeWindowStart, LocalTime timeWindowEnd) {
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
        this.notifyObservers(NotifType.UPDATE);
    }

    public void setIntersection(Intersection intersection) {
        this.intersection = intersection;
        this.notifyObservers(NotifType.UPDATE);
    }
}
