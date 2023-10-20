package com.insa.lifraison.model;

/**
 * Object that corresponds to the intersection from which every tour starts and end.
 */
public class Warehouse{
    private Intersection intersection;
    public Warehouse(Intersection intersection) {
        this.intersection = intersection;
    }

    public Intersection getIntersection() {
        return intersection;
    }
}
