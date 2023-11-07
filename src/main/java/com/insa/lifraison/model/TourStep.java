package com.insa.lifraison.model;

import java.time.LocalTime;
import java.util.LinkedList;

/**
 * Object that stores the path between two {@link com.insa.lifraison.model.DeliveryRequest}
 * as well as the departure and arrival time.
 */
public class TourStep {
    public final LinkedList<Segment> segments;
    public final LocalTime departure;
    public final LocalTime arrival;

    public TourStep(LinkedList<Segment> segments, LocalTime departure, LocalTime arrival) {
        this.segments = segments;
        this.departure = departure;
        this.arrival = arrival;
    }

    public double getLength() {
        double length = 0;
        for(Segment segment : this.segments) {
            length += segment.length;
        }
        return length;
    }

}
