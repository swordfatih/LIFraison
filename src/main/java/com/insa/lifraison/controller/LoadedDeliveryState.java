package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.MapController;
import com.insa.lifraison.view.View;
import javafx.scene.layout.VBox;
import com.insa.lifraison.xml.CityMapDeserializer;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.TourSerializer;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

public class LoadedDeliveryState implements State{
    /**
     * load a list of deliveries from an XML file
     * @param c
     * @param m
     */
    @Override
    public void loadDeliveries(Controller c, CityMap m, View view, ListOfCommands l) {
        //try{
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File("."));
            File file = fileChooser.showOpenDialog(view.getStage());

            l.add(new LoadDeliveriesCommand(m, file));
            c.setCurrentState(c.loadedDeliveryState);
        /*} catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            System.out.println(e.getMessage());
        }*/
    }

    /**
     * Change to state addDelivery
     * @param c
     */
    @Override
    public void addDelivery(Controller c, View view) {
        view.<MapController>getController("map").informations.displayAddDeliveryInformations();
        c.setCurrentState(c.addDeliveryState1);
    }

    @Override
    public void computePlan(CityMap m, ListOfCommands l) {
        l.add(new ComputePlanCommand(m));
    }

    @Override
    public void save(CityMap m, View view){
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File("."));
            File file = fileChooser.showSaveDialog(view.getStage());

            System.out.println(file.exists());
            TourSerializer instance = TourSerializer.getInstance();
            instance.save(m.getTours(), file);
        }catch (ParserConfigurationException | ExceptionXML |TransformerException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteDelivery(Controller c, View view){
        view.<MapController>getController("map").informations.displayDeleteDeliveryInformations();
        c.setCurrentState(c.deleteDeliveryState1);
    }

    @Override
    public void undo(Controller c, CityMap m, ListOfCommands l) {
        l.undo();
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }

    @Override
    public void redo(Controller c, CityMap m, ListOfCommands l) {
        l.redo();
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }

    @Override
    public void changeMap(Controller c, View view){
        view.<MapController>getController("map").informations.displayDeleteMapInformations();
        c.setCurrentState(c.changeMapState);
    }

    @Override
    public void addTour(CityMap m, VBox container, String text, Controller controller, ListOfCommands l) {
        l.add(new AddTourCommand(m, container, text, controller));
    }
}
