package com.insa.lifraison;

import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.xml.ExceptionXML;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Dry test class
 */
public class IntersectionTest {

    /**
     * Tests the construction and equality test for Intersection.
     */
    @Test
    void constructorTest() {
        String id = "test";
        double longitude = 0.34;
        double latitude = 0.53;
        Intersection intersection = new Intersection(id, longitude, latitude);
        assertEquals(id, intersection.id);
        assertEquals(longitude, intersection.longitude);
        assertEquals(latitude, intersection.latitude);
    }

    @Test
    void toStringTest() {
        String id = "test";
        double longitude = 0.34;
        double latitude = 0.53;
        Intersection intersection = new Intersection(id, longitude, latitude);
        String toString = "Intersection{id='" + id + "', longitude=" + longitude + ", latitude=" + latitude + "}";
        assertEquals(toString, intersection.toString());
    }
}
