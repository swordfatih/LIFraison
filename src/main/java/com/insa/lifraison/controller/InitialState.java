package com.insa.lifraison.controller;

import com.insa.lifraison.view.View;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.CityMapDeserializer;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import javafx.scene.control.Alert;

public class InitialState implements State {
    /**
     * open the file chooser and load the map from the selected file
     * @param c the app controller
     * @param view the app view
     * @param l the list of commands for the undo/redo
     */
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
                c.setCurrentState(c.emptyMapMainState);
                view.navigate("main");
            }
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e){
            String errorMessage = "Invalid XML:\n" + e.getMessage();
            Alert alert = new Alert(Alert.AlertType.ERROR,errorMessage);
            alert.showAndWait();
            System.out.println(errorMessage);
        }
    }
}