package com.insa.lifraison.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.insa.lifraison.model.*;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.TourDeserializer;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;

import com.insa.lifraison.xml.Roadmap;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


public class RoadmapTest {
    @Test
    void testRoadmap() throws ExceptionXML, ParserConfigurationException, TransformerException, IOException, SAXException {
       Tour tour = new Tour();
       // Create intersections
        ArrayList<Intersection> intersectionList = new ArrayList<>();
        for(Integer i = 1; i<=5;i++){
            intersectionList.add(new Intersection(i.toString(),0,0));
        }
        //Create tours steps
        LinkedList<Segment> segmentList1 = new LinkedList<>();
        segmentList1.add(new Segment(intersectionList.get(0),intersectionList.get(1), 100,"Road A"));
        segmentList1.add(new Segment(intersectionList.get(1),intersectionList.get(2), 100,"Road A"));

        LinkedList<Segment> segmentList2 = new LinkedList<>();
        segmentList2.add(new Segment(intersectionList.get(2),intersectionList.get(3), 100,"Road B"));
        segmentList2.add(new Segment(intersectionList.get(3),intersectionList.get(4), 100,"Road C"));

        LinkedList<Segment> segmentList3 = new LinkedList<>();
        segmentList3.add(new Segment(intersectionList.get(4),intersectionList.get(3), 100,"Road C"));
        segmentList3.add(new Segment(intersectionList.get(3),intersectionList.get(0), 100,"Road C"));

        LinkedList<TourStep> tourSteps = new LinkedList<>();

        tourSteps.add(new TourStep(segmentList1, LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        tourSteps.add(new TourStep(segmentList2, LocalTime.parse("09:30"), LocalTime.parse("10:00")));
        tourSteps.add(new TourStep(segmentList3, LocalTime.parse("10:00"), LocalTime.parse("11:00")));

        tour.setTourSteps(tourSteps);
        Document testDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element roadmap = Roadmap.createRoadmapElt(tour, testDocument);

        File htmlFile = new File("./src/test/java/com/insa/lifraison/xml/resources/RouteTest.html");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        Document document = docBuilder.parse(htmlFile);
        Element root = document.getDocumentElement();

        assertTrue(roadmap.isEqualNode(root));
    }
}
