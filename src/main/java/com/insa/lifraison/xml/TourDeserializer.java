package com.insa.lifraison.xml;

import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Tour;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

public class TourDeserializer {
    /**
     * Open an XML file and create a CityMap from this file.
     * @param map_intersections The list of intersections of the city map
     * @param file The file containing the CityMap.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
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

    private static DeliveryRequest createDeliveryRequest(Element deliveryNode, LinkedList<Intersection> map_intersections) throws ExceptionXML {
        String timeWindowStartAttribute = deliveryNode.getAttribute("time_window_start");
        LocalTime timeWindowStart = null;
        if(!timeWindowStartAttribute.isEmpty()){
            try {
                timeWindowStart = LocalTime.parse(timeWindowStartAttribute);
            }catch(DateTimeParseException e){
                throw new ExceptionXML("Invalid start for time window: '" + timeWindowStartAttribute + "'.");
            }
        }
        String timeWindowEndAttribute = deliveryNode.getAttribute("time_window_end");
        LocalTime timeWindowEnd = null;
        if(!timeWindowEndAttribute.isEmpty()){
            try {
                timeWindowEnd = LocalTime.parse(timeWindowEndAttribute);
            }catch(DateTimeParseException e) {
                throw new ExceptionXML("Invalid ending for time window: '" + timeWindowEndAttribute + "'.");
            }
        }
        String intersection_id = deliveryNode.getAttribute("destination");
        Optional<Intersection> intersectionOption = map_intersections.stream().filter(x -> x.id.equals(intersection_id)).findAny();
        if (intersectionOption.isPresent()) {
            Intersection destination = intersectionOption.get();
            return new DeliveryRequest(timeWindowStart, timeWindowEnd, destination);
        }
        throw new ExceptionXML("The following intersection do not exist in the map: '" + intersection_id + "'.");
    }
}
