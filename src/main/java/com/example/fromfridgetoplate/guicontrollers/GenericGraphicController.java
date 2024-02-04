package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.Session;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GenericGraphicController implements Initializable {
    @FXML
    private Button profileButton;
    @FXML
    private Button homeButton;

    Navigator navigator = Navigator.getInstance(null);


    //method called as soon as the view is loaded
    @Override
    public void initialize(URL location,
                           ResourceBundle resources) {

        homeButton.setOnMouseClicked(event -> {
            try {
                if(Session.getSession().getUserBean().getRole() == Role.CLIENT) {
                    navigator.goTo("clientHomePage.fxml");
                }
                else if(Session.getSession().getUserBean().getRole() == Role.OWNER){
                    navigator.goTo("resellerMainPage2.fxml");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        profileButton.setOnMouseClicked(event -> {
            try {
                if(Session.getSession().getUser().getRole() == Role.CLIENT) {
                    navigator.goTo("profilePage.fxml"); //non esiste lol
                }
                 else if(Session.getSession().getUser().getRole() == Role.OWNER){
                     navigator.goTo("shopProfilePage.fxml");
                 }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
