module lifraison {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens com.insa.lifraison.view to javafx.fxml;
    opens com.insa.lifraison to javafx.fxml;
    exports com.insa.lifraison;
}