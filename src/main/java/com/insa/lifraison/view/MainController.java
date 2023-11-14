package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;
import com.insa.lifraison.model.CityMap;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

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

    /**
     * Handle map click event
     * @param event mouseListener on the map
     */
    public void mainClick(MouseEvent event) {
        if(event.getButton() == MouseButton.SECONDARY) {
            this.controller.rightClick();
        }
    }
}
