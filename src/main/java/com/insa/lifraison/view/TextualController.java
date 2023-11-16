package com.insa.lifraison.view;

import com.insa.lifraison.model.*;
import com.insa.lifraison.observer.Observable;
import com.insa.lifraison.observer.Observer;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
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
            title.getStyleClass().add("selectable");

            if(tour.isSelected()) {
                title.getStyleClass().add("selected");
            }

            title.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> controller.tourButtonClicked(tour));

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

                TableColumn<DeliveryRequest, String> idCol = new TableColumn<>("ID");
                TableColumn<DeliveryRequest, String> statusCol = new TableColumn<>("Status");
                TableColumn<DeliveryRequest, String> startCol = new TableColumn<>("Window start");
                TableColumn<DeliveryRequest, String> endCol = new TableColumn<>("Window end");
                TableColumn<DeliveryRequest, String> coordsCol = new TableColumn<>("Coordinates");

                idCol.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getIntersection().getId()));
                statusCol.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getState().toString()));
                startCol.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getTimeWindowStart() != null ? p.getValue().getTimeWindowStart().format(DateTimeFormatter.ISO_LOCAL_TIME) : ""));
                endCol.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getTimeWindowEnd() != null ? p.getValue().getTimeWindowEnd().format(DateTimeFormatter.ISO_LOCAL_TIME) : ""));
                coordsCol.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getIntersection().getLongitude() + "\n" + p.getValue().getIntersection().getLatitude()));

                deliveryTable.getColumns().add(idCol);
                deliveryTable.getColumns().add(statusCol);
                deliveryTable.getColumns().add(startCol);
                deliveryTable.getColumns().add(endCol);
                deliveryTable.getColumns().add(coordsCol);

                deliveryTable.getSortOrder().add(startCol);

                deliveryTable.setRowFactory(tv -> {
                    TableRow<DeliveryRequest> row = new TableRow<>() {
                        @Override
                        protected void updateItem(DeliveryRequest delivery, boolean empty) {
                            super.updateItem(delivery, empty);

                            if(delivery != null && delivery.isSelected()) {
                                this.getStyleClass().add("selected");
                            }
                        }
                    };

                    row.getStyleClass().add("selectable");

                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                            DeliveryRequest delivery = row.getItem();
                            this.controller.leftClick(delivery.getIntersection(), delivery, tour);
                        }
                    });

                    DeliveryRequest delivery = row.getItem();
                    if(delivery != null && delivery.isSelected()) {
                        row.setStyle("-fx-text-fill: #800080");
                    }

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

        for (Tour tour: this.map.getTours()) {
            tour.addObserver(this);

            for(DeliveryRequest delivery: tour.getDeliveries()) {
                delivery.addObserver(this);
            }
        }

        printContent();
    }
}
