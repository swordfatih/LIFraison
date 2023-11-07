module lifraison {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens com.insa.lifraison.controller to javafx.fxml;
    opens com.insa.lifraison.view to javafx.fxml;
    opens com.insa.lifraison to javafx.fxml;

    exports com.insa.lifraison.controller;
    exports com.insa.lifraison.model;
    exports com.insa.lifraison.view;
    exports com.insa.lifraison;
    exports com.insa.lifraison.observer;
}