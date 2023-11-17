package com.insa.lifraison.model;

import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.observer.Observer;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CityMapTest {

    Observer observer;
    int updateCalled;
    Observable.NotifType updateCalledType;
    Observable observableCalled;
    Object objectCalled;

    @BeforeEach
    void Setup() {
        updateCalled = 0;

        observer = new Observer(){
            public void update(Observable.NotifType notifType, Observable obs, Object o) {
                updateCalled++;
                updateCalledType = notifType;
                observableCalled = obs;
                objectCalled = o;
            }
        };

    }

    @Test
    void constructorTest() {
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2));
        Segment segment = new Segment(intersection1, intersection2, 0.43, "segment");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment));
        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        assertEquals(intersections, cityMap.getIntersections());
        assertEquals(segments, cityMap.getSegments());
        assertEquals(warehouse, cityMap.getWarehouse());
        assertEquals(0.12, cityMap.getMinLatitude());
        assertEquals(0.24, cityMap.getMinLongitude());
        assertEquals(0.53, cityMap.getMaxLatitude());
        assertEquals(0.34, cityMap.getMaxLongitude());
        assertEquals(1, cityMap.getTours().size());
        Tour tr = new Tour();
        tr.setId(0);
        tr.setColor(Color.BLUE);
        assertEquals(tr, cityMap.getTours().get(0));
        assertNull(cityMap.getTemporaryDelivery());
    }

    @Test
    void getNumberDeliveriesTest1() {
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2));
        Segment segment = new Segment(intersection1, intersection2, 0.43, "segment");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment));
        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        assertEquals(0, cityMap.getNumberDeliveries());
    }

    @Test
    void getNumberDeliveriesTest2() {
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        Intersection intersection3 = new Intersection("intersection3", 0.23, 0.63);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2, intersection3));
        LinkedList<Segment> segments = new LinkedList<>();
        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        cityMap.addDelivery(0, new DeliveryRequest(intersection2));
        cityMap.addDelivery(0, new DeliveryRequest(intersection3));
        assertEquals(2, cityMap.getNumberDeliveries());
    }

    @Test
    void updateMinMaxTest(){
        Intersection inter1 = new Intersection("1", 1, 1);
        Intersection inter2 = new Intersection("2", 4, 2);

        Segment segment1 = new Segment(inter1, inter2, 10, "Rue 1");
        Segment segment2 = new Segment(inter2, inter1, 10, "Rue 2");

        Warehouse warehouse = new Warehouse(inter1);

        LinkedList<Intersection> intersections = new LinkedList<>(List.of(inter1, inter2));
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment1, segment2));

        CityMap map = new CityMap(intersections, segments, warehouse);

        assertEquals(map.getMinLatitude(), 1);
        assertEquals(map.getMinLongitude(), 1);
        assertEquals(map.getMaxLatitude(), 2);
        assertEquals(map.getMaxLongitude(), 4);
    }

    @Test
    void setAndCleanTemporaryDelivery(){
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2));
        Segment segment = new Segment(intersection1, intersection2, 0.43, "segment");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment));
        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        cityMap.addObserver(observer);
        DeliveryRequest deliveryRequest = new DeliveryRequest(intersection2);
        cityMap.addDelivery(0, deliveryRequest);


        cityMap.setTemporaryDelivery(deliveryRequest);
        assertEquals(cityMap.getTemporaryDelivery(), deliveryRequest);
        assertEquals(updateCalled, 1);
        assertEquals(Observable.NotifType.ADD, updateCalledType);
        assertEquals(observableCalled, cityMap);
        assertEquals(objectCalled, deliveryRequest);


        cityMap.clearTemporaryDelivery();
        assertNull(cityMap.getTemporaryDelivery());
        assertEquals(updateCalled, 2);
        assertEquals(Observable.NotifType.REMOVE, updateCalledType);
        assertEquals(observableCalled, cityMap);
        assertEquals(objectCalled, deliveryRequest);
    }

    @Test
    void addDeliveryTest() {
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2));
        Segment segment = new Segment(intersection1, intersection2, 0.43, "segment");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment));
        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        cityMap.addObserver(observer);
        DeliveryRequest deliveryRequest = new DeliveryRequest(intersection2);
        cityMap.addDelivery(0, deliveryRequest);
        assertTrue(cityMap.getTours().get(0).getDeliveries().contains(deliveryRequest));
        assertEquals(updateCalled, 0);
    }

    @Test
    void addTourTest() {
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2));
        Segment segment = new Segment(intersection1, intersection2, 0.43, "segment");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment));
        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        cityMap.addObserver(observer);
        Tour tour = new Tour();
        tour.addDelivery(new DeliveryRequest(intersection2));
        cityMap.addTour(tour);
        assertTrue(cityMap.getTours().contains(tour));
        assertEquals(updateCalled, 1);
        assertEquals(Observable.NotifType.ADD, updateCalledType);
        assertEquals(observableCalled, cityMap);
        assertEquals(objectCalled, tour);
    }

    @Test
    void removeTourTest1() {
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2));
        Segment segment = new Segment(intersection1, intersection2, 0.43, "segment");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment));
        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        Tour tour = new Tour();
        tour.addDelivery(new DeliveryRequest(intersection2));
        cityMap.addTour(tour);
        cityMap.addObserver(observer);
        assertTrue(cityMap.removeTour(tour));
        assertFalse(cityMap.getTours().contains(tour));
        assertEquals(updateCalled, 1);
        assertEquals(Observable.NotifType.REMOVE, updateCalledType);
        assertEquals(observableCalled, cityMap);
        assertEquals(objectCalled, tour);
    }

    @Test
    void removeTourTest2() {
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2));
        Segment segment = new Segment(intersection1, intersection2, 0.43, "segment");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment));
        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        Tour tour = new Tour();
        tour.addDelivery(new DeliveryRequest(intersection2));
        cityMap.addObserver(observer);
        assertFalse(cityMap.removeTour(tour));
        assertEquals(updateCalled, 0);
    }

    @Test
    void addToursTest() {
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        Intersection intersection3 = new Intersection("intersection3", 0.67, 0.09);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2,intersection3));
        Segment segment1 = new Segment(intersection1, intersection2, 0.43, "segment1");
        Segment segment2 = new Segment(intersection2, intersection3, 0.43, "segment2");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment1, segment2));
        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        cityMap.addObserver(observer);
        Tour tour1 = new Tour();
        tour1.addDelivery(new DeliveryRequest(intersection2));
        Tour tour2 = new Tour();
        tour1.addDelivery(new DeliveryRequest(intersection3));
        LinkedList<Tour> tours = new LinkedList<>(List.of(tour1,tour2));
        cityMap.addTours(tours);
        assertTrue(cityMap.getTours().containsAll(tours));
        assertEquals(updateCalled, 2);
    }

    @Test
    void removeToursTest() {
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        Intersection intersection3 = new Intersection("intersection3", 0.67, 0.09);
        Intersection intersection4 = new Intersection("intersection4", 0.67, 0.09);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2,intersection3, intersection4));
        Segment segment1 = new Segment(intersection1, intersection2, 0.43, "segment1");
        Segment segment2 = new Segment(intersection2, intersection3, 0.43, "segment2");
        Segment segment3 = new Segment(intersection2, intersection4, 0.43, "segment2");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment1, segment2, segment3));
        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        cityMap.addObserver(observer);

        Tour tour1 = new Tour();
        tour1.addDelivery(new DeliveryRequest(intersection2));
        Tour tour2 = new Tour();
        tour2.addDelivery(new DeliveryRequest(intersection3));
        Tour tour3 = new Tour();
        tour3.addDelivery(new DeliveryRequest(intersection4));

        LinkedList<Tour> tours = new LinkedList<>(List.of(tour1,tour2, tour3));
        LinkedList<Tour> toursToDelete = new LinkedList<>(List.of(tour1,tour2));
        cityMap.addTours(tours);

        System.out.println("AVANT");
        for (Tour t : cityMap.getTours()){
            System.out.println(t);
        }
        assertTrue(cityMap.removeTours(toursToDelete));

        System.out.println("APRES");
        for (Tour t : cityMap.getTours()){
            System.out.println(t);
        }
        assertFalse(cityMap.getTours().contains(tour2));
        assertFalse(cityMap.getTours().contains(tour1));
    }

    @Test
    void emptyMap() {
        Intersection inter1 = new Intersection("1", 0, 0);
        Warehouse warehouse = new Warehouse(inter1);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(inter1));
        LinkedList<Segment> segments = new LinkedList<>();

        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        Tour tour = new Tour();

        LinkedList<TourStep> tourSegmentList = cityMap.computePath(tour);

        assertEquals(tourSegmentList, tour.getTourSteps());
        assertTrue(tourSegmentList.isEmpty());
    }

    @Test
    void simpleMap() {
        Intersection inter1 = new Intersection("1", 0, 0);
        Intersection inter2 = new Intersection("2", 1, 1);

        Segment segment1 = new Segment(inter1, inter2, 10, "Rue 1");
        Segment segment2 = new Segment(inter2, inter1, 10, "Rue 2");

        Warehouse warehouse = new Warehouse(inter1);

        LinkedList<Intersection> intersections = new LinkedList<>(List.of(inter1, inter2));
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment1, segment2));

        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        Tour tour = new Tour();
        tour.addDelivery(new DeliveryRequest(inter2));

        LinkedList<TourStep> tourSegmentList = cityMap.computePath(tour);

        assertEquals(tourSegmentList, tour.getTourSteps());
        assertEquals(tourSegmentList.size(), 2);

        assertEquals(tourSegmentList.get(0).segments.size(), 1);
        assertEquals(tourSegmentList.get(0).segments.getFirst(), segment1);

        assertEquals(tourSegmentList.get(1).segments.size(), 1);
        assertEquals(tourSegmentList.get(1).segments.getFirst(), segment2);
    }
}
