package com.insa.lifraison.model;

import java.util.Objects;

/**
 * Object that corresponds to the intersection from which every tour starts and end.
 */
public class Warehouse{
    /**
     * Final {@link com.insa.lifraison.model.Intersection}
     */
    public final Intersection intersection;

    /**
     * Constructor of Warehouse
     * @param intersection where is located the warehouse
     */
    public Warehouse(Intersection intersection) {
        this.intersection = intersection;
    }

    /**
     * Compare the warehouse to another object
     * @param o the object which is compared to the warehouse
     * @ return <code>true</code> this object is the same as the o argument.
     *          <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return Objects.equals(intersection, warehouse.intersection);
    }
}
