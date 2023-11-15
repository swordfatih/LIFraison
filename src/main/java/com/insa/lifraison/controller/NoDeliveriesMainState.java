package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.View;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.TourDeserializer;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class NoDeliveriesMainState extends EmptyMapMainState {

    @Override
    public void loadMap(Controller c, CityMap m, View view, ListOfCommands l) {
        c.setCurrentState(c.changeMapState);
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
                for (Tour tour : TourDeserializer.load(m.getIntersections(), file)) {
                    loadDeliveriesCommand.addCommand(new AddTourCommand(m, tour));
                }
                l.add(loadDeliveriesCommand);
                c.setCurrentStateToMain();
            }
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addTour(Controller c, CityMap m, ListOfCommands l) {
        l.add(new AddTourCommand(m, new Tour()));
    }

    @Override
    public void removeTour(Controller c, CityMap m){
        c.setCurrentState(c.deleteTourState);
    }
}
