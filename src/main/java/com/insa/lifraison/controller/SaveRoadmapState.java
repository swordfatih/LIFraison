package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.view.View;
import com.insa.lifraison.xml.ExceptionXML;
import com.insa.lifraison.xml.Roadmap;
import javafx.stage.FileChooser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;

public class SaveRoadmapState implements State {

    @Override
    public void entryAction(CityMap m, View view) {
        view.<MainController>getController("main").getInformationController().displaySaveRoadmapInformation();
    }

    @Override
    public void exitAction(CityMap m, View view) {
        view.<MainController>getController("main").getInformationController().clearInformation();
    }

    @Override
    public void tourButtonClicked(Controller c, CityMap m, Tour t, View v, ListOfCommands l) {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File("src/main/resources/maps"));
            File file = fileChooser.showSaveDialog(v.getStage());

            if (file != null) {
                //System.out.println(file.exists());
                Roadmap.save(t, file);
                c.setCurrentState(c.filledMapMainState);
            }
        }catch (ParserConfigurationException | ExceptionXML | TransformerException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void rightClick(Controller c, CityMap m, View view, ListOfCommands l){
        c.setCurrentState(c.filledMapMainState);
    }

    @Override
    public void cancel(Controller c, View view) {
        c.setCurrentState(c.filledMapMainState);
    }

}
