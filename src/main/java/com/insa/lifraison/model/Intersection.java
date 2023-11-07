package com.insa.lifraison.model;

import java.util.LinkedList;
import java.util.List;
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

    public Intersection(String id, double longitude, double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intersection that = (Intersection) o;
        return Double.compare(longitude, that.longitude) == 0 && Double.compare(latitude, that.latitude) == 0 && Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "id='" + id + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}