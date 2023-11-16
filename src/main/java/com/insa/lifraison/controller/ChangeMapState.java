package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.View;
import com.insa.lifraison.xml.CityMapDeserializer;
import com.insa.lifraison.xml.ExceptionXML;
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

    @Override
    public void confirm(Controller c, CityMap m, View view, ListOfCommands l){
        view.<MainController>getController("main").getInformationController().clearInformation();
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
            } else {
                c.setCurrentState(c.filledMapMainState);
            }
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e){
            System.out.println(e.getMessage());
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