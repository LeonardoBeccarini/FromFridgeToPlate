package com.example.fromfridgetoplate.guicontrollers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ClientHomePageGraphicController extends GenericGraphicController{
    @FXML
    private ImageView makeOrderImage;

    @FXML
    private StackPane stackpaneId; // questo contiene pendingOrdersImg

    @FXML
    private StackPane stackpaneId2; // questo contiene viewStatusImg

    @FXML
    private ImageView viewStatusImg;

    @FXML
    void onClick(MouseEvent event) throws IOException {
        Node sourceNode = (Node) event.getSource() ;
        if(sourceNode == makeOrderImage){
            navigator.goTo("marketListPage.fxml");
        } else if (sourceNode == viewStatusImg) {
            // navigator.goTo(la view di viewStatus);
        }
    }


    @FXML // bo?
    void zoom(MouseEvent event) {
        // Logica per lo zoom (se necessaria)
    }

    // Metodi per gestire l'ingrandimento e il cambio di colore del bordo, quando il mouse passa sopra le imageview
    //
    @FXML
    void onMouseEnteredForStackPane1(MouseEvent event) {
        //pendingOrdersImg.setScaleX(1.05);
        //pendingOrdersImg.setScaleY(1.05);
        stackpaneId.setScaleX(1.05);
        stackpaneId.setScaleY(1.05);
        stackpaneId.setStyle("-fx-border-color: blue; -fx-border-width: 2; -fx-border-style: solid;");
    }

    @FXML
    void onMouseExitedForStackPane1(MouseEvent event) {
        //pendingOrdersImg.setScaleX(1.0);
        //pendingOrdersImg.setScaleY(1.0);
        stackpaneId.setScaleX(1.0); // Reimposta la scala del StackPane
        stackpaneId.setScaleY(1.0);
        stackpaneId.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-style: solid;");
    }

    @FXML
    void onMouseEnteredForStackPane2(MouseEvent event) {
        //viewStatusImg.setScaleX(1.05);
        //viewStatusImg.setScaleY(1.05);
        stackpaneId2.setScaleX(1.05);
        stackpaneId2.setScaleY(1.05);
        stackpaneId2.setStyle("-fx-border-color: blue; -fx-border-width: 2; -fx-border-style: solid;");
    }

    @FXML
    void onMouseExitedForStackPane2(MouseEvent event) {
        //viewStatusImg.setScaleX(1.0);
        //viewStatusImg.setScaleY(1.0);
        stackpaneId2.setScaleX(1.0);
        stackpaneId2.setScaleY(1.0);
        stackpaneId2.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-style: solid;");
    }
}


