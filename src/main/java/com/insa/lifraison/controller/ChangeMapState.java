package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.View;
import com.insa.lifraison.xml.CityMapDeserializer;
import com.insa.lifraison.xml.ExceptionXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
/**
 * ChangeMapState implements a State {@link com.insa.lifraison.controller.State}
 * It is specifically used when a user want to load a new map but the CityMap contains
 * delivery request
 */
public class ChangeMapState implements State {

    @Override
    public void entryAction(CityMap m, View view) {
        view.<MainController>getController("main").getInformationController().displayDeleteMapInformation();
    }

    @Override
    public void exitAction(CityMap m, View view) {
        view.<MainController>getController("main").getInformationController().clearInformation();
    }

    /**
     * Load the file after confirm
     * @param c the Controller
     * @param m the CityMap
     * @param view the View
     * @param l the list of commands
     */
    @Override
    public void confirm(Controller c, CityMap m, View view, ListOfCommands l){
        view.<MainController>getController("main").getInformationController().clearInformation();
        try{
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showOpenDialog(view.getStage());

            if (file != null) {
                c.setMap(CityMapDeserializer.load(file));
                l.reset();
                c.setCurrentState(c.emptyMapMainState);
                view.navigate("main");
            } else {
                c.setCurrentState(c.filledMapMainState);
            }
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e){
            System.out.println(e.getMessage());

            String errorMessage = "Invalid XML:\n" + e.getMessage();
            Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage);
            alert.showAndWait();

            l.reset();
            c.setCurrentState(c.initialState);
        }
    }

    /**
     * Cancel the action
     * @param c the Controller
     * @param view the View
     */
    @Override
    public void cancel(Controller c, View view){
        c.setCurrentState(c.filledMapMainState);
    }
}