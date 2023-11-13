package com.insa.lifraison.model;

import com.insa.lifraison.observer.Observable;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;
import com.insa.lifraison.utils.*;
import javafx.scene.paint.Color;

import java.time.LocalTime;

/**
 * Object that stores all the intersections and segments in the city,
 * as well as the warehouse instance.
 */
public class CityMap extends Observable {

    private final Color[] tourColors = {Color.BLUE, Color.ORANGE, Color.LAWNGREEN, Color.LIGHTGOLDENRODYELLOW};
    /**
     * List of all {@link com.insa.lifraison.model.Intersection} of the city
     */
    private final LinkedList<Intersection> intersections;
    /**
     * List of all {@link com.insa.lifraison.model.Segment} of the city
     */
    private final LinkedList<Segment> segments;
    /**
     * Unique {@link com.insa.lifraison.model.Warehouse} of the city
     */
    private final Warehouse warehouse;

    private LinkedList<Tour> tours;

    private DeliveryRequest temporaryDelivery;

    private double minLatitude, maxLatitude, minLongitude, maxLongitude;

    public CityMap(LinkedList<Intersection> intersections, LinkedList<Segment> segments, Warehouse warehouse) {
        this.intersections = intersections;
        this.segments = segments;
        this.warehouse = warehouse;
        this.updateMinMax();
        this.tours = new LinkedList<>();
        this.addTour(new Tour());
        this.temporaryDelivery = null;
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
    public boolean addDelivery(int index, DeliveryRequest newDelivery){
        return tours.get(index).addDelivery(newDelivery);
    }

    /**
     * add delivery which is selected to the cityMap
     * @param newDelivery
     * @return
     */
    public void setTemporaryDelivery(DeliveryRequest newDelivery){
        if(this.temporaryDelivery != null)
            clearTemporaryDelivery();
        this.temporaryDelivery = newDelivery;
        notifyObservers(NotifType.ADD, newDelivery);
    }

    public void addTour(Tour tour) {
        int i = 0;
        if (tour.getId() == -1) {
            while (i < this.tours.size() && this.tours.get(i).getId() == i) {
                i++;
            }
            tour.setId(i);
            tour.setColor(tourColors[i % tourColors.length]);
        } else {
            while (i < this.tours.size() && this.tours.get(i).getId() < tour.getId()) {
                i++;
            }
        }

        this.tours.add(i, tour);
        notifyObservers(NotifType.ADD, tour);
    }

    public boolean removeTour(Tour tour) {
        boolean hasChanged = this.tours.remove(tour);
        if (hasChanged) notifyObservers(NotifType.REMOVE, tour);
        return hasChanged;
    }
    public DeliveryRequest moveDelivery(Intersection newIntersection){
        notifyObservers(NotifType.REMOVE, temporaryDelivery);
        temporaryDelivery.setIntersection(newIntersection);
        notifyObservers(NotifType.ADD, temporaryDelivery);
        return temporaryDelivery;
    }
    public void addTours(Collection<Tour> tours) {
        for(Tour tour : tours) {
            this.addTour(tour);
        }
    }

    public boolean removeTours(Collection<Tour> tours) {
        boolean hasChanged = false;
        for(Tour tour : tours) {
            hasChanged = hasChanged || this.removeTour(tour);
        }
        return hasChanged;
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

    public DeliveryRequest getTemporaryDelivery() {
        return this.temporaryDelivery;
    }

    public void updateMinMax() {
        if (this.intersections.isEmpty()) {
            this.minLatitude = Double.MAX_VALUE;
            this.minLongitude = Double.MAX_VALUE;
            this.maxLatitude = Double.MIN_VALUE;
            this.maxLongitude = Double.MIN_VALUE;
        } else {
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
            idMap.put(inter.id, idMap.size());
        }
        ArrayList<ArrayList<Edge>> adjList = new ArrayList<>();
        for(int i = 0; i < length; i++) {
            adjList.add(new ArrayList<>());
        }
        for(Segment segment : segments) {
            int originIndex = idMap.get(segment.origin.id);
            int destinationIndex = idMap.get(segment.destination.id);
            adjList.get(originIndex)
                    .add(new Edge(originIndex, destinationIndex, segment.length, segment));
        }

        ArrayList<DeliveryRequest> deliveries = tour.getDeliveries();

        if(deliveries.isEmpty()) {
            tour.setTourSteps(new LinkedList<>());
            return tour.getTourSteps();
        }

        deliveries.add(0, new DeliveryRequest(warehouse.intersection));

        ArrayList<ArrayList<Double>> adjMatrix = new ArrayList<>();
        ArrayList<ArrayList<Edge>> parentSegments = new ArrayList<>();
        ArrayList<Integer> graphIndices = new ArrayList<>();

        for(DeliveryRequest deliveryRequest : deliveries) {
            ArrayList<Double> distances = new ArrayList<>(Collections.nCopies(length, Double.MAX_VALUE));
            ArrayList<Edge> parents = new ArrayList<>(Collections.nCopies(length, null));
            int index = idMap.get(deliveryRequest.getIntersection().id);
            distances.set(index, 0.0);
            Dijkstra(index, adjList, distances, parents);
            adjMatrix.add(new ArrayList<>());
            for (DeliveryRequest delivery : deliveries) {
                adjMatrix.get(adjMatrix.size() - 1)
                        .add(distances.get(idMap.get(delivery.getIntersection().id)));
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

            LocalTime startTime = (tourSteps.isEmpty() ? LocalTime.of(8, 0) : tourSteps.getLast().departure);
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

    public void clearTemporaryDelivery() {
        DeliveryRequest deliveryRequest = this.temporaryDelivery;
        this.temporaryDelivery = null;
        this.notifyObservers(NotifType.REMOVE, deliveryRequest);
    }

    public LinkedList<Intersection> getIntersections() {
        return intersections;
    }
}
