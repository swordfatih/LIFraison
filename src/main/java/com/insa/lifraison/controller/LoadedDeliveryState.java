package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.XMLdeserializer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class LoadedDeliveryState implements State{
    /**
     * load a list of deliveries from an XML file
     * @param c
     * @param m
     */
    @Override
    public void loadDeliveries(Controller c, CityMap m) {
        try{
            XMLdeserializer.loadDeliveries(m);
            c.setCurrentState(c.loadedDeliveryState);
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Change to state addDelivery
     * @param c
     */
    @Override
    public void addDelivery(Controller c) {
        c.setCurrentState(c.addDeliveryState1);
    }

    @Override
    public void compute(Controller c) {
        c.setCurrentState(c.computeState1);
    }

    @Override
    public void deleteDelivery(Controller c){
        c.setCurrentState(c.deleteState1);
    }
}
