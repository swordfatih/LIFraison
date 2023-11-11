package com.insa.lifraison.model;

import com.insa.lifraison.model.*;
import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.observer.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CityMapTest {

    Observer observer;
    boolean updateCalled;

    Observable.NotifType updateCalledType;

    @BeforeEach
    void Setup() {
        updateCalled = false;
        observer = new Observer(){
            public void update(Observable.NotifType notifType){
                updateCalled = true;
                updateCalledType = notifType;
            }
        };
    }

    @Test
    void constructorTest1() {
        CityMap cityMap = new CityMap();
        assertTrue(cityMap.getIntersections().isEmpty());
        assertTrue(cityMap.getSegments().isEmpty());
        assertNull(cityMap.getWarehouse());
        assertEquals(Double.MAX_VALUE, cityMap.getMinLatitude());
        assertEquals(Double.MAX_VALUE, cityMap.getMinLongitude());
        assertEquals(Double.MIN_VALUE, cityMap.getMaxLatitude());
        assertEquals(Double.MIN_VALUE, cityMap.getMaxLongitude());
        assertEquals(1, cityMap.getTours().size());
        assertEquals(new Tour(), cityMap.getTours().get(0));
        assertNull(cityMap.getSelectedDelivery());
    }

    @Test
    void constructorTest2() {
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
        assertEquals(new Tour(), cityMap.getTours().get(0));
        assertNull(cityMap.getSelectedDelivery());
    }

    @Test
    void resetTest() {
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2));
        Segment segment = new Segment(intersection1, intersection2, 0.43, "segment");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment));
        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        cityMap.addDelivery(0, new DeliveryRequest(intersection2));
        cityMap.addTour(new Tour());
        cityMap.addObserver(observer);
        cityMap.reset();
        assertTrue(cityMap.getIntersections().isEmpty());
        assertTrue(cityMap.getSegments().isEmpty());
        assertNull(cityMap.getWarehouse());
        assertEquals(Double.MAX_VALUE, cityMap.getMinLatitude());
        assertEquals(Double.MAX_VALUE, cityMap.getMinLongitude());
        assertEquals(Double.MIN_VALUE, cityMap.getMaxLatitude());
        assertEquals(Double.MIN_VALUE, cityMap.getMaxLongitude());
        assertEquals(1, cityMap.getTours().size());
        assertEquals(new Tour(), cityMap.getTours().get(0));
        assertNull(cityMap.getSelectedDelivery());
        assertTrue(updateCalled);
        assertEquals(Observable.NotifType.FULL_UPDATE, updateCalledType);
    }

    @Test
    void getNumberDeliveriesTest1() {
        CityMap cityMap = new CityMap();
        assertEquals(0, cityMap.getNumberDeliveries());
    }

    @Test
    void getNumberDeliveriesTest2() {
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        Intersection intersection3 = new Intersection("intersection2", 0.23, 0.63);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2, intersection3));
        LinkedList<Segment> segments = new LinkedList<>();
        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        cityMap.addDelivery(0, new DeliveryRequest(intersection2));
        cityMap.addDelivery(0, new DeliveryRequest(intersection3));
        assertEquals(2, cityMap.getNumberDeliveries());
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
        assertTrue(updateCalled);
        assertEquals(Observable.NotifType.LIGHT_UPDATE, updateCalledType);
    }

    @Test
    void removeDeliveryTest1() {
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2));
        Segment segment = new Segment(intersection1, intersection2, 0.43, "segment");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment));
        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        DeliveryRequest deliveryRequest = new DeliveryRequest(intersection2);
        cityMap.addDelivery(0, deliveryRequest);
        cityMap.addObserver(observer);
        assertTrue(cityMap.removeDelivery(deliveryRequest));
        assertFalse(cityMap.getTours().get(0).getDeliveries().contains(deliveryRequest));
        assertTrue(updateCalled);
        assertEquals(Observable.NotifType.LIGHT_UPDATE, updateCalledType);
    }

    @Test
    void removeDeliveryTest2() {
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2));
        Segment segment = new Segment(intersection1, intersection2, 0.43, "segment");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment));
        CityMap cityMap = new CityMap(intersections, segments, warehouse);
        DeliveryRequest deliveryRequest = new DeliveryRequest(intersection2);
        cityMap.addObserver(observer);
        assertFalse(cityMap.removeDelivery(deliveryRequest));
        assertFalse(updateCalled);
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
        assertTrue(updateCalled);
        assertEquals(Observable.NotifType.LIGHT_UPDATE, updateCalledType);
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
        assertTrue(updateCalled);
        assertEquals(Observable.NotifType.LIGHT_UPDATE, updateCalledType);
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
        assertFalse(updateCalled);
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
        assertTrue(updateCalled);
        assertEquals(Observable.NotifType.LIGHT_UPDATE, updateCalledType);
    }

    @Test
    void removeToursTest() {
        Intersection intersection1 = new Intersection("intersection1", 0.34, 0.53);
        Warehouse warehouse = new Warehouse(intersection1);
        Intersection intersection2 = new Intersection("intersection2", 0.24, 0.12);
        Intersection intersection3 = new Intersection("intersection3", 0.67, 0.09);
        LinkedList<Intersection> intersections = new LinkedList<>(List.of(intersection1,intersection2,intersection3));
        Segment segment1 = new Segment(intersection1, intersection2, 0.43, "segment1");
        Segment segment2 = new Segment(intersection2, intersection3, 0.43, "segment2");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment1, segment2));
        CityMap cityMap = new CityMap(intersections, segments, warehouse);

        Tour tour1 = new Tour();
        tour1.addDelivery(new DeliveryRequest(intersection2));
        Tour tour2 = new Tour();
        tour1.addDelivery(new DeliveryRequest(intersection3));
        LinkedList<Tour> tours = new LinkedList<>(List.of(tour1,tour2));
        cityMap.addTours(tours);
        cityMap.addObserver(observer);
        assertTrue(cityMap.removeTours(tours));
        assertFalse(cityMap.getTours().contains(tour1));
        assertFalse(cityMap.getTours().contains(tour2));
        assertTrue(updateCalled);
        assertEquals(Observable.NotifType.LIGHT_UPDATE, updateCalledType);
    }

    @Test
    void computePathTest1() {
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
    void computePathTest2() {
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
