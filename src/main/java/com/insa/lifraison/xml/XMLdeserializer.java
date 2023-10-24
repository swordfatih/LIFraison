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

import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.XMLfileOpener;


public class XMLdeserializer {
    /**
     * Open an XML file and create CityMap from this file.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public static void load(CityMap map) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
        File xml = XMLfileOpener.getInstance().open(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(xml);
        Element root = document.getDocumentElement();
        if (root.getNodeName().equals("map")) {
            buildFromDOMXML(root, map);
        }
        else
            throw new ExceptionXML("Wrong format");
    }

    /**
     * Builds a CityMap from a XML node tree.
     * @param rootDOMNode The root of the XML file, which must be a <code>map</code> tag.
     * @param map The map that will hold the information.
     * @throws ExceptionXML
     * @throws NumberFormatException
     */
    public static void buildFromDOMXML(Element rootDOMNode, CityMap map) throws ExceptionXML, NumberFormatException{

        map.reset();

        NodeList IntersectionList = rootDOMNode.getElementsByTagName("intersection");
        HashMap<String, Intersection> intersectionMap = new HashMap<>();
        for (int i = 0; i < IntersectionList.getLength(); i++) {
            Intersection currentIntersection = createIntersection((Element) IntersectionList.item(i));
            intersectionMap.put(currentIntersection.getId(), currentIntersection);
            map.addIntersection(currentIntersection);
        }
        NodeList segmentNodeList = rootDOMNode.getElementsByTagName("segment");
        LinkedList<Segment> segmentList = new LinkedList<>();
        for (int i = 0; i < segmentNodeList.getLength(); i++) {
            Segment currentSegment = createSegment((Element) segmentNodeList.item(i), intersectionMap);
            map.addSegment(currentSegment);
        }
        NodeList warehouseNodes = rootDOMNode.getElementsByTagName("warehouse");
        map.setWarehouse(createWarehouse((Element) warehouseNodes.item(0),intersectionMap));
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
        double length = Double.parseDouble(elt.getAttribute("length"));
        return new Segment(intersections.get(origin), intersections.get(destination),length,name);
    }

    private static Warehouse createWarehouse(Element elt, HashMap<String, Intersection> intersections) throws ExceptionXML{
       String id = elt.getAttribute("address");
       return new Warehouse(intersections.get(id));
    }

}