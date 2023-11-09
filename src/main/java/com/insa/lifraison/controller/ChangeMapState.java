package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.view.MapController;
import com.insa.lifraison.view.View;
import com.insa.lifraison.xml.CityMapDeserializer;
import com.insa.lifraison.xml.ExceptionXML;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ChangeMapState implements State {
    @Override
    public void loadMap(Controller c, CityMap m, View view, ListOfCommands l){
        view.<MapController>getController("map").informations.clearInformations();
        try{
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File("."));
            File file = fileChooser.showOpenDialog(view.getStage());

            CityMapDeserializer.load(m, file);
            l.reset();
            c.setCurrentState(c.loadedMapState);

            view.navigate("map");
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e){
            System.out.println(e.getMessage());
            l.reset();
            c.setCurrentState(c.initialState);
        }
    }

    @Override
    public void cancel(Controller c, View view){
        view.<MapController>getController("map").informations.clearInformations();
        c.setCurrentState(c.loadedDeliveryState);
    }


}
