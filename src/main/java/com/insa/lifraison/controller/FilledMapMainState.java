package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.View;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.TourSerializer;
import javafx.stage.FileChooser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
/**
 * FilledMapMainState extends a State {@link com.insa.lifraison.controller.NoDeliveriesMainState}
 * It is the main last state. It corresponds to the state where the user can ask to go to
 * any other state without any particular restriction.
 */
public class FilledMapMainState extends NoDeliveriesMainState {

    @Override
    public void entryAction(CityMap m, View view) {
        super.entryAction(m, view);
        view.<MainController>getController("main").enableButton("#computePlanButton");
        view.<MainController>getController("main").enableButton("#saveDeliveriesButton");
        view.<MainController>getController("main").enableButton("#saveRoadmapButton");
        view.<MainController>getController("main").enableButton("#removeDeliveryButton");
    }

    @Override
    public void exitAction(CityMap m, View view) {
        super.exitAction(m, view);
        view.<MainController>getController("main").disableButton("#computePlanButton");
        view.<MainController>getController("main").disableButton("#saveDeliveriesButton");
        view.<MainController>getController("main").disableButton("#saveRoadmapButton");
        view.<MainController>getController("main").disableButton("#removeDeliveryButton");
    }

    /**
     * Compute the plan with all actual information
     * @param m the CityMap
     * @param l the list of commands
     */
    @Override
    public void computePlan(CityMap m, ListOfCommands l) {
        m.clearSelection();
        l.add(new ComputePlanCommand(m));
    }

    /**
     * Save information about tours and deliveries on a file
     * @param m the CityMap
     * @param view the View
     */
    @Override
    public void saveDeliveries(CityMap m, View view){
        m.clearSelection();
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File("src/main/resources/maps"));
            File file = fileChooser.showSaveDialog(view.getStage());

            if (file != null) {
                //System.out.println(file.exists());
                TourSerializer instance = TourSerializer.getInstance();
                instance.save(m.getTours(), file);
            }
        }catch (ParserConfigurationException | ExceptionXML |TransformerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Go to deleteDeliveryState1
     * @param c the Controller
     * @param m the CityMap
     * @param view the View
     */
    @Override
    public void saveRoadmap(Controller c) {
        c.setCurrentState(c.saveRoadmapState);
    }

    @Override
    public void removeDelivery(Controller c, CityMap m, View view){
        c.setCurrentState(c.deleteDeliveryState1);
    }

    /**
     * Select a component
     * @param c the Controller
     * @param m the CityMap
     * @param l the list of commands
     */
    @Override
    public void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, Tour t, ListOfCommands l){
        if (d != null) {
            m.selectComponent(d);
        }
    }
}
