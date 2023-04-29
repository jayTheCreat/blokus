module com.example.blokus1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.blokus1 to javafx.fxml;
    exports com.example.blokus1;
}