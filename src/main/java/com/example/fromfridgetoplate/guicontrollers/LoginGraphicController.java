package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.UserBean;
import com.example.fromfridgetoplate.logic.control.LoginController;
import com.example.fromfridgetoplate.logic.exceptions.NotExistentUserException;
import com.example.fromfridgetoplate.logic.model.Role;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginGraphicController implements Initializable {
    @FXML
    private Button loginButton;
    @FXML
    private Button signInButton;
    @FXML
    private TextField emailText;
    @FXML
    private PasswordField pwdText;

    Navigator navigator = Navigator.getInstance(null);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnMouseClicked(event -> handleLogin());
        signInButton.setOnMouseClicked(event -> handleSignIn());
    }

    private void handleLogin() {
        if (emailText.getText().isEmpty() || pwdText.getText().isEmpty()) {
            showAlert("Complete the field before!");
            return;
        }

        UserBean userBean = new UserBean(emailText.getText(), pwdText.getText());
        LoginController loginController = new LoginController();
        UserBean loggedUser = null;

        try {
            loggedUser = loginController.login(userBean);
            navigateBasedOnRole(loggedUser);
        } catch (NotExistentUserException e) {
            showAlert("Errore login: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSignIn() {
        try {
            navigator.goTo("chooseUserPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateBasedOnRole(UserBean loggedUser) throws IOException {
        if (Objects.requireNonNull(loggedUser).getRole() == Role.CLIENT) {
            navigator.goTo("clientHomePage.fxml");
        } else if (loggedUser.getRole() == Role.RIDER) {
            navigator.goTo("riderMainPage.fxml");
        } else if (loggedUser.getRole() == Role.OWNER) {
            navigator.goTo("resellerMainPage2.fxml");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.showAndWait();
    }
}
