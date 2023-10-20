package com.insa.lifraison.model;

/**
 * A directed edge connecting two {@link com.insa.lifraison.model.Intersection}, with its length and a name
 */
public class Segment {
    private final Intersection origin;
    private final Intersection destination;
    private final double length;
    private final String name;

    public Segment(Intersection origin, Intersection destination, double length, String name) {
        this.origin = origin;
        this.destination = destination;
        this.length = length;
        this.name = name;
    }

    public boolean equals(Object o) {
        if(!(o instanceof Segment s)) {
            return false;
        }

        return s.origin.equals(origin) &&
                s.destination.equals(destination) &&
                s.length == length &&
                s.name.equals(name);
    }
}
