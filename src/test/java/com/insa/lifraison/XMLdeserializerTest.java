package com.insa.lifraison;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Segment;
import com.insa.lifraison.model.Warehouse;
import com.insa.lifraison.xml.ExceptionXML;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.insa.lifraison.xml.XMLdeserializer;
import org.xml.sax.SAXException;

/**
 * Dry test class
 */
public class XMLdeserializerTest {
    /**
     * Tests the construction of a CityMap from a XML document.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    @Test
    void testXMLdeserializer() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        Intersection inter1 = new Intersection("1",45,45);
        Intersection inter2 = new Intersection("2",53,50);
        Intersection inter3 = new Intersection("3",20,10);
        LinkedList<Intersection> targetIntersections = new LinkedList<>(List.of(inter1,inter2,inter3));
        Segment segment1 = new Segment(inter1, inter2,10,"Avenue Albert Einstein");
        Segment segment2 = new Segment(inter2, inter1,10,"Avenue Albert Einstein");
        Segment segment3 = new Segment(inter2, inter3,22,"Avenue des Arts");
        LinkedList<Segment> targetSegments= new LinkedList<>(List.of(segment1,segment2,segment3));
        Warehouse targetWarehouse = new Warehouse(inter2);
        CityMap targetMap = new CityMap(targetIntersections, targetSegments,targetWarehouse);

        CityMap mapFromXML = new CityMap(new LinkedList<>(), new LinkedList<>(),null);
        File XMLFile = new File("./src/test/java/com/insa/lifraison/DeserializerTest.xml");
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(XMLFile);
        Element root = document.getDocumentElement();
        if (root.getNodeName().equals("map")) {
            XMLdeserializer.buildFromDOMXML(root, mapFromXML);
        }

        assertEquals(targetMap, mapFromXML);
    }
}
