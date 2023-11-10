package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class AddTourCommand implements Command {
    private CityMap cityMap;
    private Tour tour;
    private VBox container;
    private String text;
    private Controller controller;
    private Button button;

    /**
     * Create the command which adds a Tour to a CityMap
     * @param cityMap the map to which tour is added
     * @param tour the tour added to cityMap
     */
    public AddTourCommand(CityMap cityMap, VBox container, String text, Controller controller) {
        this.cityMap = cityMap;
        this.tour = new Tour();
        this.container = container;
        this.text = text;
        this.controller = controller;

        this.button = new Button(text);
    }

    @Override
    public void doCommand() {
        this.cityMap.addTour(tour);
        
        button.setOnAction(event -> {
            event.consume();
            int index = container.getChildren().indexOf(button);
            controller.handleTourButton(index, container);
        });
        container.getChildren().add(button);
    }

    @Override
    public void undoCommand() {
        this.cityMap.removeTour(tour);
        container.getChildren().remove(button);
    }
}
