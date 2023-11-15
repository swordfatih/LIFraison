package com.insa.lifraison.model;

import com.insa.lifraison.observer.Observable;

import java.time.Duration;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.insa.lifraison.observer.Selectable;
import com.insa.lifraison.utils.*;
import javafx.scene.paint.Color;

import java.time.LocalTime;

/**
 * CityMap is the class in model that stores all
 * intersections and segments in the city,
 * as well as the warehouse instance.
 * It also has tours of the city.
 * This class extends from the class Observable {@link com.insa.lifraison.observer.Observable}
 */
public class CityMap extends Observable {

    private final Color selectionColor = Color.PURPLE;
    private final Color[] tourColors = {Color.BLUE, Color.ORANGE, Color.GREEN, Color.BROWN};
    /**
     * List of all {@link com.insa.lifraison.model.Intersection} of the city
     */
    private LinkedList<Intersection> intersections;
    /**
     * List of all {@link com.insa.lifraison.model.Segment} of the city
     */
    private final LinkedList<Segment> segments;
    /**
     * Unique {@link com.insa.lifraison.model.Warehouse} of the city
     */
    private final Warehouse warehouse;
    /**
     * List of all {@link com.insa.lifraison.model.Tour} of the city
     */
    private LinkedList<Tour> tours;
    /**
     * Temporary {@link com.insa.lifraison.model.DeliveryRequest} of the city
     */
    private DeliveryRequest temporaryDelivery;

    private double minLatitude, maxLatitude, minLongitude, maxLongitude;

    private ArrayList<ArrayList<Edge>> adjList;
    private HashMap<String, Integer> intersectionIdMap;
    /**
     * Temporary {@link com.insa.lifraison.observer.Selectable}
     */
    private Selectable selectedComponent;

    /**
     * CityMap constructor
     * @param intersections list of all intersections of the cityMap
     * @param segments list of all segments of the cityMap
     * @param warehouse the warehouse of the firm in the particular city
     */
    public CityMap(LinkedList<Intersection> intersections, LinkedList<Segment> segments, Warehouse warehouse) {
        this.intersections = intersections;
        this.segments = segments;
        this.warehouse = warehouse;
        this.updateMinMax();
        this.tours = new LinkedList<>();
        this.addTour(new Tour());
        this.selectedComponent = null;
        this.temporaryDelivery = null;
        this.adjList = createAdjacencyList();
    }

    /**
     * Give to the CityMap the selected component {@link com.insa.lifraison.observer.Selectable} and select it
     * If the selectedComponent is already present (not null), we unselect it first
     * @param obj the selected component
     */
    public void selectComponent(Selectable obj) {
        if(this.selectedComponent != null) {
            this.selectedComponent.unselect();
        }
        this.selectedComponent = obj;
        this.selectedComponent.select();
    }

    /**
     * unselect the selected component {@link com.insa.lifraison.observer.Selectable} and make the
     * selectComponent to null in the CityMap
     */
    public void clearSelection() {
        if(this.selectedComponent != null) {
            this.selectedComponent.unselect();
        }
        this.selectedComponent = null;
    }

    /**
     * Give the selectedComponent of the CityMap
     * @return the selectedComponent
     */
    public Selectable getSelectedComponent() {
        return this.selectedComponent;
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

    /**
     * Give the color of the selected component
     * @return color
     */
    public Color getSelectionColor() {
        return selectionColor;
    }

    /**
     * Give all tours of the CityMap
     * @return LinkedList<Tour> tours
     */
    public LinkedList<Tour> getTours() { return tours; }

    /**
     * Give all segments of the CityMap
     * @return LinkedList<Segment> segments
     */
    public LinkedList<Segment> getSegments() {
        return this.segments;
    }

    /**
     * Give all intersections of the CityMap
     * @return LinkedList<Intersection> intersections
     */
    public LinkedList<Intersection> getIntersections() {
        return intersections;
    }

    /**
     * Add a delivery which isn't linked to a tour in the CityMap
     * This delivery is temporary and will be deleted as soon as the user add it to a tour
     * Notify observers {@link com.insa.lifraison.observer.Observer} that a new delivery has been added {@link }
     * @param newDelivery the temporary delivery
     */
    public void setTemporaryDelivery(DeliveryRequest newDelivery){
        if(this.temporaryDelivery != null)
            clearTemporaryDelivery();
        this.temporaryDelivery = newDelivery;
        notifyObservers(NotifType.ADD, newDelivery);
    }

    /**
     * Give the temporaryDelivery
     * @return temporaryDelivery
     */
    public DeliveryRequest getTemporaryDelivery() {
        return this.temporaryDelivery;
    }

    /**
     * Clear the delivery which is temporal
     */
    public void clearTemporaryDelivery() {
        DeliveryRequest deliveryRequest = this.temporaryDelivery;
        this.temporaryDelivery = null;
        this.notifyObservers(NotifType.REMOVE, deliveryRequest);
    }

    /**
     * Add a new tour to tours of the CityMap
     * Notify observers {@link com.insa.lifraison.observer.Observer} that a new tour have been added
     * @param tour the new tour which is added
     */
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

    /**
     * Remove a tour from tours of the CityMap
     * Notify observers {@link com.insa.lifraison.observer.Observer} that a tour have been removed
     * @param tour the tour which is removed
     */
    public boolean removeTour(Tour tour) {
        boolean hasChanged = this.tours.remove(tour);
        if (hasChanged) notifyObservers(NotifType.REMOVE, tour);
        return hasChanged;
    }

    /**
     * Add a collection of tour in the CityMap
     * @param tours the collection of tour which will be added to tours of the CityMap
     */
    public void addTours(Collection<Tour> tours) {
        for(Tour tour : tours) {
            this.addTour(tour);
        }
    }

    /**
     * Remove a collection of tour in the CityMap
     * @param tours the collection of tour which will be removed to tours of the CityMap
     */
    public boolean removeTours(Collection<Tour> tours) {
        boolean hasChanged = false;
        for(Tour tour : tours) {
            hasChanged = hasChanged || this.removeTour(tour);
        }
        return hasChanged;
    }

    /**
     * Get the warehouse of the CityMap
     * @return the warehouse of the CityMap
     */
    public Warehouse getWarehouse(){return this.warehouse;}

    /**
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

    /**
     * Find the new maximum and minimum latitude and longitude values of intersections of the CityMap.
     * Update attributes minLatitude, minLongitude, maxLatitude, maxLongitude of the class.
     */
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

    /**
     * Give the minLatitude of the CityMap
     * @return double minLatitude
     */
    public double getMinLatitude(){
        return minLatitude;
    }

    /**
     * Give the maxLatitude of the CityMap
     * @return double maxLatitude
     */
    public double getMaxLatitude(){
        return maxLatitude;
    }

    /**
     * Give the minLongitude of the CityMap
     * @return double minLongitude
     */
    public double getMinLongitude(){
        return minLongitude;
    }

    /**
     * Give the maxLongitude of the CityMap
     * @return double maxLongitude
     */
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

        boolean hasImpossible = false;

        for(int i = 0; i < deliveries.size(); i++) {
            DeliveryRequest deliveryRequest = deliveries.get(i);
            ArrayList<Double> distances = new ArrayList<>(Collections.nCopies(intersectionCnt, Double.MAX_VALUE));
            ArrayList<Edge> parents = new ArrayList<>(Collections.nCopies(intersectionCnt, null));
            int index = intersectionIdMap.get(deliveryRequest.getIntersection().id);
            distances.set(index, 0.0);
            Dijkstra(index, distances, parents);

            if(checkImpossible(distances, deliveries, i)) {
                hasImpossible = true;
            }

            adjMatrix.add(new ArrayList<>());
            for (int j = 0; j < deliveries.size(); j++) {
                if (deliveries.get(j).getTimeWindowStart() != null &&
                        deliveryRequest.getTimeWindowStart() != null &&
                        deliveries.get(j).getTimeWindowStart().isBefore(deliveryRequest.getTimeWindowStart())) {
                    distances.set(intersectionIdMap.get(deliveries.get(j).getIntersection().id), Double.MAX_VALUE);
                }
                adjMatrix.get(adjMatrix.size() - 1)
                        .add(distances.get(intersectionIdMap.get(deliveries.get(j).getIntersection().id)));
            }

            parentSegments.add(parents);
            graphIndices.add(index);
        }

        if(hasImpossible) {
            deliveries.remove(0);
            tour.setTourSteps(new LinkedList<>());
            return tour.getTourSteps();
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
            if(deliveries.get(finalNode).getTimeWindowStart() != null &&
                    endTime.isBefore(deliveries.get(finalNode).getTimeWindowStart())) {
                var delta = Duration.between(endTime, deliveries.get(finalNode).getTimeWindowStart());
                startTime = startTime.plus(delta);
                endTime = endTime.plus(delta);
            } else if(deliveries.get(finalNode).getTimeWindowStart() == null) {
                int windowStart = endTime.getHour();
                deliveries.get(finalNode).setTimeWindowStart(LocalTime.of(windowStart, 0));
                deliveries.get(finalNode).setTimeWindowEnd(LocalTime.of(windowStart + 1, 0));
            }
            if (endTime.isAfter(deliveries.get(finalNode).getTimeWindowStart())) {
                deliveries.get(finalNode).setState(DeliveryState.Late);
            } else {
                deliveries.get(finalNode).setState(DeliveryState.Ok);
            }

            tourSteps.add(new TourStep(path, startTime, endTime));
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
        ArrayList<Integer> timeWindows = new ArrayList<>(Collections.nCopies(4, 0));
        for(DeliveryRequest deliveryRequest : deliveries) {
            if(deliveryRequest.getTimeWindowStart() != null) {
                int index = deliveryRequest.getTimeWindowStart().getHour() - 8;
                timeWindows.set(index, timeWindows.get(index) + 1);
            }
        }

        for(DeliveryRequest deliveryRequest : deliveries) {
            if(deliveryRequest.getTimeWindowStart() == null) {
                int index = timeWindows.indexOf(Collections.min(timeWindows));
                deliveryRequest.setTimeWindow(LocalTime.of(8 + index, 0), LocalTime.of(9 + index, 0));
                timeWindows.set(index, timeWindows.get(index) + 1);
            }
        }
    }

    private boolean checkImpossible(ArrayList<Double> distances, ArrayList<DeliveryRequest> deliveries, int index) {
        boolean hasImpossible = false;

        if(index != 0 && distances.get(0).equals(Double.MAX_VALUE)) {
            deliveries.get(index).setState(DeliveryState.NotPossible);
            return true;
        } else if(index == 0) {
            for(int i = 1; i < deliveries.size(); i++) {
                if(distances
                        .get(intersectionIdMap.get(deliveries.get(index).getIntersection().id))
                        .equals(Double.MAX_VALUE)) {
                    hasImpossible = true;
                    deliveries.get(i).setState(DeliveryState.NotPossible);
                }
            }
        }

        return hasImpossible;
    }

}
