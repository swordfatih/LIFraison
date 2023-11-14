package com.insa.lifraison.model;

import com.insa.lifraison.observer.Observable;

import java.time.Duration;
import java.util.*;

import static java.lang.Integer.valueOf;
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

    private ArrayList<ArrayList<Edge>> adjList;
    private HashMap<String, Integer> intersectionIdMap;

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
        this.adjList = createAdjacencyList();
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
        for(Tour t : this.tours) {
            System.out.println(t.getId());
        }
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

    public void setIntersectionsSegmentsWarehouse(LinkedList<Intersection> intersections, LinkedList<Segment> segments, Warehouse warehouse) {
        this.intersections = intersections;
        this.segments = segments;
        this.warehouse = warehouse;
        this.updateMinMax();
        this.notifyObservers(NotifType.FULL_UPDATE);
        this.adjList = createAdjacencyList();
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

    private ArrayList<ArrayList<Edge>> createAdjacencyList() {
        this.intersectionIdMap = new HashMap<>();
        int length = intersections.size();
        for(Intersection inter : intersections) {
            intersectionIdMap.put(inter.id, intersectionIdMap.size());
        }
        ArrayList<ArrayList<Edge>> adjList = new ArrayList<>();
        for(int i = 0; i < length; i++) {
            adjList.add(new ArrayList<>());
        }
        for(Segment segment : segments) {
            int originIndex = intersectionIdMap.get(segment.origin.id);
            int destinationIndex = intersectionIdMap.get(segment.destination.id);
            adjList.get(originIndex)
                    .add(new Edge(originIndex, destinationIndex, segment.length, segment));
        }

        return adjList;
    }

    public LinkedList<TourStep> computePath(Tour tour) {
        ArrayList<DeliveryRequest> deliveries = tour.getDeliveries();
        int intersectionCnt = intersections.size();

        if(deliveries.isEmpty()) {
            tour.setTourSteps(new LinkedList<>());
            return tour.getTourSteps();
        }

        if(deliveries.stream().anyMatch(d -> d.getTimeWindowStart() != null)) {
            ComputeTimeWindows(deliveries);
        }

        deliveries.add(0, new DeliveryRequest(warehouse.intersection));

        ArrayList<ArrayList<Double>> adjMatrix = new ArrayList<>();
        ArrayList<ArrayList<Edge>> parentSegments = new ArrayList<>();
        ArrayList<Integer> graphIndices = new ArrayList<>();

        for(DeliveryRequest deliveryRequest : deliveries) {
            ArrayList<Double> distances = new ArrayList<>(Collections.nCopies(intersectionCnt, Double.MAX_VALUE));
            ArrayList<Edge> parents = new ArrayList<>(Collections.nCopies(intersectionCnt, null));
            int index = intersectionIdMap.get(deliveryRequest.getDestination().id);
            distances.set(index, 0.0);
            Dijkstra(index, distances, parents);

            adjMatrix.add(new ArrayList<>());
            for (int i = 0; i < deliveries.size(); i++) {
                if (deliveries.get(i).getTimeWindowStart() != null &&
                        deliveryRequest.getTimeWindowStart() != null &&
                        deliveries.get(i).getTimeWindowStart().isBefore(deliveryRequest.getTimeWindowStart())) {
                    distances.set(i, Double.MAX_VALUE);
                }
                adjMatrix.get(adjMatrix.size() - 1)
                        .add(distances.get(intersectionIdMap.get(deliveries.get(0).getDestination().id)));
            }

            parentSegments.add(parents);
            graphIndices.add(index);
        }

        Graph graph = new Graph(adjMatrix);
        TSP solver = new TSP();
        solver.searchSolution(Constants.tspMaximumTimeMillis, graph);

        LinkedList<TourStep> tourSteps = new LinkedList<>();

        for(int i = 0; i < deliveries.size(); i++) {
            int currentNode = solver.getSolution(i), nextNode = graphIndices.get(0), finalNode = 0;
            if(i + 1 != deliveries.size()) {
                nextNode = graphIndices.get(solver.getSolution(i + 1));
                finalNode = i + 1;
            }

            LinkedList<Segment> path = new LinkedList<>();
            double pathLength = 0;
            while(nextNode != graphIndices.get(currentNode)) {
                Edge parentEdge = parentSegments.get(currentNode).get(nextNode);
                path.add(0, parentEdge.getSegment());
                nextNode = parentEdge.getOrigin();
                pathLength += parentEdge.getLength();
            }

            LocalTime startTime = (tourSteps.isEmpty() ? LocalTime.of(8, 0) : tourSteps.getLast().arrival);
            int hourDuration = (int)Math.floor(pathLength / Constants.courierSpeed);
            int minutesDuration = (int)Math.ceil(60.0 * (pathLength - Constants.courierSpeed*hourDuration) / Constants.courierSpeed);
            LocalTime endTime = startTime.plusHours(hourDuration).plusMinutes(minutesDuration);
            if(endTime.isBefore(deliveries.get(finalNode).getTimeWindowStart())) {
                var delta = Duration.between(endTime, deliveries.get(finalNode).getTimeWindowStart());
                startTime = startTime.plus(delta);
                endTime = endTime.plus(delta);
            }
            tourSteps.add(new TourStep(path, startTime, endTime));
            System.out.println(startTime.toString() + " " + endTime.toString());
        }

        deliveries.remove(0);
        tour.setTourSteps(tourSteps);
        return tourSteps;
    }

    private void Dijkstra(int root, ArrayList<Double> distances, ArrayList<Edge> parent) {
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

    private void ComputeTimeWindows(ArrayList<DeliveryRequest> deliveries) {
        System.out.println(deliveries.size());
        ArrayList<Integer> timeWindows = new ArrayList<>(Collections.nCopies(5, 0));
        for(DeliveryRequest deliveryRequest : deliveries) {
            if(deliveryRequest.getTimeWindowStart() != null) {
                int index = (deliveryRequest.getTimeWindowStart().getHour() - 8)/2;
                timeWindows.set(index, timeWindows.get(index) + 1);
            }
        }

        for(DeliveryRequest deliveryRequest : deliveries) {
            if(deliveryRequest.getTimeWindowStart() == null) {
                int index = timeWindows.indexOf(Collections.min(timeWindows));
                deliveryRequest.setTimeWindowStart(LocalTime.of(8 + 2*index, 0));
                timeWindows.set(index, timeWindows.get(index) + 1);
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
