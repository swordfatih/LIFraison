package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;

/**
 * Abstract class ViewController.
 * All other controller views will extend from this one
 */
public abstract class ViewController {
    /**
     * {@link com.insa.lifraison.controller.Controller}
     */
    protected Controller controller;

    /**
     * set the app controller to the view
     * @param controller the app controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
