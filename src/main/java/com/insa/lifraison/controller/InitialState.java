package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.XMLdeserializer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class InitialState implements State {
    @Override
    public void loadMap(Controller c, CityMap m){
        try{
            XMLdeserializer.load(m);
            c.setCurrentState(c.loadedMapState);
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e){
            System.out.println(e.getMessage());
        }
    }

}
