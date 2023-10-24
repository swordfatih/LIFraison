package com.insa.lifraison;

import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Warehouse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Dry test class
 */
public class WarehouseTest {
    /**
     * Tests the construction and equality test for Warehouse.
     */
    @Test
    void TestIntersection() {
        Intersection inter1 = new Intersection("test", 0.34, 0.53);
        Intersection inter2 = new Intersection("test", 0.34, 0.53);
        Intersection inter3 = new Intersection("test1", 0.23, 0.58);
        Warehouse warehouse1 = new Warehouse(inter1);
        Warehouse warehouse2 = new Warehouse(inter2);
        Warehouse warehouse3 = new Warehouse(inter3);
        assertEquals(warehouse1, warehouse2);
        assertNotEquals(warehouse1, warehouse3);
        assertEquals(warehouse1.getIntersection(), inter1);
    }
}
