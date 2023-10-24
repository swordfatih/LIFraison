package com.insa.lifraison.controller;

public class ComputeState implements State {

    /**
     * go back to the loadedStateState when the tour is calculate
     * @param c
     */
    @Override
    public void tourCompute(Controller c){
        c.setCurrentState(c.loadedDeliveryState);
    }
}
