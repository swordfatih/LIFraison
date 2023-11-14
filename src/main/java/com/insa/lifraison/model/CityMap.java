package com.insa.lifraison.model;

import com.insa.lifraison.observer.Observable;

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
    private final Color[] tourColors = {Color.BLUE, Color.ORANGE, Color.LAWNGREEN, Color.PINK};
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
    /**
     * List of all {@link com.insa.lifraison.model.Tour} of the city
     */
    private LinkedList<Tour> tours;
    /**
     * Temporary {@link com.insa.lifraison.model.DeliveryRequest} of the city
     */
    private DeliveryRequest temporaryDelivery;

    private double minLatitude, maxLatitude, minLongitude, maxLongitude;
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

    /**
     * Miguel n'a pas commenté sa putain de méthode. Il se démerde à vous expliquer
     * En cas d'incompréhension appeler le "c'est pas mon problème ! c'est littéralement écrit" Miguel 14/11/2023
     * @param tour
     * @return
     */
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

        deliveries.remove(0);
        tour.setTourSteps(tourSteps);
        return tourSteps;
    }

    /**
     * "c'est juste un dijkstra"
     * @param root
     * @param adjList
     * @param distances
     * @param parent
     */
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
