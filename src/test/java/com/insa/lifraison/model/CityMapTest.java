package com.insa.lifraison.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

public class CityMapTest {
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

        assertEquals(tourSegmentList.get(0).getSegments().size(), 1);
        assertEquals(tourSegmentList.get(0).getSegments().getFirst(), segment1);

        assertEquals(tourSegmentList.get(1).getSegments().size(), 1);
        assertEquals(tourSegmentList.get(1).getSegments().getFirst(), segment2);
    }
}
