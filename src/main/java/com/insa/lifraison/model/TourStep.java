package com.insa.lifraison.model;

import java.time.LocalTime;
import java.util.LinkedList;

/**
 * Object that stores the path between two {@link com.insa.lifraison.model.DeliveryRequest}
 * as well as the departure and arrival time.
 */
public class TourStep {
    /**
     * List of {@link com.insa.lifraison.model.Segment}
     */
    public final LinkedList<Segment> segments;

    public final LocalTime departure;
    public final LocalTime arrival;

    /**
     * Constructor of TourStep
     * @param segments a list of {@link com.insa.lifraison.model.Segment} between two deliveries
     * @param departure a localTime of departure
     * @param arrival a localTime of arrival
     */
    public TourStep(LinkedList<Segment> segments, LocalTime departure, LocalTime arrival) {
        this.segments = segments;
        this.departure = departure;
        this.arrival = arrival;
    }

    /**
     * Get the length of all segments of the TourStep.
     * @return double length of the TourStep
     */
    public double getLength() {
        double length = 0;
        for(Segment segment : this.segments) {
            length += segment.length;
        }
        return length;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TourStep trStp){
            return this.departure.equals(trStp.departure) &&
                    this.arrival.equals(trStp.arrival) &&
                    this.segments.containsAll(trStp.segments) &&
                    trStp.segments.equals(this.segments);
        } else {
            return false;
        }
    }

    public LocalTime getDeparture() {
        return departure;
    }

    public LocalTime getArrival() {
        return arrival;
    }
}
