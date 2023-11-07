package com.insa.lifraison.xml;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.insa.lifraison.model.*;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TourSerializer {// Singleton

    private Element shapeRoot;
    private Document document;
    private static TourSerializer instance = null;
    private TourSerializer(){}
    public static TourSerializer getInstance(){
        if (instance == null)
            instance = new TourSerializer();
        return instance;
    }

    /**
     * Open an XML file and write an XML description of the plan in it
     * @param tours A list containing the tours to serialize
     * @throws ParserConfigurationException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     * @throws ExceptionXML
     */
    public void save(LinkedList<Tour> tours, File file) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, ExceptionXML{
        StreamResult result = new StreamResult(file);
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        document.appendChild(createToursElt(tours,document));
        DOMSource source = new DOMSource(document);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xformer.transform(source, result);
    }

    public Element createToursElt(Collection<Tour> tours, Document doc) {
        Element root = doc.createElement("tours_map");
        for(Tour t : tours){
            Element tourNode = doc.createElement("tour");
            for(DeliveryRequest del : t.getDeliveries()){
                Element deliveryNode = buildDeliveryNode(del,doc);
                tourNode.appendChild(deliveryNode);
            }
            root.appendChild(tourNode);
        }
        return root;
    }

    private void createAttribute(Element root, String name, String value, Document doc){
        Attr attribute = doc.createAttribute(name);
        root.setAttributeNode(attribute);
        attribute.setValue(value);
    }

    private Element buildDeliveryNode(DeliveryRequest delRequest, Document doc) {
        Element deliveryNode = doc.createElement("delivery");

        createAttribute(deliveryNode,"destination",delRequest.getDestination().getId(),doc);
        LocalTime startTime = delRequest.getTimeWindowStart();
        String startTimeString = "";
        if(startTime!=null){
            startTimeString = startTime.toString();
        }

        LocalTime endTime = delRequest.getTimeWindowStart();
        String endTimeString = "";
        if(endTime!=null){
            endTimeString = endTime.toString();
        }
        createAttribute(deliveryNode,"time_window_start",startTimeString,doc);
        createAttribute(deliveryNode,"time_window_end",endTimeString,doc);
        return deliveryNode;
    }
}
