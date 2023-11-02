package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.view.MapController;
import com.insa.lifraison.view.View;
import javafx.stage.Stage;

public class Controller {
    private View view;
    private CityMap map;

    private ListOfCommands listOfCommands;
    private State currentState;
    protected final InitialState initialState = new InitialState();
    protected final LoadedMapState loadedMapState = new LoadedMapState();
    protected final AddDeliveryState1 addDeliveryState1 = new AddDeliveryState1();
    protected final AddDeliveryState2 addDeliveryState2 = new AddDeliveryState2();
    protected final LoadedDeliveryState loadedDeliveryState = new LoadedDeliveryState();
    protected final DeleteDeliveryState1 deleteDeliveryState1 = new DeleteDeliveryState1();

    protected final DeleteDeliveryState2 deleteDeliveryState2 = new DeleteDeliveryState2();

    /**
     * Create the controller of the application
     */
    public Controller(Stage stage) {
        this.currentState = initialState;
        this.listOfCommands = new ListOfCommands();
        this.map = new CityMap();
        this.view = new View(stage, this);

        view.loadScene("home", "home.fxml", "style/home.css");
        view.loadScene("map", "map.fxml", "style/map.css");
        view.<MapController>getController("map").setMap(this.map);
        stage.setTitle("LIFraison");
        view.navigate("home");
        stage.show();
    }

    public void setView(View view) {
        this.view = view;
    }

    /**
     * Change the current state
     * @param state new state
     */
    protected void setCurrentState(State state) {
        this.currentState = state;
    }

    /**
     * Method called after a click on the "load Map" button
     */
    public void loadMap(){
        currentState.loadMap(this, map, view, listOfCommands);
    };

    /**
     * Method called after a click on the "load Deliveries" button
     */
    public void loadDeliveries(){
        currentState.loadDeliveries(this, map, view, listOfCommands);
    };

    /**
     * Method called after a click on the "Add Delivery" button
     */
    public void addDelivery(){
        currentState.addDelivery(this,view);
    };

    /**
     * Method called after a left Click
     * @param i The nearest intersection of the click
     */
    public void leftClick(Intersection i, DeliveryRequest d){
        currentState.leftClick(this, map, i, d, listOfCommands);
    };

    /**
     * Method called after a right Click
     */
    public void rightClick(){
        currentState.rightClick(this, map, view, listOfCommands);
    };

    /**
     * Method called after a click on the "Confirm" button
     */
    public void confirm(){
        currentState.confirm(this, map, view, listOfCommands);
    };

    /**
     * Method called after a click on the "Delete Delivery" button
     */
    public void deleteDelivery(){
        currentState.deleteDelivery(this, view);
    };

    /**
     * Method called after a click on the "Compute Plan" button
     */
    public void computePlan(){
        currentState.computePlan(map, listOfCommands);
    };
    /**
     * Method called after a click on the "Undo" button
     */
    public void undo(){
        currentState.undo(this, map, listOfCommands);
    };

    /**
     * Method called after a click on the "Redo" button
     */
    public void redo(){
        currentState.redo(this, map, listOfCommands);
    };
}
