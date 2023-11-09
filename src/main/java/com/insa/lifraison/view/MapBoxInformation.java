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

    private void loadMap(ActionEvent event) {
        event.consume();
        controller.loadMap();
    }

    private void cancelAction(ActionEvent event){
        event.consume();
        controller.cancel();
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

    public void displayDeleteMapInformations(){
        clearInformations();
        Label info = new Label("Some deliveries are already on the map. \nBy doing this action you will delete all deliveries. \nDo you still want to continue ?");
        Button confirm = new Button("confirm");
        confirm.setOnAction(this::loadMap);
        Button cancel = new Button("cancel");
        cancel.setOnAction(this::cancelAction);
        this.getChildren().addAll(info, confirm, cancel);
    }

    public void clearInformations(){
        this.getChildren().clear();
    }


}
