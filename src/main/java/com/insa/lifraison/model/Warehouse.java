package com.insa.lifraison.model;

import java.util.Objects;

/**
 * Object that corresponds to the intersection from which every tour starts and end.
 */
public class Warehouse{
    public final Intersection intersection;
    public Warehouse(Intersection intersection) {
        this.intersection = intersection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return Objects.equals(intersection, warehouse.intersection);
    }
}
