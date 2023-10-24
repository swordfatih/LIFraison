package com.insa.lifraison.model;

import com.insa.lifraison.observer.Observable;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;
import com.insa.lifraison.utils.*;
import java.time.LocalTime;

/**
 * Object that stores all the intersections and segments in the city,
 * as well as the warehouse instance.
 */
public class CityMap extends Observable {
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

    private LinkedList<Tour> tours;

    private DeliveryRequest selectedDelivery;

    private double minLatitude, maxLatitude, minLongitude, maxLongitude;

    public CityMap() {
        reset();
    }

    public CityMap(LinkedList<Intersection> intersections, LinkedList<Segment> segments, Warehouse warehouse) {
        this.intersections = intersections;
        this.segments = segments;
        this.warehouse = warehouse;
        this.updateMinMax();
        this.tours = new LinkedList<>();
        Tour tour = new Tour();
        this.selectedDelivery = null;
        tours.add(tour);
    }

    public void reset(){
        this.intersections = new LinkedList<>();
        this.segments = new LinkedList<>();
        this.warehouse = null;
        this.minLatitude = Double.MAX_VALUE;
        this.minLongitude = Double.MAX_VALUE;
        this.maxLatitude = Double.MIN_VALUE;
        this.maxLongitude = Double.MIN_VALUE;
        this.tours = new LinkedList<>();
        Tour tour = new Tour();
        tours.add(tour);
        this.selectedDelivery = null;
        this.notifyObservers(NotifType.FULL_UPDATE);
    }

    /**
     * Sum the deliveries present in the city map and in tours
     * @return the sum
     */
    public int getNumberDeliveries(){
        int sum = 0;
        for (Tour tour : tours) {
            sum += tour.getDeliveries().size();
        }
        return sum;
    }

    public LinkedList<Tour> getTours() { return tours; }

    public LinkedList<Segment> getSegments() {
        return this.segments;
    }
    /**
     * add a delivery to the uncomputedDeliveries list
     * @param newDelivery the delivery you want to add
     * @return succes of the adding
     */
    public boolean addDelivery(DeliveryRequest newDelivery){
        boolean hasChanged = tours.get(0).addDelivery(newDelivery);
        if(hasChanged) notifyObservers(NotifType.LIGHT_UPDATE);
        return hasChanged;
    }
    public void addTour(Tour tour) {
        boolean hasChanged = this.tours.add(tour);
        if (hasChanged) notifyObservers(NotifType.LIGHT_UPDATE);
    }

    public void removeTour(Tour tour) {
        boolean hasChanged = this.tours.remove(tour);
        if (hasChanged) notifyObservers(NotifType.LIGHT_UPDATE);
    }
    public void moveDelivery(DeliveryRequest delivery, Intersection newIntersection){
        delivery.setDestination(newIntersection);
        notifyObservers(NotifType.LIGHT_UPDATE);
    }
    public void addTours(Collection<Tour> tours) {
        boolean hasChanged = this.tours.addAll(tours);
        if (hasChanged) notifyObservers(NotifType.LIGHT_UPDATE);
    }

    public void removeTours(Collection<Tour> tours) {
        boolean hasChanged = this.tours.removeAll(tours);
        if(hasChanged) notifyObservers(NotifType.LIGHT_UPDATE);
    }

    /**
     * Withdraw the delivery at this position if it exists
     * @param i : the position where the delivery can be
     * @return true if a delivery was at this position and was successfully remove
     */
    public boolean removeDeliveryAt(Intersection i){ return false; }

    /**
     * Withdraw the delivery  if it exists
     * @param deliveryRequest : the position where the delivery can be
     * @return true if a delivery was successfully remove
     */
    public boolean removeDelivery(DeliveryRequest deliveryRequest){
        for(Tour tour : this.tours){
            if(tour.removeDelivery(deliveryRequest)) {
                this.notifyObservers(NotifType.LIGHT_UPDATE);
                return true;
            }
        }
        return false;
    }

    /**
     * Add a new intersection to the city map.
     * @param intersection The intersection to be added to the CityMap.
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

    public void setIntersectionsSegmentsWarehouse(LinkedList<Intersection> intersections, LinkedList<Segment> segments, Warehouse warehouse) {
        this.intersections = intersections;
        this.segments = segments;
        this.warehouse = warehouse;
        this.updateMinMax();
        this.notifyObservers(NotifType.FULL_UPDATE);
    }

    /**
     * Set the warehouse of the CityMap.
     * @param warehouse warehouse to be set as the warehouse of the CityMap
     */
    public void setWarehouse(Warehouse warehouse){
        this.warehouse = warehouse;
    }

    /**
     * Get the warehouse of the CityMap
     * @return
     */
    public Warehouse getWarehouse(){return this.warehouse;}
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
        boolean equalWarehouses;
        if(warehouse != null) {
            equalWarehouses = warehouse.equals(otherMap.warehouse);
        } else {
            equalWarehouses = otherMap.warehouse == null;
        }
        return equalWarehouses && equalIntersections && equalSegments;
    }

    public DeliveryRequest getSelectedDelivery() {
        return this.selectedDelivery;
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

    public LinkedList<TourStep> computePath(Tour tour) {
        HashMap<String, Integer> idMap = new HashMap<>();
        int length = intersections.size();
        for(Intersection inter : intersections) {
            idMap.put(inter.getId(), idMap.size());
        }
        ArrayList<ArrayList<Edge>> adjList = new ArrayList<>();
        for(int i = 0; i < length; i++) {
            adjList.add(new ArrayList<>());
        }
        for(Segment segment : segments) {
            int originIndex = idMap.get(segment.getOrigin().getId());
            int destinationIndex = idMap.get(segment.getDestination().getId());
            adjList.get(originIndex)
                    .add(new Edge(originIndex, destinationIndex, segment.getLength(), segment));
        }

        ArrayList<DeliveryRequest> deliveries = tour.getDeliveries();

        if(deliveries.isEmpty()) {
            tour.setTourSteps(new LinkedList<>());
            return tour.getTourSteps();
        }

        deliveries.add(0, new DeliveryRequest(warehouse.getIntersection()));

        ArrayList<ArrayList<Double>> adjMatrix = new ArrayList<>();
        ArrayList<ArrayList<Edge>> parentSegments = new ArrayList<>();
        ArrayList<Integer> graphIndices = new ArrayList<>();

        for(DeliveryRequest deliveryRequest : deliveries) {
            ArrayList<Double> distances = new ArrayList<>(Collections.nCopies(length, Double.MAX_VALUE));
            ArrayList<Edge> parents = new ArrayList<>(Collections.nCopies(length, null));
            int index = idMap.get(deliveryRequest.getDestination().getId());
            distances.set(index, 0.0);
            Dijkstra(index, adjList, distances, parents);
            adjMatrix.add(new ArrayList<>());
            for (DeliveryRequest delivery : deliveries) {
                adjMatrix.get(adjMatrix.size() - 1)
                        .add(distances.get(idMap.get(delivery.getDestination().getId())));
            }
            parentSegments.add(parents);
            graphIndices.add(index);
        }

        Graph graph = new Graph(adjMatrix);
        TSP solver = new TSP();
        solver.searchSolution(Constants.tspMaximumTimeMillis, graph);

        LinkedList<TourStep> tourSteps = new LinkedList<>();

        for(int i = 0; i < deliveries.size(); i++) {
            int currentNode = solver.getSolution(i), nextNode = graphIndices.get(0);
            if(i + 1 != deliveries.size()) {
                nextNode = graphIndices.get(solver.getSolution(i + 1));
            }

            LinkedList<Segment> path = new LinkedList<>();
            double pathLength = 0;
            while(nextNode != graphIndices.get(currentNode)) {
                Edge parentEdge = parentSegments.get(currentNode).get(nextNode);
                path.add(0, parentEdge.getSegment());
                nextNode = parentEdge.getOrigin();
                pathLength += parentEdge.getLength();
            }

            LocalTime startTime = (tourSteps.isEmpty() ? LocalTime.of(8, 0) : tourSteps.getLast().getDeparture());
            int hourDuration = (int)Math.floor(pathLength / Constants.courierSpeed);
            int minutesDuration = (int)Math.ceil(60.0 * (pathLength - Constants.courierSpeed*hourDuration) / Constants.courierSpeed);
            LocalTime endTime = startTime.plusHours(hourDuration).plusMinutes(minutesDuration);
            tourSteps.add(new TourStep(path, startTime, endTime));
        }

        tour.setTourSteps(tourSteps);
        return tourSteps;
    }

    private void Dijkstra(int root, ArrayList<ArrayList<Edge>> adjList, ArrayList<Double> distances, ArrayList<Edge> parent) {
        PriorityQueue<Node> priority_queue = new PriorityQueue<>();
        priority_queue.add(new Node(root, 0));
        while(!priority_queue.isEmpty()) {
            Node currentNode = priority_queue.poll();
            if(currentNode.getDistance() > distances.get(currentNode.getIndex())) {
                continue;
            }
            for(Edge edge : adjList.get(currentNode.getIndex())) {
                if(distances.get(edge.getDestination()) > currentNode.getDistance() + edge.getLength()) {
                    distances.set(edge.getDestination(), currentNode.getDistance() + edge.getLength());
                    parent.set(edge.getDestination(), edge);
                    priority_queue.add(new Node(edge.getDestination(), distances.get(edge.getDestination())));
                }
            }
        }
    }

    /**
     * Find the closest intersection to a point
     * @param longitude
     * @param latitude
     * @return intersectionMin the closer intersection in terms of distance
     */
    public Intersection getClosestIntersection(double longitude, double latitude){
        if(!this.intersections.isEmpty()){
            double distanceMin = Double.MAX_VALUE;
            double distanceTmp;
            Intersection intersectionMin = null;

            for(Intersection intersection : this.intersections) {
                distanceTmp = Math.sqrt(Math.pow(intersection.latitude-latitude,2)+Math.pow(intersection.longitude - longitude, 2));

                if(distanceTmp < distanceMin){
                    distanceMin = distanceTmp;
                    intersectionMin = intersection;
                }
            }
            return intersectionMin;
        }
        return null;

    }

    /**
     * Find the closest delivery to a point
     * @param longitude
     * @param latitude
     * @return intersectionMin the closer intersection in terms of distance
     */
    public DeliveryRequest getClosestDelivery(double longitude, double latitude){
        double distanceMin = Double.MAX_VALUE;
        DeliveryRequest deliveryMin = null;
        for(Tour tour : this.tours) {
            for(DeliveryRequest delivery : tour.getDeliveries()) {
                Intersection intersection = delivery.getDestination();
                double distanceTmp = Math.sqrt(Math.pow(intersection.latitude - latitude, 2) + Math.pow(intersection.longitude - longitude, 2));
                if (distanceTmp < distanceMin) {
                    distanceMin = distanceTmp;
                    deliveryMin = delivery;
                }
            }
        }
        return deliveryMin;

    }

    public void selectDelivery(DeliveryRequest delivery) {
        this.selectedDelivery = delivery;
        this.notifyObservers(NotifType.LIGHT_UPDATE);
    }

    public void clearDeliverySelection() {
        this.selectedDelivery = null;
        this.notifyObservers(NotifType.LIGHT_UPDATE);
    }

    public LinkedList<Intersection> getIntersections() {
        return intersections;
    }
}
