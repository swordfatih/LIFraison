package com.insa.lifraison.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

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

    private double minLatitude, maxLatitude, minLongitude, maxLongitude;

    public CityMap() {
        reset();
    }

    public CityMap(LinkedList<Intersection> intersections, LinkedList<Segment> segments, Warehouse warehouse) {
        this.intersections = intersections;
        this.segments = segments;
        this.warehouse = warehouse;
        this.updateMinMax();
    }

    public void reset(){
        this.intersections = new LinkedList<>();
        this.segments = new LinkedList<>();
        this.warehouse = null;
        this.minLatitude = Double.MAX_VALUE;
        this.minLongitude = Double.MAX_VALUE;
        this.maxLatitude = Double.MIN_VALUE;
        this.maxLongitude = Double.MIN_VALUE;
    }

    /**
     * Sum the deliveries present in the city map and in tours
     * @return the sum
     */
    public int getNumberDeliveries(){
        ///TODO : implementer
        return 0;
    }

    /**
     *
     * @return iterator of segments
     */
    public Iterator<Segment> getSegmentsIterator() {
        return segments.iterator();
    }

    public LinkedList<Segment> getSegments() {
        return this.segments;
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
     * Withdraw the delivery at this position if it exists
     * @param i : the position where the delivery can be
     * @return true if a delivery was a this position and was successfully remove
     */
    public boolean removeDeliveryAt(Intersection i){
        ///TODO a implementer
        return false;
    }

    /**
     * Add a new intersection to the city map.
     * @param intersection The instersction to be added to the CityMap.
     */
    public void addIntersection(Intersection intersection){
        intersections.push(intersection);
        minLongitude = min(minLongitude, intersection.longitude);
        maxLongitude = max(maxLongitude, intersection.longitude);
        minLatitude = min(minLatitude, intersection.latitude);
        maxLatitude = max(maxLatitude, intersection.latitude);
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

    public void updateMinMax(){
        if(!this.intersections.isEmpty()){
            minLatitude = intersections.get(0).latitude;
            maxLatitude = intersections.get(0).latitude;
            minLongitude = intersections.get(0).longitude;
            maxLongitude = intersections.get(0).longitude;
            for(Intersection i : intersections ){
                minLatitude = min(minLatitude, i.latitude);
                maxLatitude = max(maxLatitude, i.latitude);

                minLongitude = min(minLongitude, i.longitude);
                maxLongitude = max(maxLongitude, i.longitude);
            }

        }
    }

    public double getMinLatitude(){
        return minLatitude;
    }

    public double getMaxLatitude(){
        return maxLatitude;
    }

    public double getMinLongitude(){
        return minLongitude;
    }

    public double getMaxLongitude(){
        return maxLongitude;
    }

}
