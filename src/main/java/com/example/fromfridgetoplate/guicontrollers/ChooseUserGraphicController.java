package com.example.fromfridgetoplate.guicontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class    ChooseUserGraphicController implements Initializable {

    @FXML
    private RadioButton clientButton;

    @FXML
    private RadioButton ownerButton;

    @FXML
    private Button continueButton;

    @FXML
    private Button backButton;

    @FXML
    private RadioButton riderButton;

    Navigator navigator = Navigator.getInstance(null);

    @Override
    public void initialize(URL location,
                           ResourceBundle resources) {
        continueButton.setOnMouseClicked(event ->{
            try {
                if(clientButton.isSelected()){

                    navigator.goTo("clientSigninPage.fxml");

                } else if (ownerButton.isSelected()) {
                    navigator.goTo("shopOwnerSigninPage.fxml");

                }else if (riderButton.isSelected()){
                    navigator.goTo("riderSignInPage.fxml");
                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.showAndWait();
            }
        });
        backButton.setOnMouseClicked(event ->{
            try {
                navigator.goTo("mainPage6.fxml");
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.showAndWait();
            }
        });
    }

}
