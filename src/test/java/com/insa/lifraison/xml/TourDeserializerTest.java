package com.insa.lifraison.xml;

import com.insa.lifraison.model.*;
import com.insa.lifraison.xml.CityMapDeserializer;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.TourDeserializer;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.insa.lifraison.xml.TourSerializer;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Dry test class
 */
public class TourDeserializerTest {
    /**
     * Tests the import of a Tour from a XML file.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    @Test
    void testTourDeserializer() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        ArrayList<Tour> toursSources = new ArrayList<>();

        ArrayList<Intersection> deliveryList1 = new ArrayList<>();
        deliveryList1.add(new Intersection("1",45,45));
        deliveryList1.add(new Intersection("2",53,50));
        deliveryList1.add(new Intersection("3",20,10));
        LocalTime baseTime = LocalTime.parse("09:00");
        Tour sourceTour1 = new Tour();
        for(Intersection inter : deliveryList1){
            sourceTour1.addDelivery(new DeliveryRequest(baseTime, baseTime.plusHours(1),inter));
            baseTime = baseTime.plusMinutes(30);
        }

        ArrayList<Intersection> deliveryList2 = new ArrayList<>();
        deliveryList2.add(deliveryList1.get(0));
        deliveryList2.add(deliveryList1.get(2));
        deliveryList2.add(new Intersection("4",10,10));
        LocalTime baseTime2 = LocalTime.parse("08:30");
        Tour sourceTour2 = new Tour();
        for(Intersection inter : deliveryList2){
            sourceTour2.addDelivery(new DeliveryRequest(baseTime2, baseTime2.plusHours(1),inter));
            baseTime2 = baseTime2.plusMinutes(30);
        }
        toursSources.add(sourceTour1);
        toursSources.add(sourceTour2);
        LinkedList<Intersection> cityIntersections = new LinkedList<>();
        for(Intersection i : deliveryList1){
            cityIntersections.push(i);
        }
        cityIntersections.push(deliveryList2.get(2));

        File XMLFile = new File("./src/test/java/com/insa/lifraison/xml/resources/TourTests.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        Document document = docBuilder.parse(XMLFile);
        Element root = document.getDocumentElement();
        ArrayList<Tour> tourFromXML = new ArrayList<>();
        TourDeserializer.buildFromDOMXML(root, tourFromXML, cityIntersections);
        assertEquals(tourFromXML, toursSources);
    }

    @Test
    void testTourDeserializerEmptyTime() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        ArrayList<Tour> toursSources = new ArrayList<>();

        LinkedList<Intersection> deliveryList1 = new LinkedList<>();
        deliveryList1.add(new Intersection("1",45,45));
        deliveryList1.add(new Intersection("2",53,50));
        deliveryList1.add(new Intersection("3",20,10));
        Tour sourceTour1 = new Tour();
        for(Intersection inter : deliveryList1){
            sourceTour1.addDelivery(new DeliveryRequest(inter));
        }
        toursSources.add(sourceTour1);

        File XMLFile = new File("./src/test/java/com/insa/lifraison/xml/resources/TourTestNoTime.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        Document document = docBuilder.parse(XMLFile);
        Element root = document.getDocumentElement();
        ArrayList<Tour> toursFromXML = new ArrayList<>();
        TourDeserializer.buildFromDOMXML(root, toursFromXML, deliveryList1);

        assertEquals(toursFromXML, toursSources);
    }

    @Test
    void testTourDeserializerInvalidIntersection() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {



        LinkedList<Intersection> deliveryList1 = new LinkedList<>();
        deliveryList1.add(new Intersection("1",45,45));
        deliveryList1.add(new Intersection("2",53,50));
        deliveryList1.add(new Intersection("3",20,10));

        File XMLFile = new File("./src/test/java/com/insa/lifraison/xml/resources/TourInvalidIntersection.xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        Document document = docBuilder.parse(XMLFile);
        Element root = document.getDocumentElement();
        ArrayList<Tour> tourFromXML = new ArrayList<>();
        try {
            TourDeserializer.buildFromDOMXML(root, tourFromXML, deliveryList1);
            fail("Expected exception due to negative length");
        } catch(ExceptionXML e){
            assertEquals(e.getMessage(),"The following intersection do not exist in the map: '4'.");
        }
    }
}
