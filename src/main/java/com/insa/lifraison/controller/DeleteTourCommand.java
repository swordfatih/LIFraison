package com.insa.lifraison.controller;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class DeleteTourCommand implements Command{
    private int index;
    private VBox container;
    private Button button;
    public DeleteTourCommand(int index, VBox container) {
        this.index = index;
        this.container = container;
        this.button = null;
    }
    @Override
    public void doCommand() {
        this.button = (Button) container.getChildren().remove(index);
    }

    @Override
    public void undoCommand() {
        container.getChildren().add(index, this.button);
    }
}
