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

public class ToursController extends ViewController implements Observer {
    @FXML
    private VBox tourList;

    private ArrayList<Tour> tours;

    public void initialize() {
        this.tours = new ArrayList<>();
    }

    @Override
    public void update(Observable.NotifType type, Observable observed, Object arg) {
        if (arg instanceof Tour) {
            Tour tour = (Tour) arg;
            if(type == Observable.NotifType.ADD) {
                add(tour);
            }
            else if(type == Observable.NotifType.REMOVE) {
                remove(tour);
            }
        } else if (arg == null && type == Observable.NotifType.UPDATE){
            refresh( (CityMap) observed);
        }
    }

    public void refresh(CityMap map) {
        this.tourList.getChildren().clear();
        for(Tour tour : map.getTours()) {
            add(tour);
        }
    }
    public void setMap(CityMap map) {
        map.addObserver(this);
        this.refresh(map);
    }

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

    private void add(Tour tour) {
        ButtonTour buttonTour = new ButtonTour(tour);
        buttonTour.setOnAction(this::buttonTourClicked);
        buttonTour.setStyle("-fx-max-width: 1000");
        int i = 0;
        while (i < this.tourList.getChildren().size() && ((ButtonTour) this.tourList.getChildren().get(i)).getTour().getId() < tour.getId()) {
            i++;
        }
        this.tourList.getChildren().add(i, buttonTour);
    }

    private void buttonTourClicked(ActionEvent event) {
        event.consume();
        Object source = event.getSource();
        if(source instanceof ButtonTour) {
            ButtonTour buttonTour = (ButtonTour) source;
            this.controller.tourButtonClicked(buttonTour.getTour());
        }
    }
}
