package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.view.View;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.CityMapDeserializer;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class InitialState implements State {
    /**
     * load a map from an XML file
     * @param c the Controller
     * @param m the city map
     */
    @Override
    public void loadMap(Controller c, CityMap m, View view, ListOfCommands l){
        try{
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File("."));
            File file = fileChooser.showOpenDialog(view.getStage());

            CityMapDeserializer.load(m, file);
            l.reset();

            c.setCurrentState(c.loadedMapState);

            view.navigate("main");
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e){
            System.out.println(e.getMessage());
        }
    }

}
