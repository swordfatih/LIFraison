package com.insa.lifraison.view;

import com.insa.lifraison.controller.Controller;
import com.insa.lifraison.observer.Observer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MapBoxInformation extends VBox{
    private Controller controller;
    public MapBoxInformation(){
    }

    public void setController (Controller c){this.controller = c;}

    private void confirmAction(ActionEvent event) {
        event.consume();
        controller.confirm();
    }

    public void displayAddDeliveryInformations() {
        this.getChildren().clear();
        Label info = new Label("Click anywhere on the map to create a new delivery");
        Button confirm = new Button("confirm");
        confirm.setOnAction(this::confirmAction);
        this.getChildren().addAll(info, confirm);
    }

    public void displayDeleteDeliveryInformations() {
        this.getChildren().clear();
        Label info = new Label("Click on the map to select a delivery to delete");
        Button confirm = new Button("confirm");
        confirm.setOnAction(this::confirmAction);
        this.getChildren().addAll(info, confirm);
    }

    public void clearInformations(){
        this.getChildren().clear();
    }


}
