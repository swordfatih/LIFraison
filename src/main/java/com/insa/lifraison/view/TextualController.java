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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.Double.valueOf;

/**
 * The textual view of the application
 * {@link com.insa.lifraison.view.ViewController}
 * {@link com.insa.lifraison.observer.Observer}
 */
public class TextualController extends ViewController implements Observer {
    /**
     * {@link javafx.scene.layout.VBox}
     */
    @FXML
    private VBox console;
    /**
     * {@link com.insa.lifraison.model.CityMap}
     */
    private CityMap map;

    /**
     * update the textual view
     * @param type the type of notification
     * @param observed the observable which has notified the observer
     * @param arg optional information about the update
     */
    @Override
    public void update(Observable.NotifType type, Observable observed, Object arg) {
        if(type == Observable.NotifType.ADD && arg instanceof Observable) {
            ((Observable) arg).addObserver(this);
        }
        printContent();
    }

    /**
     * write and display the text
     */
    private void printContent() {
        this.console.getChildren().clear();

        for(int i = 0; i < map.getTours().size(); ++i) {
            Tour tour = map.getTours().get(i);

            Label title = new Label();
            title.setText("Tour " + tour.getId() );
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

                TableColumn<DeliveryRequest, String> idCol = new TableColumn<>("Delivery");
                TableColumn<DeliveryRequest, String> statusCol = new TableColumn<>("Status");
                TableColumn<DeliveryRequest, String> winCol = new TableColumn<>("Window");
                TableColumn<DeliveryRequest, String> coordsCol = new TableColumn<>("Coordinates");

                idCol.setCellValueFactory(p -> {
                    List<Segment> segments = map.getSegments().stream().filter(segment -> segment.contains(p.getValue().getIntersection())).toList();
                    return new ReadOnlyStringWrapper(segments.stream().map(segment -> segment.name + "\n").distinct().collect(Collectors.joining()));
                });

                statusCol.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getState().toString()));
                winCol.setCellValueFactory(p -> {
                    LocalTime startTime = p.getValue().getTimeWindowStart();
                    LocalTime endTime = p.getValue().getTimeWindowEnd();

                    if(p.getValue().getState() == DeliveryState.NotCalculated) {
                        return new ReadOnlyStringWrapper("Not calculated");
                    } else if(startTime == null || endTime == null) {
                        return new ReadOnlyStringWrapper("");
                    }

                    String start = startTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
                    String end = endTime.format(DateTimeFormatter.ISO_LOCAL_TIME);

                    return new ReadOnlyStringWrapper("start: " + start + "\nend: " + end);
                });

                coordsCol.setCellValueFactory(p -> {
                    String lng = String.valueOf(p.getValue().getIntersection().getLongitude());
                    String lat = String.valueOf(p.getValue().getIntersection().getLatitude());
                    return new ReadOnlyStringWrapper( "lng: " + lng + "\nlat: " + lat);
                });

                deliveryTable.getColumns().add(idCol);
                deliveryTable.getColumns().add(statusCol);
                deliveryTable.getColumns().add(winCol);
                deliveryTable.getColumns().add(coordsCol);

                deliveryTable.getSortOrder().add(winCol);

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
                        if (!row.isEmpty()) {
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
