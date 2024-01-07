
package com.example.fromfridgetoplate.guicontrollers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class RiderHomePageGraphicController {

    @FXML
    private Button homeButton;

    @FXML
    private ImageView msgImage;

    @FXML
    private Button notificationsButton;

    @FXML
    private ImageView orderImage;

    @FXML
    private Button profileButton;

    @FXML
    private ImageView reportImg;

    @FXML
    private StackPane stackpaneId;

    @FXML
    private StackPane stackpaneId2;

    @FXML
    private StackPane stackpaneId3;

    @FXML
    void onClick(MouseEvent event) {

    }

    @FXML
    void initialize() {
        assert homeButton != null : "fx:id=\"homeButton\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert msgImage != null : "fx:id=\"msgImage\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert notificationsButton != null : "fx:id=\"notificationsButton\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert orderImage != null : "fx:id=\"orderImage\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert profileButton != null : "fx:id=\"profileButton\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert reportImg != null : "fx:id=\"reportImg\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert stackpaneId != null : "fx:id=\"stackpaneId\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert stackpaneId2 != null : "fx:id=\"stackpaneId2\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert stackpaneId3 != null : "fx:id=\"stackpaneId3\" was not injected: check your FXML file 'riderMainPage.fxml'.";

    }

}
