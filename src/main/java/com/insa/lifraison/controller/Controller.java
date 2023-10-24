package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import model.CityMap;

public class Controller {
    private CityMap map;
    private State currentState;
    protected final InitialState initialState = new InitialState();
    protected final LoadedMapState loadedMapState = new LoadedMapState();
    protected final AddDeliveryState1 addDeliveryState1 = new AddDeliveryState1();
    protected final LoadedDeliveryState loadedDeliveryState = new LoadedDeliveryState();

    /**
     * Create the controller of the application
     * @param map the city map of the application
     */
    public Controller(CityMap map) {
        this.map = map;
        this.currentState = initialState;
    }

    /**
     * Change the current state
     * @param state new state
     */
    protected void setCurrentState(State state){
        this.currentState = state;
    }
}
