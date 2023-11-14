package com.insa.lifraison.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InformationController extends ViewController {
    @FXML
    private VBox information;

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
        this.information.getChildren().clear();
        Label info = new Label("Click anywhere on the map to create a new delivery");
        this.information.getChildren().add(info);
    }

    public void displayDeleteDeliveryInformations() {
        this.information.getChildren().clear();
        Label info = new Label("Click on the map to select a delivery to delete");
        Button confirm = new Button("confirm");
        confirm.setOnAction(this::confirmAction);
        this.information.getChildren().addAll(info, confirm);
    }

    public void displayDeleteMapInformations(){
        clearInformations();
        Label info = new Label("Some deliveries are already on the map. \nBy doing this action you will delete all deliveries. \nDo you still want to continue ?");
        Button confirm = new Button("confirm");
        confirm.setOnAction(this::loadMap);
        Button cancel = new Button("cancel");
        cancel.setOnAction(this::cancelAction);
        this.information.getChildren().addAll(info, confirm, cancel);
    }

    public void clearInformations(){
        this.information.getChildren().clear();
    }
}
