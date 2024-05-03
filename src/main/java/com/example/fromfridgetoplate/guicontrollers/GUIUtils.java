package com.example.fromfridgetoplate.guicontrollers;

import javafx.scene.control.Alert;

public class GUIUtils {

    public static void showErrorAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
