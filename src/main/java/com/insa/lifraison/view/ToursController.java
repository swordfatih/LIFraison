package com.insa.lifraison.view;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * The TourController manage the interaction and the visualization of the courier available
 * {@link com.insa.lifraison.view.ViewController}
 * {@link com.insa.lifraison.observer.Observer}
 */
public class ToursController extends ViewController implements Observer {
    /**
     * {@link javafx.scene.layout.VBox}
     */
    @FXML
    private VBox tourList;
    /**
     * {@link com.insa.lifraison.model.CityMap}
     */
    private CityMap map;

    /**
     * update the list of button view
     * @param type the type of notification
     * @param observed the observable which has notified the observer
     * @param arg optional information about the update
     */
    @Override
    public void update(Observable.NotifType type, Observable observed, Object arg) {
        if(observed instanceof Tour && type == Observable.NotifType.UPDATE) {
            update((Tour) observed);
        }
        if (arg instanceof Tour tour) {
            if(type == Observable.NotifType.ADD) {
                add(tour);
            }
            else if(type == Observable.NotifType.REMOVE) {
                remove(tour);
            }
        } else if (arg == null && observed instanceof CityMap && type == Observable.NotifType.UPDATE){
            refresh( (CityMap) observed);
        }
    }

    /**
     * redraw every button
     * @param map the app map
     */
    public void refresh(CityMap map) {
        this.tourList.getChildren().clear();
        for(Tour tour : map.getTours()) {
            add(tour);
        }
    }

    /**
     * copy the map in a local property
     * make it observable by this
     * @param map the app map
     */
    public void setMap(CityMap map) {
        this.map = map;
        map.addObserver(this);
        this.refresh(map);
    }

    /**
     * change the color of the tour text button if the tour is selected
     * @param tour the tour that need an update
     */
    private void update(Tour tour) {
        ButtonTour buttonTour = null;
        for(Node n : this.tourList.getChildren()) {
            if(n instanceof ButtonTour) {
                if(((ButtonTour) n).getTour() == tour) {
                    buttonTour = (ButtonTour) n;
                }
            }
        }
        if(buttonTour != null) {
            if(tour.isSelected()) {
                buttonTour.setTextFill(map.getSelectionColor());
            } else {
                buttonTour.setTextFill(tour.getColor());
            }
        }
    }

    /**
     * remove the tour button
     * @param tour the tour button that must be erased
     */
    private void remove(Tour tour) {
        Node tourButton = null;
        for(Node n : this.tourList.getChildren()) {
            if(n instanceof ButtonTour) {
                if(((ButtonTour) n).getTour() == tour) {
                   tourButton = n;
                }
            }
        }
        if(tourButton != null) {
            this.tourList.getChildren().remove(tourButton);
        }
    }

    /**
     * add a button in the list of button
     * automatically choose a number (first available)
     * @param tour the tour to add
     */
    private void add(Tour tour) {
        tour.addObserver(this);
        ButtonTour buttonTour;
        if(tour.isSelected()) {
            buttonTour = new ButtonTour(tour, map.getSelectionColor());
        } else {
            buttonTour = new ButtonTour(tour, tour.getColor());
        }
        buttonTour.setOnAction(this::buttonTourClicked);
        buttonTour.setStyle("-fx-max-width: 1000");
        int i = 0;
        while (i < this.tourList.getChildren().size() && ((ButtonTour) this.tourList.getChildren().get(i)).getTour().getId() < tour.getId()) {
            i++;
        }
        this.tourList.getChildren().add(i, buttonTour);
    }

    /**
     * called after a click on a "Courier X" button
     * @param event the event input
     */
    private void buttonTourClicked(ActionEvent event) {
        event.consume();
        Object source = event.getSource();
        if(source instanceof ButtonTour) {
            ButtonTour buttonTour = (ButtonTour) source;
            this.controller.tourButtonClicked(buttonTour.getTour());
        }
    }
}
