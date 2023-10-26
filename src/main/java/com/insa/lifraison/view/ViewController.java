package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;

public abstract class ViewController {
    protected Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
