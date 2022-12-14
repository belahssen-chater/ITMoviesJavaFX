module com.example.itmovies {
    requires java.se;

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.itmovies.controllers to javafx.fxml;
    exports com.itmovies.controllers;
}