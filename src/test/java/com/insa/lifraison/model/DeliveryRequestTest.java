package com.insa.lifraison.model;

import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DeliveryRequestTest {

    @Test
    void constructorTest1() {
        Intersection intersection = new Intersection("test", 0.4, 0.6);
        DeliveryRequest deliveryRequest = new DeliveryRequest(intersection);
        assertEquals(intersection, deliveryRequest.getDestination());
        assertNull(deliveryRequest.getTimeWindowStart());
        assertNull(deliveryRequest.getTimeWindowEnd());
    }

    @Test
    void constructorTest2() {
        Intersection intersection = new Intersection("test", 0.4, 0.6);
        LocalTime timeWindowStart = LocalTime.of(10, 30);
        LocalTime timeWindowEnd = LocalTime.of(11, 30);
        DeliveryRequest deliveryRequest = new DeliveryRequest(timeWindowStart, timeWindowEnd, intersection);
        assertEquals(intersection, deliveryRequest.getDestination());
        assertEquals(timeWindowStart, deliveryRequest.getTimeWindowStart());
        assertEquals(timeWindowEnd, deliveryRequest.getTimeWindowEnd());
    }
    @Test
    void setDestinationTest() {
        DeliveryRequest deliveryRequest = new DeliveryRequest(null);
        Intersection intersection = new Intersection("test", 0.4, 0.6);
        deliveryRequest.setDestination(intersection);
        assertEquals(intersection, deliveryRequest.getDestination());
    }

    @Test
    void setTimeWindowStartTest() {
        DeliveryRequest deliveryRequest = new DeliveryRequest(null);
        LocalTime timeWindowStart = LocalTime.of(10, 30);
        deliveryRequest.setTimeWindowStart(timeWindowStart);
        assertEquals(timeWindowStart, deliveryRequest.getTimeWindowStart());
    }

    @Test
    void setTimeWindowEndTest() {
        DeliveryRequest deliveryRequest = new DeliveryRequest(null);
        LocalTime timeWindowEnd = LocalTime.of(10, 30);
        deliveryRequest.setTimeWindowEnd(timeWindowEnd);
        assertEquals(timeWindowEnd, deliveryRequest.getTimeWindowEnd());
    }


}
