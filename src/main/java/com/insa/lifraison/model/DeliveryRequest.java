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

    /**
     * Change the start of the time window of the delivery request.
     * @param timeWindowStart The start of the time window.
     */
    public void setTimeWindowStart(LocalTime timeWindowStart) {
        this.timeWindowStart = timeWindowStart;
        this.notifyObservers(NotifType.UPDATE);
    }

    /**
     * Change the end of the time window of the delivery request.
     * @param timeWindowEnd The end of the time window.
     */
    public void setTimeWindowEnd(LocalTime timeWindowEnd) {
        this.timeWindowEnd = timeWindowEnd;
        this.notifyObservers(NotifType.UPDATE);
    }

    /**
     * Returns the state of the path calculation of the delivery.
     * @return The state of the path calculation of the delivery.
     */
    public DeliveryState getState() {
        return state;
    }

    /**
     * Set the state of the path calculation of the delivery.
     */
    public void setState(DeliveryState state) {
        this.state = state;
        notifyObservers(NotifType.UPDATE);
    }

    /**
     * Description of the delivery request
     * @return A string containing the description of the delivery request.
     */
    @Override
    public String toString() {
        return intersection.toString() + " entre " + timeWindowStart + " et " + timeWindowEnd;
    }
}
