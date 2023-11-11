package com.insa.lifraison.model;

import com.insa.lifraison.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TourTest {

    @Test
    void AddDeliveryTest() {
        Tour tour = new Tour();
        DeliveryRequest deliveryRequest = new DeliveryRequest(new Intersection("test", 0, 1));
        tour.addDelivery(deliveryRequest);
        assertTrue(tour.getDeliveries().contains(deliveryRequest));
    }

    @Test
    void RemoveDeliveryTest1() {
        Tour tour = new Tour();
        DeliveryRequest deliveryRequest = new DeliveryRequest(new Intersection("test", 0, 1));
        tour.addDelivery(deliveryRequest);
        assertTrue(tour.removeDelivery(deliveryRequest));
        assertFalse(tour.getDeliveries().contains(deliveryRequest));
    }

    @Test
    void RemoveDeliveryTest2() {
        Tour tour = new Tour();
        DeliveryRequest deliveryRequest = new DeliveryRequest(new Intersection("test", 0, 1));
        assertFalse(tour.removeDelivery(deliveryRequest));
    }

    @Test
    void SetTourStepsTest() {
        Tour tour = new Tour();
        Intersection intersection1 = new Intersection("intersection1", 0, 0);
        Intersection intersection2 = new Intersection("intersection2", 1, 0);
        Segment segment = new Segment(intersection1, intersection2, 1, "segment");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment));
        LocalTime departure = LocalTime.of(10, 30);
        LocalTime arrival = LocalTime.of(11, 30);
        TourStep tourStep = new TourStep(segments, departure, arrival);
        LinkedList<TourStep> tourSteps = new LinkedList<>(List.of(tourStep));
        tour.setTourSteps(tourSteps);
        assertEquals(tourSteps, tour.getTourSteps());
    }
}
