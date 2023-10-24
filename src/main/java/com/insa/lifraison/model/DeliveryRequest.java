package com.insa.lifraison.model;

import java.time.LocalTime;

/**
 * Object that includes an {@link com.insa.lifraison.model.Intersection} to deliver.
 */
public class DeliveryRequest {
    /**
     * The final arrival time of this delivery after considering all the deliveries from the same courier.
     */
    private LocalTime arrivalTime;
    /**
     * Point to which this delivery must be made
     */
    private Intersection destination;

    public DeliveryRequest(LocalTime arrivalTime, Intersection destination) {
        this.arrivalTime = arrivalTime;
        this.destination = destination;
    }

    public DeliveryRequest(Intersection destination) {
        this.destination = destination;

    }
}