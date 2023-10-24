package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.XMLdeserializer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class LoadedMapState implements State {
    @Override
    public void loadMap(Controller c, CityMap m){
        try{
            XMLdeserializer.load(m);
            c.setCurrentState(c.loadedMapState);
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e){
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
    public void loadDeliveries(Controller c, CityMap m) {
        try{
            XMLdeserializer.loadDeliveries(m);
            c.setCurrentState(c.loadedDeliveryState);
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            System.out.println(e.getMessage());
        }
    }
}
