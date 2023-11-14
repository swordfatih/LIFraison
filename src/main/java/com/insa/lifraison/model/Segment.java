package com.insa.lifraison.model;

import java.util.Objects;

/**
 * A directed edge connecting two {@link com.insa.lifraison.model.Intersection}, with its length and a name
 */
public class Segment {
    public final Intersection origin;
    public final Intersection destination;
    public final double length;
    public final String name;

    public Segment(Intersection origin, Intersection destination, double length, String name) {
        this.origin = origin;
        this.destination = destination;
        this.length = length;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return Double.compare(length, segment.length) == 0 && Objects.equals(origin, segment.origin) && Objects.equals(destination, segment.destination) && Objects.equals(name, segment.name);
    }

    @Override
    public String toString() {
        return "Segment{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", length=" + length +
                ", name='" + name + '\'' +
                '}';
    }
}
