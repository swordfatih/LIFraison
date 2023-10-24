module lifraison {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens com.insa.lifraison to javafx.fxml;
    exports com.insa.lifraison;
}