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
    void TestIntersection() {
        Intersection inter1 = new Intersection("test", 0.34, 0.53);
        Intersection inter2 = new Intersection("test", 0.34, 0.53);
        Intersection inter3 = new Intersection("test1", 0.23, 0.58);
        assertEquals(inter1, inter2);
        assertNotEquals(inter1, inter3);
        assertEquals(inter1.getId(),"test");
    }
}
