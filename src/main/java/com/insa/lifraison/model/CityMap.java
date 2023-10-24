package com.insa.lifraison.model;

import java.util.LinkedList;

/**
 * Object that stores all the intersections and segments in the city,
 * as well as the warehouse instance.
 */
public class CityMap {
    /**
     * List of all {@link com.insa.lifraison.model.Intersection} of the city
     */
    private LinkedList<Intersection> intersections;
    /**
     * List of all {@link com.insa.lifraison.model.Segment} of the city
     */
    private LinkedList<Segment> segments;
    /**
     * Unique {@link com.insa.lifraison.model.Warehouse} of the city
     */
    private Warehouse warehouse;

    private LinkedList<DeliveryRequest> uncomputedDeliveries;

    public CityMap(LinkedList<Intersection> intersections, LinkedList<Segment> segments, Warehouse warehouse) {
        this.intersections = intersections;
        this.segments = segments;
        this.warehouse = warehouse;
    }

    public void reset(){
        this.intersections = new LinkedList<>();
        this.segments = new LinkedList<>();
        this.warehouse = null;
    }

    /**
     * Compt the number of deliveries present on the map (in the list an the tours)
     * @return the number of deliveries
     */
    public int getNumberDeliveries(){
        ///TODO : implementer
        return 0;
    }

    /**
     * add a delivery to the uncomputedDeliveries list
     * @param newDelivery the delivery you want to add
     * @return succes of the adding
     */
    public boolean addDelivery(DeliveryRequest newDelivery){
        return uncomputedDeliveries.add(newDelivery);
    }

    /**
     * Add a new intersection to the city map.
     * @param intersection The instersction to be added to the CityMap.
     */
    public void addIntersection(Intersection intersection){
        intersections.push(intersection);
    }

    /**
     * Add a new segment to the city map.
     * @param segment The segment to be added to the CityMap.
     */
    public void addSegment(Segment segment){
        segments.push(segment);
    }

    /**
     * Set the warehouse of the CityMap.
     * @param warehouse
     */
    public void setWarehouse(Warehouse warehouse){
        this.warehouse = warehouse;
    }
    /**
     *
     * Compares two City maps. It returns true if, and only if,
     * they have the same intersections, segments, and the warehouses
     * are located on the same intersection.
     *
     * @param o the object with which to compare.
     * @ return <code>true</code> this object is the same as the o argument.
     *          <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityMap otherMap = (CityMap) o;
        boolean equalIntersections = intersections.containsAll(otherMap.intersections) && otherMap.intersections.containsAll(intersections);
        boolean equalSegments = segments.containsAll(otherMap.segments) && otherMap.segments.containsAll(segments);
        boolean equalWarehouses = warehouse.equals(otherMap.warehouse);
        return equalWarehouses && equalIntersections && equalSegments;
    }

}
