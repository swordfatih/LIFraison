package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;
import com.insa.lifraison.model.CityMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

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

    @FXML
    private BorderPane main;

    public void disableAllButtons() {
        GridPane buttons = (GridPane) this.main.getScene().lookup("#buttons");
        buttons.getChildren().forEach((button) -> button.setDisable(true));
    }
    public void disableButton(String id) {
        this.main.getScene().lookup(id).setDisable(true);
    }

    public void enableButton(String id) {
        this.main.getScene().lookup(id).setDisable(false);
    }

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
