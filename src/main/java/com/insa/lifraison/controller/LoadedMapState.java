package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.View;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.CityMapDeserializer;
import com.insa.lifraison.xml.TourDeserializer;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class LoadedMapState implements State {
    @Override
    public void loadMap(Controller c, View view, ListOfCommands l){
        try{
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File("src/main/resources/maps"));
            File file = fileChooser.showOpenDialog(view.getStage());

            if (file != null) {
                c.setMap(CityMapDeserializer.load(file));
                l.reset();
                c.setCurrentState(c.loadedMapState);
            }

            view.navigate("main");
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e){
            String errorMessage = "Invalid XML:\n" + e.getMessage();
            Alert alert = new Alert(Alert.AlertType.ERROR,errorMessage);
            alert.showAndWait();
            System.out.println(errorMessage);
        }
    }

    @Override
    public void addDelivery(Controller c, CityMap m, View view) {
        m.clearSelection();
        view.<MainController>getController("main").getInformationController().displayAddDeliveryInformations();
        c.setCurrentState(c.addDeliveryState1);
    }

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
                if (m.getTours().size() == 1) {
                    loadDeliveriesCommand.addCommand(new ReverseCommand(new AddTourCommand(m, m.getTours().get(0))));
                }
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
    public void addTour(CityMap m, ListOfCommands l) {
        l.add(new AddTourCommand(m, new Tour()));
    }

    @Override
    public void removeTour(Controller c, CityMap m){
        if(m.getTours().size() > 1)
            c.setCurrentState(c.deleteTourState);
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
