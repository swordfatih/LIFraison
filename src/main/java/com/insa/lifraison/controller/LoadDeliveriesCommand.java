package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.TourDeserializer;
import javafx.scene.control.Alert;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/**
 * LoadDeliveriesCommand is the class which does the specific command of loading deliveries
 * {@link com.insa.lifraison.model.DeliveryRequest}
 * The class extends Command {@link com.insa.lifraison.controller.Command}
 */
public class LoadDeliveriesCommand implements Command {
    private final CityMap cityMap;
    private final File file;
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

    /**
     * Add tours and deliveries
     */
    @Override
    public void doCommand() {
        try {
            tours = TourDeserializer.load(cityMap.getIntersections(), file);
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            String errorMessage = "Invalid XML:\n" + e.getMessage();
            Alert alert = new Alert(Alert.AlertType.ERROR,errorMessage);
            alert.showAndWait();
            System.out.println(errorMessage);
        }
        cityMap.addTours(tours);
    }

    /**
     * Remove tours and deliveries
     */
    @Override
    public void undoCommand() {
        cityMap.removeTours(tours);
    }
}
