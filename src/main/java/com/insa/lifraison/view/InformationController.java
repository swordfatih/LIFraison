package com.insa.lifraison.view;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalTime;

/**
 * Class InformationController extends from {@link com.insa.lifraison.view.ViewController}
 * It gives information about what can do a user on a specific state.
 */
public class InformationController extends ViewController {
    /**
     * {@link javafx.scene.layout.VBox}
     */
    @FXML
    private VBox information;

    /**
     * This method is called after a click on the "Confirm" button
     * Informs the controller
     * @param event the input event
     */
    private void confirmAction(ActionEvent event) {
        event.consume();
        controller.confirm();
    }
    /**
     * This method is called after a click on the "Cancel" button
     * Informs the controller
     * @param event the input event
     */
    private void cancelAction(ActionEvent event){
        event.consume();
        controller.cancel();
    }

    /**
     * Display information on addDeliveryState1
     */
    public void displayAddDelivery1Information() {
        this.information.getChildren().clear();
        Label info = new Label("Click on an intersection on the map to create a new delivery");
        this.information.getChildren().add(info);
    }

    /**
     * Display information on addDeliveryState2
     * and a choice box for the time window
     */
    public void displayAddDelivery2Information() {
        this.information.getChildren().clear();
        ChoiceBox<String> cb = new ChoiceBox<>(FXCollections.observableArrayList("no time window","8h-9h", "9h-10h", "10h-11h", "11h-12h"));
        cb.setValue("no time window");
        cb.setOnAction(this::timeWindowChanged);
        this.information.getChildren().add(cb);
        Label info = new Label("If you are satisfied with your delivery, choose its tour\nelse you can click on the intersections to move it");
        this.information.getChildren().add(info);
    }

    /**
     * On click on the time window choice box
     * redefine the time window of the delivery
     * @param event the input event
     */
    public void timeWindowChanged(ActionEvent event) {
        event.consume();
        if(event.getSource() instanceof ChoiceBox<?> cb) {
            if(cb.getValue() instanceof String value) {
                LocalTime timeWindowStart;
                LocalTime timeWindowEnd;
                switch (value) {
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
                    case "no time window":
                    default:
                        timeWindowStart = null;
                        timeWindowEnd = null;
                        break;
                }
                controller.timeWindowChanged(timeWindowStart, timeWindowEnd);
            }
        }
    }

    /**
     * Display information on deleteDeliveryState
     */
    public void displayDeleteDeliveryInformation() {
        this.information.getChildren().clear();
        Label info = new Label("Click on the map to select a delivery to delete");
        Button confirm = new Button("confirm");
        confirm.setOnAction(this::confirmAction);
        this.information.getChildren().addAll(info, confirm);
    }

    /**
     * Display information on deleteTourState
     */
    public void displayDeleteTourInformation() {
        this.information.getChildren().clear();
        Label info = new Label("Click on a tour button to delete it");
        Button confirm = new Button("confirm");
        confirm.setOnAction(this::confirmAction);
        this.information.getChildren().addAll(info, confirm);
    }

    /**
     * display message on changeMapState
     */
    public void displayDeleteMapInformation(){
        clearInformation();
        Label info = new Label("Some deliveries are already on the map. By doing this action you will delete all deliveries. \nDo you still want to continue ?");
        Button confirm = new Button("confirm");
        confirm.setOnAction(this::confirmAction);
        Button cancel = new Button("cancel");
        cancel.setOnAction(this::cancelAction);
        this.information.getChildren().addAll(info, confirm, cancel);
    }

    /**
     * display message on saveRoadmapState
     */
    public void displaySaveRoadmapInformation(){
        clearInformation();
        Label info = new Label("Click on a Tour button to save it");
        Button cancel = new Button("cancel");
        cancel.setOnAction(this::cancelAction);
        this.information.getChildren().addAll(info, cancel);
    }

    /**
     * clear the information from the BVox
     */
    public void clearInformation(){
        this.information.getChildren().clear();
    }
}