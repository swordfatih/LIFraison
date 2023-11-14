package com.insa.lifraison.model;

import com.insa.lifraison.observer.Observable;

import java.time.LocalTime;

/**
 * Class that includes an {@link com.insa.lifraison.model.Intersection} to deliver.
 * It extends form {@link com.insa.lifraison.observer.Observable}
 */
public class DeliveryRequest extends Observable {
    /**
     * The final arrival time of this delivery after considering all the deliveries from the same courier.
     */
    private LocalTime timeWindowStart;
    private LocalTime timeWindowEnd;
    private DeliveryState state;

    /**
     * Intersection to which this delivery must be made {@link com.insa.lifraison.model.Intersection}
     */
    private Intersection intersection;

    /**
     * Constructor of the DeliveryRequest
     * @param intersection the intersection on which the deliveryRequest is created
     */
    public DeliveryRequest(Intersection intersection) {
        this.timeWindowStart = null;
        this.timeWindowEnd = null;
        this.intersection = intersection;
        this.state = DeliveryState.NotCalculated;
    }

    /**
     * Constructor of the DeliveryRequest
     * @param timeWindowStart the beginning of the time window of the delivery
     * @param timeWindowEnd the end of the time window of the delivery
     * @param intersection the intersection on which the deliveryRequest is created
     */
    public DeliveryRequest(LocalTime timeWindowStart, LocalTime timeWindowEnd, Intersection intersection) {
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
        this.intersection = intersection;
        this.state = DeliveryState.NotCalculated;
    }

    /**
     * @return the timeWindowStart
     */
    public LocalTime getTimeWindowStart(){
        return timeWindowStart;
    }

    /**
     * @return the timeWindowEnd
     */
    public LocalTime getTimeWindowEnd(){
        return timeWindowEnd;
    }

    /**
     * @return the intersection on which is the delivery
     */
    public Intersection getIntersection(){
        return this.intersection;
    }

    /**
     * Set the time window of the delivery and notify {@link com.insa.lifraison.observer.Observer}
     * @param timeWindowStart the beginning of the time window of the delivery
     * @param timeWindowEnd the end of the time window of the delivery
     */
    public void setTimeWindow(LocalTime timeWindowStart, LocalTime timeWindowEnd) {
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
        this.notifyObservers(NotifType.UPDATE);
    }

    /**
     * Set the intersection of the delivery and notify {@link com.insa.lifraison.observer.Observer}
     * @param intersection the new intersection on which the delivery is made
     */
    public void setIntersection(Intersection intersection) {
        this.intersection = intersection;
        this.notifyObservers(NotifType.UPDATE);
    }

    public void setTimeWindowStart(LocalTime timeWindowStart) {
        this.timeWindowStart = timeWindowStart;
        this.notifyObservers(NotifType.UPDATE);
    }

    public void setTimeWindowEnd(LocalTime timeWindowEnd) {
        this.timeWindowEnd = timeWindowEnd;
        this.notifyObservers(NotifType.UPDATE);
    }

    public DeliveryState getState() {
        return state;
    }

    public void setState(DeliveryState state) {
        this.state = state;
        notifyObservers(NotifType.UPDATE);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DeliveryRequest delreq){
            return this.intersection.equals(delreq.intersection) &&
                    this.timeWindowEnd == delreq.timeWindowEnd &&
                    this.timeWindowStart == delreq.timeWindowStart;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return intersection.toString() + " entre " + timeWindowStart + " et " + timeWindowEnd;
    }
}
