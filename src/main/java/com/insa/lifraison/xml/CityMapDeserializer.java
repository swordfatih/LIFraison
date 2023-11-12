package com.insa.lifraison.xml;

import java.util.HashMap;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Segment;
import com.insa.lifraison.model.Warehouse;
import com.insa.lifraison.model.CityMap;


public class CityMapDeserializer {
    /**
     * Open an XML file and create CityMap from this file.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public static CityMap load(File file) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(file);
        Element root = document.getDocumentElement();
        if (root.getNodeName().equals("map")) {
            return buildFromDOMXML(root);
        }
        else
            throw new ExceptionXML("Wrong format");
    }

    public static void loadDeliveries(CityMap map) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
        ///TODO a implementer
    }

    /**
     * Builds a CityMap from a XML node tree.
     * @param rootDOMNode The root of the XML file, which must be a <code>map</code> tag.
     * @param map The map that will hold the information.
     * @throws ExceptionXML
     * @throws NumberFormatException
     */
    public static CityMap buildFromDOMXML(Element rootDOMNode) throws ExceptionXML, NumberFormatException{

        CityMap map = new CityMap();

        NodeList IntersectionList = rootDOMNode.getElementsByTagName("intersection");
        HashMap<String, Intersection> intersectionMap = new HashMap<>();
        LinkedList<Intersection> intersections = new LinkedList<>();
        for (int i = 0; i < IntersectionList.getLength(); i++) {
            Intersection currentIntersection = createIntersection((Element) IntersectionList.item(i));
            intersectionMap.put(currentIntersection.id, currentIntersection);
            intersections.add(currentIntersection);
        }
        NodeList segmentNodeList = rootDOMNode.getElementsByTagName("segment");
        LinkedList<Segment> segmentList = new LinkedList<>();
        for (int i = 0; i < segmentNodeList.getLength(); i++) {
            Segment currentSegment = createSegment((Element) segmentNodeList.item(i), intersectionMap);
            segmentList.add(currentSegment);
        }
        NodeList warehouseNodes = rootDOMNode.getElementsByTagName("warehouse");
        Warehouse warehouse= createWarehouse((Element) warehouseNodes.item(0),intersectionMap);
        map.setIntersectionsSegmentsWarehouse(intersections, segmentList, warehouse);
        return map;
    }
    private static Intersection createIntersection(Element elt) throws ExceptionXML{
        String id = elt.getAttribute("id");
        double latitude = Double.parseDouble(elt.getAttribute("latitude"));
        double longitude = Double.parseDouble(elt.getAttribute("longitude"));
        return new Intersection(id,longitude, latitude);
    }

    private static Segment createSegment(Element elt, HashMap<String, Intersection> intersections) throws ExceptionXML{
        String origin = elt.getAttribute("origin");
        String destination = elt.getAttribute("destination");
        String name = elt.getAttribute("name");
        String lengthString = elt.getAttribute("length");
        try {
            double length = Double.parseDouble(lengthString);
            if (length < 0.0) {
                throw new ExceptionXML("Length cannot be negative: '" + length + "'.");
            }
            if (!intersections.containsKey(origin)) {
                throw new ExceptionXML("Unknown intersection origin: '" + origin + "'.");
            }
            if (!intersections.containsKey(destination)) {
                throw new ExceptionXML("Unknown intersection destination: '" + destination + "'.");
            }
            return new Segment(intersections.get(origin), intersections.get(destination), length, name);
        }
        catch(NumberFormatException e){
            throw new ExceptionXML("Length is not a number: '" + lengthString + "'.");
        }
    }

    private static Warehouse createWarehouse(Element elt, HashMap<String, Intersection> intersections) throws ExceptionXML{
       String id = elt.getAttribute("address");
       if(intersections.containsKey(id)) {
           return new Warehouse(intersections.get(id));
       }
       throw new ExceptionXML("Unknown warehouse address: '" + id + "'.");
    }

}