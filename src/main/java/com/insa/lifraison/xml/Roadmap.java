package com.insa.lifraison.xml;

import com.insa.lifraison.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Collection;

public class Roadmap {
    public static void save(Tour tour, File file) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, ExceptionXML{
        StreamResult result = new StreamResult(file);
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootNode = document.createElement("html");
        rootNode.appendChild(createTag("h1","Roadmap", document));
        rootNode.appendChild(createRoadmapElt(tour, document));
        document.appendChild(rootNode);
        DOMSource source = new DOMSource(document);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        xformer.transform(source, result);
    }

    public static Element createRoadmapElt(Tour tour, Document doc) {
        Element root = doc.createElement("div");
        LocalTime previousArrival = null;
        int stepIndex = 0;
        for(TourStep step : tour.getTourSteps()){
            stepIndex++;
            if(!(previousArrival == null) && !step.departure.equals(previousArrival)){
                Element waitTag = createTag("p", "Wait for " + previousArrival.until(step.departure, ChronoUnit.MINUTES) + " minutes.", doc);
                root.appendChild(waitTag);
            }
            String currentRoadName = null;
            double distance = 0;
            for(Segment segment : step.segments){
               if(!(currentRoadName == null) && !segment.name.equals(currentRoadName)){
                   Element roadNode = createTag("p",describeSegment(currentRoadName,distance),doc);
                   root.appendChild(roadNode);
                   distance = 0;
               }
               distance+=segment.length;
               currentRoadName = segment.name;
            }
            Element roadNode = createTag("p",describeSegment(currentRoadName,distance),doc);
            root.appendChild(roadNode);

            if(stepIndex != tour.getTourSteps().size()){
                Element deliveryNode = createTag("p","Deliver the package at " + step.arrival + "." ,doc);
                root.appendChild(deliveryNode);
            }
            else{
                Element deliveryNode = createTag("p","You arrive at the warehouse at " + step.arrival + "." ,doc);
                root.appendChild(deliveryNode);
            }

            previousArrival = step.arrival;
        }
        return root;
    }

    private static Element createTag(String tagName, String text, Document doc){
       Element tagNode = doc.createElement(tagName);
       Text tagText = doc.createTextNode(text);
       tagNode.appendChild(tagText);
       return tagNode;
    }

    private static String describeSegment(String roadName, double distance){
        return "Drive " + (int) distance + " meters in " + roadName+".";
    }
}
