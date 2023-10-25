package com.insa.lifraison.utils;

import com.insa.lifraison.model.Segment;

public class Edge {
    private final int origin;
    private final int destination;
    private final double length;
    private final Segment segment;

    public Edge(int origin, int destination, double length, Segment segment) {
        this.origin = origin;
        this.destination = destination;
        this.length = length;
        this.segment = segment;
    }

    public int getOrigin() {
        return origin;
    }

    public int getDestination() {
        return destination;
    }

    public double getLength() {
        return length;
    }

    public Segment getSegment() {
        return segment;
    }
}