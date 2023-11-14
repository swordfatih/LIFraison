package com.insa.lifraison.model;

import java.util.Objects;

/**
 * A directed edge connecting two {@link com.insa.lifraison.model.Intersection}, with its length and a name
 */
public class Segment {
    /**
     * Final {@link com.insa.lifraison.model.Intersection} of the segment
     */
    public final Intersection origin;
    public final Intersection destination;
    public final double length;
    /**
     * name of the street segment
     */
    public final String name;

    /**
     * Constructor of the Segment
     * @param origin intersection origin of the segment
     * @param destination intersection destination of the segment
     * @param length double length of the segment
     * @param name string name of the segment
     */
    public Segment(Intersection origin, Intersection destination, double length, String name) {
        this.origin = origin;
        this.destination = destination;
        this.length = length;
        this.name = name;
    }

    /**
     * Compares two segments. It returns true if, and only if,
     * they have the same origin, destination and name
     *
     * @param o the object with which to compare.
     * @ return <code>true</code> this object is the same as the o argument.
     *          <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return Double.compare(length, segment.length) == 0 && Objects.equals(origin, segment.origin) && Objects.equals(destination, segment.destination) && Objects.equals(name, segment.name);
    }

    /**
     * @return origin, destination, length and name of the segment as a string
     */
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
