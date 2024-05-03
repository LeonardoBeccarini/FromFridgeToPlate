package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.UserBean;
import com.example.fromfridgetoplate.logic.control.LoginController;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
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
            navigateByRole(loggedUser);
        } catch (NotExistentUserException e) {
            showAlert("Errore, le credenziali non corrispondono a nessun utente: " + e.getMessage());
        } catch (DAOException e) {
            GUIUtils.showErrorAlert("Errore nel salvataggio delle credenziali", "Errore nello strato di persistenza", "causa errore:" + e.getMessage());
        }
    }

    private void handleSignIn() {
        try {
            navigator.goTo("chooseUserPage.fxml");
        } catch (IOException e) {
            GUIUtils.showErrorAlert("Errore di Navigazione", "Impossibile caricare la pagina", "Non è stato possibile navigare alla pagina desiderata. Si prega di riprovare.");
        }
    }


    private void navigateByRole(UserBean loggedUser) {
        try {
            if (Objects.requireNonNull(loggedUser).getRole() == Role.CLIENT) {
                navigator.goTo("clientHomePage.fxml");
            } else if (loggedUser.getRole() == Role.RIDER) {
                navigator.goTo("riderMainPage.fxml");
            } else if (loggedUser.getRole() == Role.OWNER) {
                navigator.goTo("resellerMainPage2.fxml");
            }
        } catch (IOException e) {
            GUIUtils.showErrorAlert("Errore di Navigazione", "Impossibile caricare la pagina", "Non è stato possibile navigare alla pagina specificata in base al ruolo. ");
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.showAndWait();
    }
}
