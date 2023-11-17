package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.View;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.TourDeserializer;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
/**
 * NoDeliveriesMainState extends a State {@link com.insa.lifraison.controller.EmptyMapMainState}
 * It is the main second state. It corresponds to the state where the user has loaded a map but any deliveries yet.
 */
public class NoDeliveriesMainState extends EmptyMapMainState {

    @Override
    public void entryAction(CityMap m, View view) {
        super.entryAction(m, view);
        view.<MainController>getController("main").enableButton("#removeTourButton");

    }

    @Override
    public void exitAction(CityMap m, View view) {
        super.exitAction(m, view);
        view.<MainController>getController("main").disableButton("#removeTourButton");

    }

    /**
     * Go to ChangeMapState
     * @param c the Controller
     * @param m the CityMap
     * @param view the View
     * @param l the list of commands
     */
    @Override
    public void loadMap(Controller c, CityMap m, View view, ListOfCommands l) {
        c.setCurrentState(c.changeMapState);
    }

    /**
     * Load deliveries
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
                for (Tour tour : TourDeserializer.load(m.getIntersections(), file)) {
                    loadDeliveriesCommand.addCommand(new AddTourCommand(m, tour));
                }
                l.add(loadDeliveriesCommand);
                c.setCurrentStateToMain();
            }
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            String errorMessage = "Invalid XML:\n" + e.getMessage();
            Alert alert = new Alert(Alert.AlertType.ERROR,errorMessage);
            alert.showAndWait();

            System.out.println(e.getMessage());
        }
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
    }

    /**
     * Go to state DeleteTourState
     * @param c the Controller
     * @param m the CityMap
     */
    @Override
    public void removeTour(Controller c, CityMap m){
        c.setCurrentState(c.deleteTourState);
    }
}
