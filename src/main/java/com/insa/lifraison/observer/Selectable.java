package com.insa.lifraison.observer;

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
