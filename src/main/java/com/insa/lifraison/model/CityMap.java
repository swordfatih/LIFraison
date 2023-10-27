package com.insa.lifraison.model;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;
import com.insa.lifraison.utils.*;
import java.time.LocalTime;

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

    /**
     * Set the warehouse of the CityMap.
     * @param warehouse
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

    public LinkedList<TourStep> computePath(Tour tour) {
        HashMap<String, Integer> idMap = new HashMap<>();
        int length = intersections.size();
        for(Intersection inter : intersections) {
            idMap.put(inter.getId(), idMap.size());
        }
        ArrayList<ArrayList<Edge>> adjList = new ArrayList<>(Collections.nCopies(length, new ArrayList<>()));
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
        }

        Graph graph = new Graph(adjMatrix);
        TSP solver = new TSP();
        solver.searchSolution(Constants.tspMaximumTimeMillis, graph);

        LinkedList<TourStep> tourSteps = new LinkedList<>();

        for(int i = 0; i < deliveries.size(); i++) {
            int currentNode = solver.getSolution(i), nextNode = 0;
            if(i + 1 != deliveries.size()) {
                nextNode = solver.getSolution(i + 1);
            }

            LinkedList<Segment> path = new LinkedList<>();
            double pathLength = 0;
            while(nextNode != currentNode) {
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
}
