module com.example.librarymanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jbcrypt;

    opens com.example.librarymanagement to javafx.fxml;
    exports com.example.librarymanagement;

    opens com.example.librarymanagement.controller to javafx.fxml;
    opens com.example.librarymanagement.model to javafx.fxml;
    exports com.example.librarymanagement.controller;
}