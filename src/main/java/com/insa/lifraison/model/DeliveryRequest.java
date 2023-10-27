package com.insa.lifraison.model;

import java.time.LocalTime;
import java.util.Objects;

/**
 * Object that includes an {@link com.insa.lifraison.model.Intersection} to deliver.
 */
public class DeliveryRequest {
    /**
     * The final arrival time of this delivery after considering all the deliveries from the same courier.
     */
    private LocalTime timeWindowStart;
    private LocalTime timeWindowEnd;

    /**
     * Point to which this delivery must be made
     */
    private Intersection destination;

    public DeliveryRequest(LocalTime timeWindowStart, LocalTime timeWindowEnd, Intersection destination) {
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
        this.destination = destination;
    }

    public DeliveryRequest(Intersection destination) {
        this.destination = destination;
    }

    public LocalTime getTimeWindowStart(){
        return timeWindowStart;
    }
    public LocalTime getTimeWindowEnd(){
        return timeWindowEnd;
    }

    public Intersection getDestination(){
        return this.destination;
    }

    public void setTimeWindowStart(LocalTime timeWindowStart) {
        this.timeWindowStart = timeWindowStart;
    }

    public void setTimeWindowEnd(LocalTime timeWindowEnd) {
        this.timeWindowEnd = timeWindowEnd;
    }

    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryRequest that = (DeliveryRequest) o;
        return Objects.equals(timeWindowStart, that.timeWindowStart) && Objects.equals(timeWindowEnd, that.timeWindowEnd) && Objects.equals(destination, that.destination);
    }
}
