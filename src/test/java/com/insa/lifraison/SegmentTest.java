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
    void TestSegment() {
        Intersection inter1 = new Intersection("test1", 0.34, 0.53);
        Intersection inter2 = new Intersection("test1", 0.34, 0.53);
        Intersection inter3 = new Intersection("test2", 0.23, 0.58);
        Intersection inter4 = new Intersection("test2", 0.23, 0.58);
        Intersection inter5 = new Intersection("test3", 0.98, 1.43);
        Intersection inter6 = new Intersection("test4", 0.32, 1.33);

        Segment segment1 = new Segment(inter1, inter3, 23.5, "rue test");
        Segment segment2 = new Segment(inter2, inter4, 23.5, "rue test");
        Segment segment3 = new Segment(inter5, inter6, 24.5, "avenue test");
        Segment segment4 = new Segment(inter3, inter1, 23.5, "rue test");

        assertEquals(segment1, segment2);
        assertNotEquals(segment1, segment3);
        assertNotEquals(segment1, segment4);
    }
}
