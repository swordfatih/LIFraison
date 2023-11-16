package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;
import com.insa.lifraison.model.CityMap;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * Class MainController extends from {@link com.insa.lifraison.view.ViewController}
 * It manage the main app window. It's based on the main.fxml file
 */
public class MainController extends ViewController {
    /**
     * {@link com.insa.lifraison.view.MenuController}
     */
    @FXML
    private MenuController menuController;

    /**
     * {@link com.insa.lifraison.view.MapController}
     */
    @FXML
    private MapController mapController;

    /**
     * {@link com.insa.lifraison.view.TextualController}
     */
    @FXML
    private TextualController textualController;

    /**
     * {@link com.insa.lifraison.view.InformationController}
     */
    @FXML
    private InformationController informationController;

    /**
     * {@link com.insa.lifraison.view.ToursController}
     */
    @FXML
    private ToursController toursController;

    /**
     * {@link javafx.scene.layout.BorderPane}
     */
    @FXML
    private BorderPane main;

    /**
     * disable all the buttons
     */
    public void disableAllButtons() {
        GridPane buttons = (GridPane) this.main.getScene().lookup("#buttons");
        buttons.getChildren().forEach((button) -> button.setDisable(true));
    }

    /**
     * Disable a button
     * @param id the id of the button
     */
    public void disableButton(String id) {
        this.main.getScene().lookup(id).setDisable(true);
    }

    /**
     * Enable a button
     * @param id the id of the button
     */
    public void enableButton(String id) {
        this.main.getScene().lookup(id).setDisable(false);
    }

    /**
     * set the app controller to every ViewController of the window
     * @param controller the app controller
     */
    @Override
    public void setController(Controller controller) {
        super.setController(controller);
        menuController.setController(controller);
        mapController.setController(controller);
        textualController.setController(controller);
        informationController.setController(controller);
        toursController.setController(controller);
    }

    /**
     * set the map in the different viewController of the window
     * @param map the city map of the app {@link com.insa.lifraison.model.CityMap}
     */
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