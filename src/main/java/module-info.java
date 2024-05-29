module com.example.desktopclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens com.example.desktopclient to javafx.fxml;
    exports com.example.desktopclient;
    exports com.example.desktopclient.ui;
    exports com.example.desktopclient.model;
}