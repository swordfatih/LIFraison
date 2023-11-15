package com.insa.lifraison.model;

import java.util.Objects;

/**
 * An object that models a point in the map with its latitude and longitude
 */
public class Intersection {
    /**
     * Unique identifier of the intersection
     */
    public final String id;

    public final double longitude;
    public final double latitude;

    /**
     * Constructor of Intersection
     * @param id distinct all intersections
     * @param longitude longitude of the intersection
     * @param latitude latitude of the intersection
     */
    public Intersection(String id, double longitude, double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @return id of the intersection
     */
    public String getId() {
        return id;
    }

    /**
     * @return longitude of the intersection
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return latitude of the intersection
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Compare the intersection to another object
     * @param o the object which is compared to the intersection
     * @ return <code>true</code> this object is the same as the o argument.
     *          <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intersection that = (Intersection) o;
        return Double.compare(longitude, that.longitude) == 0 && Double.compare(latitude, that.latitude) == 0 && Objects.equals(id, that.id);
    }

    /**
     * @return id, latitude and longitude information of the intersection in a string
     */
    @Override
    public String toString() {
        return "Intersection " +
                "[" + this.id + "] " +
                "{" + this.longitude + ", " + this.latitude + "}";
    }
}