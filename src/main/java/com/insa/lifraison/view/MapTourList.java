package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;
import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.observer.Observer;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class MapTourList extends VBox implements Observer{
    private Controller controller;

    private ArrayList<Tour> tours;
    public MapTourList(){
        tours = new ArrayList<>();
    }

    public void setController (Controller c){this.controller = c;}

    @Override
    public void update(Observable.NotifType type, Observable observed, Object arg) {
        System.out.println(this + "received notif");
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
        this.getChildren().clear();
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
        for(Node n : this.getChildren()) {
            if(n instanceof ButtonTour) {
                if(((ButtonTour) n).getTour() == tour) {
                   tourButton = n;
                }
            }
        }
        if(tourButton != null) {
            this.getChildren().remove(tourButton);
        }
    }

    private void add(Tour tour) {
        ButtonTour buttonTour = new ButtonTour(tour);
        buttonTour.setOnAction(this::buttonTourClicked);
        this.getChildren().add(buttonTour);
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
