package com.insa.lifraison.observer;

/**
 * Selectable is a class to know if an element is selected or not.
 * If an element of the model is selected in one of the views,
 * other views must also highlight it.
 */
public class Selectable {
    private boolean isSelected;

    public Selectable() {
        isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void select() {
        isSelected = true;
    }

    public void unselect() {
        isSelected = false;
    }
}
