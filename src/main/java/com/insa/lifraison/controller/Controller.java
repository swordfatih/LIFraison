package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Intersection;

public class Controller {
    private CityMap map;
    private State currentState;
    protected final InitialState initialState = new InitialState();
    protected final LoadedMapState loadedMapState = new LoadedMapState();
    protected final AddDeliveryState1 addDeliveryState1 = new AddDeliveryState1();
    protected final AddDeliveryState2 addDeliveryState2 = new AddDeliveryState2();
    protected final LoadedDeliveryState loadedDeliveryState = new LoadedDeliveryState();
    protected final DeleteState1 deleteState1 = new DeleteState1();
    protected final ComputeState1 computeState1 = new ComputeState1();

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
    protected void setCurrentState(State state) {
        this.currentState = state;
    }

    public void loadMap(){
        currentState.loadMap(this, map);
    };

    public void loadDeliveries(){
        currentState.loadDeliveries(this, map);
    };

    public void addDelivery(){
        currentState.addDelivery(this);
    };

    public void leftClick(Intersection i){
        currentState.leftClick(this, map, i);
    };

    public void rightClick(){
        currentState.rightClick(this, map);
    };

    public void confirm(){
        currentState.confirm(this, map);
    };

    public void deleteDelivery(){
        currentState.deleteDelivery(this);
    };

    public void compute(){
        currentState.compute(this);
    };

    public void tourCompute(){
        currentState.compute(this);
    };
}
