package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.RegistrationBean;
import com.example.fromfridgetoplate.logic.control.RegisterController;
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
    private TextField SurnameText;
    @FXML
    private TextField cityText;

    @FXML
    private Button signInButton;

    Navigator navigator = Navigator.getInstance(null);

    @Override
    public void initialize(URL location,
                           ResourceBundle resources) {
        signInButton.setOnMouseClicked(event -> {
            RegistrationBean registrationBean;
            if (emailText.getText().isEmpty() || passwordText.getText().isEmpty() || nameText.getText().isEmpty() || SurnameText.getText().isEmpty() || cityText.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Complete the field before!");
                alert.showAndWait();
            }
            else{
                registrationBean = new RegistrationBean(emailText.getText(), passwordText.getText(),nameText.getText(),SurnameText.getText(), Role.RIDER, cityText.getText());
                RegisterController registerController = new RegisterController();
                if(registerController.register_rider(registrationBean)){
                    try {
                        navigator.goTo("loginPage.fxml");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}

