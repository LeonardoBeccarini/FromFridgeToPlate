package com.example.fromfridgetoplate.guicontrollers;

import javafx.scene.control.Alert;

public class GUIUtils {
    private GUIUtils(){
        //in questo modo il compilatore non crea un costruttore di deafult e questa utility class non può essere istanziata.
        throw new IllegalStateException("utility class!!");
    }
    public static void showErrorAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
