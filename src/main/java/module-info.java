module com.example.fromfridgetoplate {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.fromfridgetoplate.guicontrollers to javafx.fxml;
    exports com.example.fromfridgetoplate.guicontrollers;
    exports com.example.fromfridgetoplate.guicontrollers.list_cell_factories;
    opens com.example.fromfridgetoplate.guicontrollers.list_cell_factories to javafx.fxml;
    exports com.example.fromfridgetoplate.logic.bean;
}