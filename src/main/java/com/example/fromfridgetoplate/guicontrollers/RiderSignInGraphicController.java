package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.RegistrationBean;
import com.example.fromfridgetoplate.logic.control.RegisterController;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Role;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RiderSignInGraphicController implements Initializable {
    @FXML
    private TextField emailText;
    @FXML
    private TextField passwordText;
    @FXML
    private TextField nameText;

    @FXML
    private TextField surnameText;
    @FXML
    private TextField cityText;

    @FXML
    private Button signInButton;
    @FXML
    private Button backButton;

    Navigator navigator = Navigator.getInstance(null);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signInButton.setOnMouseClicked(event -> {
            RegistrationBean registrationBean;
            if (emailText.getText().isEmpty() || passwordText.getText().isEmpty() || nameText.getText().isEmpty() || surnameText.getText().isEmpty() || cityText.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Complete the field before!");
                alert.showAndWait();
            }
            else{
                registrationBean = new RegistrationBean(emailText.getText(), passwordText.getText(),nameText.getText(),surnameText.getText(), Role.RIDER, cityText.getText());
                RegisterController registerController = new RegisterController();
                try {
                    if(registerController.register(registrationBean)){
                        navigator.goTo("loginPage.fxml");
                    }
                } catch ( IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                    alert.showAndWait();
                }catch(DAOException e) {
                    GUIUtils.showErrorAlert("Errore di scittura/lettura", "Errore nel salvataggio delle credenziali del rider", "" + e.getMessage());
                }
            }
        });
        backButton.setOnMouseClicked(event ->
        {
            try {
                navigator.goTo("mainPage6.fxml");
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.showAndWait();
            }
        });
    }
}

