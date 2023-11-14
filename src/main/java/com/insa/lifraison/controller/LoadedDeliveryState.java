package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.MenuController;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.View;
import com.insa.lifraison.xml.TourDeserializer;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.TourSerializer;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

public class LoadedDeliveryState implements State{
    @Override
    public void loadDeliveries(Controller c, CityMap m, View view, ListOfCommands l) {
        m.clearSelection();
        try{
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File("src/main/resources/tours"));
            File file = fileChooser.showOpenDialog(view.getStage());

            if (file != null) {
                CompoundCommand loadDeliveriesCommand = new CompoundCommand();
                for (Tour tour : TourDeserializer.load(m.getIntersections(), file)) {
                    loadDeliveriesCommand.addCommand(new AddTourCommand(m, tour));
                }
                l.add(loadDeliveriesCommand);
                c.setCurrentState(c.loadedDeliveryState);
            }
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addDelivery(Controller c, CityMap m, View view) {
        m.clearSelection();
        view.<MainController>getController("main").getInformationController().displayAddDeliveryInformations();
        c.setCurrentState(c.addDeliveryState1);
    }

    @Override
    public void computePlan(CityMap m, ListOfCommands l) {
        m.clearSelection();
        l.add(new ComputePlanCommand(m));
    }

    @Override
    public void save(CityMap m, View view){
        m.clearSelection();
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File("src/main/resources/maps"));
            File file = fileChooser.showSaveDialog(view.getStage());

            if (file != null) {
                System.out.println(file.exists());
                TourSerializer instance = TourSerializer.getInstance();
                instance.save(m.getTours(), file);
            }
        }catch (ParserConfigurationException | ExceptionXML |TransformerException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteDelivery(Controller c, CityMap m, View view){
        m.clearSelection();
        view.<MainController>getController("main").getInformationController().displayDeleteDeliveryInformations();
        c.setCurrentState(c.deleteDeliveryState1);
    }

    @Override
    public void undo(Controller c, CityMap m, ListOfCommands l) {
        m.clearSelection();
        l.undo();
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }

    @Override
    public void redo(Controller c, CityMap m, ListOfCommands l) {
        m.clearSelection();
        l.redo();
        if (m.getNumberDeliveries() != 0){
            c.setCurrentState(c.loadedDeliveryState);
        } else {
            c.setCurrentState(c.loadedMapState);
        }
    }

    @Override
    public void changeMap(Controller c, View view){
        view.<MainController>getController("main").getInformationController().displayDeleteMapInformations();
        c.setCurrentState(c.changeMapState);
    }

    @Override
    public void addTour(CityMap m, ListOfCommands l) {
        m.clearSelection();
        l.add(new AddTourCommand(m, new Tour()));
    }

    @Override
    public void removeTour(Controller c, CityMap m){
        m.clearSelection();
        c.setCurrentState(c.deleteTourState);
    }

    @Override
    public void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, Tour t, ListOfCommands l){
        System.out.println("left click");
        if (d != null) {
            m.selectComponent(d);
        }
    }

    @Override
    public void rightClick(Controller c, CityMap m, View view, ListOfCommands l){
        m.clearSelection();
    }

    @Override
    public void tourButtonClicked(Controller c, CityMap m, Tour t, View v, ListOfCommands l) {
        m.selectComponent(t);
    }
}
