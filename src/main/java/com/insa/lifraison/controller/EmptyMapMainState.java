package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.View;
import com.insa.lifraison.xml.CityMapDeserializer;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.TourDeserializer;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
/**
 * EmptyMapMainState implements a State {@link com.insa.lifraison.controller.State}
 * It is the main first state when the user hasn't loaded a map yet.
 * The CityMap is empty.
 */
public class EmptyMapMainState implements State {


    @Override
    public void entryAction(CityMap m, View view) {
        view.<MainController>getController("main").enableButton("#loadMapButton");
        view.<MainController>getController("main").enableButton("#addDeliveryButton");
        view.<MainController>getController("main").enableButton("#loadDeliveriesButton");
        view.<MainController>getController("main").enableButton("#undoButton");
        view.<MainController>getController("main").enableButton("#redoButton");
        view.<MainController>getController("main").enableButton("#addTourButton");


    }
    @Override
    public void exitAction(CityMap m, View view) {
        m.clearSelection();

        view.<MainController>getController("main").disableButton("#loadMapButton");
        view.<MainController>getController("main").disableButton("#addDeliveryButton");
        view.<MainController>getController("main").disableButton("#loadDeliveriesButton");
        view.<MainController>getController("main").disableButton("#undoButton");
        view.<MainController>getController("main").disableButton("#redoButton");
        view.<MainController>getController("main").disableButton("#addTourButton");
    }

    /**
     * Load a new map
     * @param c the Controller
     * @param m the CityMap
     * @param view the View
     * @param l the list of commands
     */
    @Override
    public void loadMap(Controller c, CityMap m, View view, ListOfCommands l){
        m.clearSelection();
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
            }

            view.navigate("main");
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e){
            String errorMessage = "Invalid XML:\n" + e.getMessage();
            Alert alert = new Alert(Alert.AlertType.ERROR,errorMessage);
            alert.showAndWait();
            System.out.println(errorMessage);
        }
    }

    /**
     * Go to addDeliveryState1
     * @param c the Controller
     * @param m the CityMap
     * @param view the View
     */
    @Override
    public void addDelivery(Controller c, CityMap m, View view) {
        c.setCurrentState(c.addDeliveryState1);
    }

    /**
     * Load deliveries from a file
     * @param c the Controller
     * @param m the CityMap
     * @param view the View
     * @param l the list of commands
     */
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
                loadDeliveriesCommand.addCommand(new ReverseCommand(new AddTourCommand(m, m.getTours().get(0))));
                for (Tour tour : TourDeserializer.load(m.getIntersections(), file)) {
                    loadDeliveriesCommand.addCommand(new AddTourCommand(m, tour));
                }
                l.add(loadDeliveriesCommand);
                c.setCurrentStateToMain();
            }
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();

            System.out.println(e.getMessage());
        }
    }

    /**
     * undo the last command
     * @param c the controller
     * @param m the city map
     * @param l the list of command
     */
    @Override
    public void undo(Controller c, CityMap m, ListOfCommands l) {
        m.clearSelection();
        l.undo();
        c.setCurrentStateToMain();
    }

    /**
     * redo the last command
     * @param c the controller
     * @param m the city map
     * @param l the list of command
     */
    @Override
    public void redo(Controller c, CityMap m, ListOfCommands l) {
        m.clearSelection();
        l.redo();
        c.setCurrentStateToMain();
    }

    /**
     * Add a tour
     * @param c the Controller
     * @param m the CityMap
     * @param l the list of commands
     */
    @Override
    public void addTour(Controller c, CityMap m, ListOfCommands l) {
        l.add(new AddTourCommand(m, new Tour()));
        c.setCurrentState(c.noDeliveriesMainState);
    }


    /**
     * Clear the selection
     * @param c the Controller
     * @param m the CityMap
     * @param view the View
     * @param l the list of commands
     */
    @Override
    public void rightClick(Controller c, CityMap m, View view, ListOfCommands l){
        m.clearSelection();
    }

    /**
     * Select the component clicked
     * @param c the Controller
     * @param m the CityMap
     * @param l the list of commands
     */
    @Override
    public void tourButtonClicked(Controller c, CityMap m, Tour t, View v, ListOfCommands l) {
        m.selectComponent(t);
    }
}
