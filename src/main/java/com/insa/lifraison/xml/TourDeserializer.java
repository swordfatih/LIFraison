package com.insa.lifraison.xml;

import java.time.LocalTime;
import java.util.*;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;

public class TourDeserializer {
    public static ArrayList<Tour> load(LinkedList<Intersection> map_intersections, File file) throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        ArrayList<Tour> tours = new ArrayList<>();
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(file);
        Element root = document.getDocumentElement();
        if (root.getNodeName().equals("tours_map")) {
            buildFromDOMXML(root, tours, map_intersections);
        } else
            throw new ExceptionXML("Wrong format");

        return tours;
    }

    /**
     * Builds a CityMap from a XML node tree.
     *
     * @param rootDOMNode The root of the XML file, which must be a <code>map</code> tag.
     * @param tours       The list of tours that will hold the information.
     * @throws ExceptionXML
     * @throws NumberFormatException
     */
    public static void buildFromDOMXML(Element rootDOMNode, ArrayList<Tour> tours, LinkedList<Intersection> map_intersections) throws ExceptionXML, NumberFormatException {

        NodeList tourList = rootDOMNode.getElementsByTagName("tour");
        for (int i = 0; i < tourList.getLength(); i++) {
            Tour newTour = createTour((Element) tourList.item(i), map_intersections);
            tours.add(newTour);
        }
    }

    private static Tour createTour(Element tourRoot, LinkedList<Intersection> map_intersections) throws ExceptionXML {
        NodeList deliveriesList = tourRoot.getElementsByTagName("delivery");
        Tour result = new Tour();
        for (int i = 0; i < deliveriesList.getLength(); i++) {
            DeliveryRequest request = createDeliveryRequest((Element) deliveriesList.item(i), map_intersections);
            result.addDelivery(request);
        }
        return result;
    }

    private static DeliveryRequest createDeliveryRequest(Element deliveryNode, LinkedList<Intersection> map_intersections) {
        LocalTime timeWindowStart = LocalTime.parse(deliveryNode.getAttribute("time_window_start"));
        LocalTime timeWindowEnd = LocalTime.parse(deliveryNode.getAttribute("time_window_end"));
        String intersection_id = deliveryNode.getAttribute("destination");
        Optional<Intersection> intersectionOption = map_intersections.stream().filter(x -> x.id.equals(intersection_id)).findAny();
        Intersection destination = null;
        if (intersectionOption.isPresent()) {
            destination = intersectionOption.get();
        }
        return new DeliveryRequest(timeWindowStart, timeWindowEnd, destination);
    }
}
