package com.insa.lifraison.view;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalTime;

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

    public void displayAddDelivery1Informations() {
        this.information.getChildren().clear();
        Label info = new Label("Click on an intersection on the map to create a new delivery");
        this.information.getChildren().add(info);
    }

    public void displayAddDelivery2Informations() {
        this.information.getChildren().clear();
        ChoiceBox<String> cb = new ChoiceBox<>(FXCollections.observableArrayList("no time window","8h-9h", "9h-10h", "10h-11h", "11h-12h"));
        cb.setValue("no time window");
        cb.setOnAction(this::timeWindowChanged);
        this.information.getChildren().add(cb);
        Label info = new Label("If you are satisfied with your delivery, choose its tour\nelse you can click on the intersections to move it");
        this.information.getChildren().add(info);
    }

    public void timeWindowChanged(ActionEvent event) {
        event.consume();
        if(event.getSource() instanceof ChoiceBox<?>) {
            ChoiceBox<?> cb = (ChoiceBox<?>) event.getSource();
            if(cb.getValue() instanceof String) {
                String value = (String) cb.getValue();
                LocalTime timeWindowStart;
                LocalTime timeWindowEnd;
                switch (value) {
                    case "no time window":
                        timeWindowStart = null;
                        timeWindowEnd = null;
                        break;
                    case "8h-9h":
                        timeWindowStart = LocalTime.of(8,0);
                        timeWindowEnd = LocalTime.of(9,0);
                        break;
                    case "9h-10h":
                        timeWindowStart = LocalTime.of(9,0);
                        timeWindowEnd = LocalTime.of(10,0);
                        break;
                    case "10h-11h":
                        timeWindowStart = LocalTime.of(10,0);
                        timeWindowEnd = LocalTime.of(11,0);
                        break;
                    case "11h-12h":
                        timeWindowStart = LocalTime.of(11,0);
                        timeWindowEnd = LocalTime.of(12,0);
                        break;
                    default:
                        timeWindowStart = null;
                        timeWindowEnd = null;
                        break;
                }
                controller.timeWindowChanged(timeWindowStart, timeWindowEnd);
            }
        }
    }

    public void displayDeleteDeliveryInformations() {
        this.information.getChildren().clear();
        Label info = new Label("Click on the map to select a delivery to delete");
        Button confirm = new Button("confirm");
        confirm.setOnAction(this::confirmAction);
        this.information.getChildren().addAll(info, confirm);
    }

    public void displayDeleteTourInformations() {
        this.information.getChildren().clear();
        Label info = new Label("Click on a tour button to delete it");
        Button confirm = new Button("confirm");
        confirm.setOnAction(this::confirmAction);
        this.information.getChildren().addAll(info, confirm);
    }

    public void displayDeleteMapInformations(){
        clearInformations();
        Label info = new Label("Some deliveries are already on the map. By doing this action you will delete all deliveries. \nDo you still want to continue ?");
        Button confirm = new Button("confirm");
        confirm.setOnAction(this::confirmAction);
        Button cancel = new Button("cancel");
        cancel.setOnAction(this::cancelAction);
        this.information.getChildren().addAll(info, confirm, cancel);
    }

    public void clearInformations(){
        this.information.getChildren().clear();
    }
}
