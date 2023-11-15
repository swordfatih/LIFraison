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

public class FilledMapMainState extends NoDeliveriesMainState {

    @Override
    public void entryAction(CityMap m, View view) {
        super.entryAction(m, view);
        view.<MainController>getController("main").enableButton("#computePlanButton");
        view.<MainController>getController("main").enableButton("#saveButton");
        view.<MainController>getController("main").enableButton("#removeDeliveryButton");
    }

    @Override
    public void exitAction(CityMap m, View view) {
        super.exitAction(m, view);
        view.<MainController>getController("main").disableButton("#computePlanButton");
        view.<MainController>getController("main").disableButton("#saveButton");
        view.<MainController>getController("main").disableButton("#removeDeliveryButton");
    }

    @Override
    public void computePlan(CityMap m, ListOfCommands l) {
        m.clearSelection();
        l.add(new ComputePlanCommand(m));
    }

    @Override
    public void save(CityMap m, View view){
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

    @Override
    public void removeDelivery(Controller c, CityMap m, View view){
        c.setCurrentState(c.deleteDeliveryState1);
    }

    @Override
    public void leftClick(Controller c, CityMap m, Intersection i, DeliveryRequest d, Tour t, ListOfCommands l){
        if (d != null) {
            m.selectComponent(d);
        }
    }
}
