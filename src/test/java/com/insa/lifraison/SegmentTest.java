package com.insa.lifraison;

import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Segment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SegmentTest {
    /**
     * Tests the construction and equality test of Segment.
     */
    @Test
    void constructorTest() {
        Intersection origin = new Intersection("origin", 0.34, 0.53);
        Intersection destination = new Intersection("destination", 0.24, 0.12);
        double length = 2.3;
        String name = "segment";

        Segment segment = new Segment(origin, destination, length, name);

        assertEquals(origin, segment.origin);
        assertEquals(destination, segment.destination);
        assertEquals(length, segment.length);
        assertEquals(name, segment.name);
    }

    @Test
    void toStringTest() {
        Intersection origin = new Intersection("origin", 0.34, 0.53);
        Intersection destination = new Intersection("destination", 0.24, 0.12);
        double length = 2.3;
        String name = "segment";

        Segment segment = new Segment(origin, destination, length, name);

        String toString = "Segment{origin=" + origin + ", destination=" + destination + ", length=" + length + ", name='" + name + "'}";
        assertEquals(toString, segment.toString());
    }
}
