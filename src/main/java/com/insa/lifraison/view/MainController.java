package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;
import com.insa.lifraison.model.CityMap;
import javafx.fxml.FXML;

public class MainController extends ViewController {
    @FXML
    private MenuController menuController;

    @FXML
    private MapController mapController;

    @FXML
    private TextualController textualController;

    @FXML
    private InformationController informationController;

    @FXML
    private ToursController toursController;

    @Override
    public void setController(Controller controller) {
        super.setController(controller);
        menuController.setController(controller);
        mapController.setController(controller);
        textualController.setController(controller);
        informationController.setController(controller);
        toursController.setController(controller);
    }

    public void setMap(CityMap map) {
        mapController.setMap(map);
        textualController.setMap(map);
        toursController.setMap(map);
    }

    public InformationController getInformationController() {
        return informationController;
    }
}
