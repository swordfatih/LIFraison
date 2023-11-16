package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;

/**
 * Abstract class ViewController.
 * All other controller views will extend from this one
 */
public abstract class ViewController {
    protected Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
