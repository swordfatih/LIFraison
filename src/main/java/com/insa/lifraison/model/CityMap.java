package com.insa.lifraison.model;

import java.util.LinkedList;

/**
 * Object that stores all the intersections and segments in the city,
 * as well as the warehouse instance.
 */
public class CityMap {
    /**
     * List of all {@link com.insa.lifraison.model.Intersection} of the city
     */
    private LinkedList<Intersection> intersections;
    /**
     * List of all {@link com.insa.lifraison.model.Segment} of the city
     */
    private LinkedList<Segment> segments;
    /**
     * Unique {@link com.insa.lifraison.model.Warehouse} of the city
     */
    private Warehouse warehouse;

    public CityMap(LinkedList<Intersection> intersections, LinkedList<Segment> segments, Warehouse warehouse) {
        this.intersections = intersections;
        this.segments = segments;
        this.warehouse = warehouse;
    }
}
