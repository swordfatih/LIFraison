package com.insa.lifraison.view;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.Segment;
import com.insa.lifraison.model.Warehouse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MapController extends ViewController {
    private CityMap map;

    private double scale;
    private double latitudeOffset;

    private double longitudeOffset;

    @FXML
    private Label label;

    @FXML
    private Pane mapPane;

    public void initialize() {
        label.setText("Map page");
    }

    @FXML
    private void loadMap(ActionEvent event) {
        event.consume();

        this.controller.loadMap();
    }
    public void setMap(CityMap map, File file) {
        this.map = map;

        this.label.setText(file.getAbsolutePath());

        // find min max

        // find small one
        double sizeLatitude = this.map.getMaxLatitude()-this.map.getMinLatitude();
        double sizeLongitude = this.map.getMaxLongitude()-this.map.getMinLongitude();
        double XScale = 940.0 / sizeLongitude;
        double YScale = 700.0/sizeLatitude;
        scale = min(XScale,YScale);
        longitudeOffset = -scale * this.map.getMinLongitude();
        latitudeOffset = scale * this.map.getMaxLatitude();

        this.mapPane.getChildren().clear();
        Iterator<Segment> iterator = this.map.getSegmentsIterator();
        while (iterator.hasNext()){
            addSegmentLine(iterator.next());
        }

        // create the warehouse
        Warehouse warehouse = this.map.getWarehouse();
        double xWarehouse = scale * warehouse.getIntersection().longitude + longitudeOffset;
        double yWarehouse = -scale * warehouse.getIntersection().latitude + latitudeOffset;
        Circle posWarehouse = new Circle(xWarehouse,yWarehouse,5);
        posWarehouse.setFill(Color.RED);
        this.mapPane.getChildren().add(posWarehouse);

    }

    public void addSegmentLine(Segment segment){
        double yOrigin = -scale * segment.origin.latitude + latitudeOffset;
        double xOrigin = scale * segment.origin.longitude + longitudeOffset;
        double yDest = -scale * segment.destination.latitude + latitudeOffset;
        double xDest = scale * segment.destination.longitude + longitudeOffset;
        System.out.println(xOrigin+"; "+ yOrigin);
        this.mapPane.getChildren().add(new Line(xOrigin,yOrigin,xDest,yDest));
    }
}
