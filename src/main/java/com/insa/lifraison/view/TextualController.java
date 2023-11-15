package com.insa.lifraison.view;

import com.insa.lifraison.model.*;
import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.observer.Observer;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.stream.Collectors;

import static java.lang.Double.valueOf;

public class TextualController extends ViewController implements Observer {
    @FXML
    private VBox console;

    private CityMap map;

    @Override
    public void update(Observable.NotifType type, Observable observed, Object arg) {
        if(type == Observable.NotifType.ADD && arg instanceof Observable) {
            ((Observable) arg).addObserver(this);
        }

        printContent();
    }

    private void printContent() {
        this.console.getChildren().clear();

        for(int i = 0; i < map.getTours().size(); ++i) {
            Tour tour = map.getTours().get(i);

            Label title = new Label();
            title.setText("Tour [" + i + "]");
            title.setStyle("--fx-font-weight: 800");

            this.console.getChildren().add(title);

            if(tour.getDeliveries().isEmpty()) {
                Label empty = new Label();
                empty.setText("Tour is empty. Add some deliveries.");
                empty.setStyle("-fx-text-fill: orange");

                this.console.getChildren().add(empty);
            }
            else {
                ObservableList<DeliveryRequest> deliveries = FXCollections.observableArrayList(tour.getDeliveries());
                TableView<DeliveryRequest> deliveryTable = new TableView<>(deliveries);

                TableColumn<DeliveryRequest, String> typeCol = new TableColumn<>("Type");
                TableColumn<DeliveryRequest, String> idCol = new TableColumn<>("ID");
                TableColumn<DeliveryRequest, String> lngCol = new TableColumn<>("Longitude");
                TableColumn<DeliveryRequest, String> latCol = new TableColumn<>("Latitude");

                typeCol.setCellValueFactory(p -> new ReadOnlyStringWrapper("Intersection"));
                idCol.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getIntersection().getId()));
                lngCol.setCellValueFactory(p -> new ReadOnlyStringWrapper(valueOf(p.getValue().getIntersection().getLongitude()).toString()));
                latCol.setCellValueFactory(p -> new ReadOnlyStringWrapper(valueOf(p.getValue().getIntersection().getLatitude()).toString()));

                deliveryTable.getColumns().add(typeCol);
                deliveryTable.getColumns().add(idCol);
                deliveryTable.getColumns().add(lngCol);
                deliveryTable.getColumns().add(latCol);

                deliveryTable.setRowFactory(tv -> {
                    TableRow<DeliveryRequest> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                            DeliveryRequest delivery = row.getItem();
                            this.controller.leftClick(delivery.getIntersection(), delivery, tour);
                        }
                    });
                    return row;
                });

                this.console.getChildren().add(deliveryTable);
            }

            if(tour.getTourSteps().isEmpty()) {
                Label empty = new Label();
                empty.setText("Tour doesn't have any step. Calculate it.");
                empty.setStyle("-fx-text-fill: orange");

                this.console.getChildren().add(empty);
            }
            else {
                ObservableList<Segment> segments = FXCollections.observableArrayList(tour.getTourSteps().stream().flatMap((step) -> step.segments.stream()).collect(Collectors.toList()));
                TableView<Segment> segmentTable = new TableView<>(segments);

                TableColumn<Segment, String> typeCol = new TableColumn<>("Type");
                TableColumn<Segment, String> nameCol = new TableColumn<>("Street");
                TableColumn<Segment, String> origCol = new TableColumn<>("Origin");
                TableColumn<Segment, String> destCol = new TableColumn<>("Destination");

                typeCol.setCellValueFactory(p -> new ReadOnlyStringWrapper("Segment"));
                nameCol.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().name));
                origCol.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().origin.toString()));
                destCol.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().destination.toString()));

                segmentTable.getColumns().add(typeCol);
                segmentTable.getColumns().add(nameCol);
                segmentTable.getColumns().add(origCol);
                segmentTable.getColumns().add(destCol);

                this.console.getChildren().add(segmentTable);
            }
        }
    }

    public void setMap(CityMap map) {
        this.map = map;
        this.map.addObserver(this);
        printContent();
    }
}
