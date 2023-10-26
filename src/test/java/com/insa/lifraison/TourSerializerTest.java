package com.insa.lifraison;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.xml.ExceptionXML;
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

/**
 * Dry test class
 */
public class TourSerializerTest {
    /**
     * Tests the exportation of a Tour to XML format.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    @Test
    void testTourSerializer() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
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
        Document testDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element tourXML = TourSerializer.getInstance().createToursElt(toursSources,testDocument);

        File XMLFile = new File("./src/test/java/com/insa/lifraison/TourTests.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        Document document = docBuilder.parse(XMLFile);
        Element root = document.getDocumentElement();

        assertTrue(tourXML.isEqualNode(root));
    }
}