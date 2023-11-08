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
    void constructorTest() {
        Intersection intersection = new Intersection("intersection", 0.32, 0.43);
        Warehouse warehouse = new Warehouse(intersection);

        assertEquals(intersection, warehouse.intersection);
    }
}
