package com.insa.lifraison.model;

/**
 * An object that models a point in the map with its latitude and longitude
 */
public class Intersection {
    /**
     * Unique identifier of the intersection
     */
    protected final String id;
    protected final double longitude;
    protected final double latitude;

    public Intersection(String id, double longitude, double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }
}