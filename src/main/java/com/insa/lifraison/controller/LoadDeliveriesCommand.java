package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.TourDeserializer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LoadDeliveriesCommand implements Command {
    private CityMap cityMap;
    private File file;
    private ArrayList<Tour> tours;

    /**
     * Create the command which adds Tours from a file to a CityMap
     * @param cityMap the map to which tours are added
     * @param file file from which the tours added to cityMap are read
     */
    LoadDeliveriesCommand(CityMap cityMap, File file){
        this.cityMap = cityMap;
        this.file = file;
        tours = new ArrayList<>();
    }

    @Override
    public void doCommand() {
        try {
            tours = TourDeserializer.load(cityMap.getIntersections(), file);
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            //TODO: handle error properly
            System.out.println(e.getMessage());
        }
        cityMap.addTours(tours);
    }

    @Override
    public void undoCommand() {
        cityMap.removeTours(tours);
    }
}
