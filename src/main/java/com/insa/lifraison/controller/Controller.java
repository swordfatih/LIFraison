package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.view.View;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {
    private View view;
    private CityMap map;
    private State currentState;
    protected final InitialState initialState = new InitialState();
    protected final LoadedMapState loadedMapState = new LoadedMapState();
    protected final AddDeliveryState1 addDeliveryState1 = new AddDeliveryState1();
    protected final AddDeliveryState2 addDeliveryState2 = new AddDeliveryState2();
    protected final LoadedDeliveryState loadedDeliveryState = new LoadedDeliveryState();
    protected final DeleteState deleteState1 = new DeleteState();
    protected final ComputeState computeState1 = new ComputeState();

    /**
     * Create the controller of the application
     */
    public Controller() {
        this.currentState = initialState;
        this.map = new CityMap();
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
        currentState.loadMap(this, map, view);
    };

    /**
     * Method called after a click on the "load Deliveries" button
     */
    public void loadDeliveries(){
        currentState.loadDeliveries(this, map);
    };

    /**
     * Method called after a click on the "Add Delivery" button
     */
    public void addDelivery(){
        currentState.addDelivery(this);
    };

    /**
     * Method called after a left Click
     * @param i The nearest intersection of the click
     */
    public void leftClick(Intersection i){
        currentState.leftClick(this, map, i);
    };

    /**
     * Method called after a right Click
     */
    public void rightClick(){
        currentState.rightClick(this, map);
    };

    /**
     * Method called after a click on the "Confirm" button
     */
    public void confirm(){
        currentState.confirm(this, map);
    };

    /**
     * Method called after a click on the "Delete Delivery" button
     */
    public void deleteDelivery(){
        currentState.deleteDelivery(this);
    };

    /**
     * Method called after a click on the "Compute" button
     */
    public void compute(){
        currentState.compute(this);
    };

    /**
     * Method called after when the Tour Class finished the computation of the tour
     */
    public void tourCompute(){
        currentState.compute(this);
    };
}
