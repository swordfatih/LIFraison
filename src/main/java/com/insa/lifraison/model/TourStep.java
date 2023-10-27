package com.insa.lifraison.model;

import java.time.LocalTime;
import java.util.LinkedList;

/**
 * Object that stores the path between two {@link com.insa.lifraison.model.DeliveryRequest}
 * as well as the departure and arrival time.
 */
public class TourStep {
    private final LinkedList<Segment> segments;
    private final LocalTime departure;
    private final LocalTime arrival;

    public TourStep(LinkedList<Segment> segments, LocalTime departure, LocalTime arrival) {
        this.segments = segments;
        this.departure = departure;
        this.arrival = arrival;
    }

    public LinkedList<Segment> getSegments() {
        return segments;
    }

    public LocalTime getDeparture() {
        return departure;
    }

    public LocalTime getArrival() {
        return arrival;
    }
}
