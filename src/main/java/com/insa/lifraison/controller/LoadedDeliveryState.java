package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.view.MapController;
import com.insa.lifraison.view.View;
import javafx.stage.FileChooser;

import java.io.File;

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
        view.<MapController>getController("map").displayAddDeliveryInformations();
        c.setCurrentState(c.addDeliveryState1);
    }

    @Override
    public void computePlan(CityMap m, ListOfCommands l) {
        l.add(new ComputePlanCommand(m));
    }

    @Override
    public void deleteDelivery(Controller c, View view){
        view.<MapController>getController("map").displayDeleteDeliveryInformations();
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
}
