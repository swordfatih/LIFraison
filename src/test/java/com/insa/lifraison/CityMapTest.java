package com.insa.lifraison;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Segment;
import com.insa.lifraison.model.Warehouse;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CityMapTest {
    /**
     * Tests the construction and equality test of CityMap.
     */
    @Test
    void TestCityMap() {
        Intersection inter1 = new Intersection("test1", 0.34, 0.53);
        Intersection inter2 = new Intersection("test2", 0.23, 0.58);
        Intersection inter3 = new Intersection("test3", 0.98, 1.43);
        Intersection inter4 = new Intersection("test4", 0.32, 1.33);

        Segment segment1 = new Segment(inter1, inter2, 23.5, "rue test");
        Segment segment2 = new Segment(inter2, inter3, 12.5, "boulevard test");
        Segment segment3 = new Segment(inter3, inter1, 24.5, "avenue test");
        Segment segment4 = new Segment(inter2, inter1, 23.5, "rue test");
        Segment segment5 = new Segment(inter4, inter1, 42, "impasse test");

        Warehouse warehouse1 = new Warehouse(inter1);
        Warehouse warehouse2 = new Warehouse(inter2);

        LinkedList<Intersection> intersections1 = new LinkedList<>(List.of(inter1,inter2,inter3));
        LinkedList<Segment> segments1 = new LinkedList<>(List.of(segment1,segment2,segment3,segment4));
        CityMap cityMap1 = new CityMap(intersections1, segments1, warehouse1);

        LinkedList<Intersection> intersections2 = new LinkedList<>(List.of(inter1,inter2,inter3));
        LinkedList<Segment> segments2 = new LinkedList<>(List.of(segment1,segment2,segment3,segment4));
        CityMap cityMap2 = new CityMap(intersections2, segments2, warehouse1);

        LinkedList<Intersection> intersections3 = new LinkedList<>(List.of(inter1,inter2,inter3,inter4));
        LinkedList<Segment> segments3 = new LinkedList<>(List.of(segment1,segment2,segment3,segment4));
        CityMap cityMap3 = new CityMap(intersections3, segments3, warehouse1);

        LinkedList<Intersection> intersections4 = new LinkedList<>(List.of(inter1,inter2,inter3,inter4));
        LinkedList<Segment> segments4 = new LinkedList<>(List.of(segment1,segment2,segment3,segment4, segment5));
        CityMap cityMap4 = new CityMap(intersections4, segments4, warehouse1);

        LinkedList<Intersection> intersections5 = new LinkedList<>(List.of(inter1,inter2,inter3,inter4));
        LinkedList<Segment> segments5 = new LinkedList<>(List.of(segment1,segment2,segment3,segment4, segment5));
        CityMap cityMap5 = new CityMap(intersections5, segments5, warehouse2);

        CityMap cityMap6 = new CityMap();

        assertEquals(cityMap1, cityMap2);
        assertNotEquals(cityMap1, cityMap3);
        assertNotEquals(cityMap1, cityMap4);
        assertNotEquals(cityMap1, cityMap5);
        assertNotEquals(cityMap1, cityMap6);

        cityMap1.addIntersection(inter4);
        assertEquals(cityMap1, cityMap3);
        assertNotEquals(cityMap1, cityMap2);
        assertNotEquals(cityMap1, cityMap4);
        assertNotEquals(cityMap1, cityMap5);
        assertNotEquals(cityMap1, cityMap6);

        cityMap1.addSegment(segment5);
        assertEquals(cityMap1, cityMap4);
        assertNotEquals(cityMap1, cityMap2);
        assertNotEquals(cityMap1, cityMap3);
        assertNotEquals(cityMap1, cityMap5);
        assertNotEquals(cityMap1, cityMap6);

        cityMap1.setWarehouse(warehouse2);
        assertEquals(cityMap1, cityMap5);
        assertNotEquals(cityMap1, cityMap2);
        assertNotEquals(cityMap1, cityMap3);
        assertNotEquals(cityMap1, cityMap4);
        assertNotEquals(cityMap1, cityMap6);

        cityMap1.reset();
        assertEquals(cityMap1, cityMap6);
        assertNotEquals(cityMap1, cityMap2);
        assertNotEquals(cityMap1, cityMap3);
        assertNotEquals(cityMap1, cityMap4);
        assertNotEquals(cityMap1, cityMap5);
    }
}
