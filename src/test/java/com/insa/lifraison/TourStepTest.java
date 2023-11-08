package com.insa.lifraison;

import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Segment;
import com.insa.lifraison.model.TourStep;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TourStepTest {

    @Test
    void constructorTest() {
        Intersection intersection1 = new Intersection("intersection1", 0, 0);
        Intersection intersection2 = new Intersection("intersection2", 1, 0);
        Intersection intersection3 = new Intersection("intersection3", 1, 1);
        Segment segment1 = new Segment(intersection1, intersection2, 1, "segment1");
        Segment segment2 = new Segment(intersection2, intersection3, 1.5, "segment2");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment1, segment2));
        LocalTime departure = LocalTime.of(10, 30);
        LocalTime arrival = LocalTime.of(11, 30);
        TourStep tourStep = new TourStep(segments, departure, arrival);
        assertEquals(segments, tourStep.segments);
        assertEquals(departure, tourStep.departure);
        assertEquals(arrival, tourStep.arrival);
    }

    @Test
    void getLengthTest() {
        Intersection intersection1 = new Intersection("intersection1", 0, 0);
        Intersection intersection2 = new Intersection("intersection2", 1, 0);
        Intersection intersection3 = new Intersection("intersection3", 1, 1);
        double length1 = 1.0;
        double length2 = 1.5;
        double length = length1 + length2;
        Segment segment1 = new Segment(intersection1, intersection2, length1, "segment1");
        Segment segment2 = new Segment(intersection2, intersection3, length2, "segment2");
        LinkedList<Segment> segments = new LinkedList<>(List.of(segment1, segment2));
        LocalTime departure = LocalTime.of(10, 30);
        LocalTime arrival = LocalTime.of(11, 30);
        TourStep tourStep = new TourStep(segments, departure, arrival);
        assertEquals(length, tourStep.getLength());
    }
}
