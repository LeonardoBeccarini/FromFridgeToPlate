package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ResellerMainPageGraphicController extends GenericGraphicController {
        @FXML
        private ImageView pendingOrdersImg;

        @FXML
        private StackPane stackpaneId; // questo contiene pendingOrdersImg

        @FXML
        private StackPane stackpaneId2; // questo contiene viewStatusImg

        @FXML
        private ImageView viewStatusImg;
        @FXML
        private Button notificationButton;
        private List<NotificationBean> notificationBeanList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int counter;
        MakeOrderControl makeOrderControl = new MakeOrderControl();
        try {
            notificationBeanList = makeOrderControl.loadNotification();
        } catch (DbException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }

        counter = notificationBeanList.size();
        notificationButton.setText("Notification" + " " + "(" + counter + ")");
        if(counter>0){
            notificationButton.setStyle("-fx-background-color: #FF0000");
        }
        super.initialize(location, resources);
    }



    @FXML // bo?
    void zoom(MouseEvent event) {
        // Logica per lo zoom (se necessaria)
    }

    @FXML
       public  void onClick(MouseEvent event) throws IOException {
            Node sourceNode = (Node) event.getSource() ;
           if(sourceNode == pendingOrdersImg){
               navigator.goTo("viewPendingOrders2.fxml");
           } else if (sourceNode == viewStatusImg) {
               navigator.goTo("OrderStatusPage.fxml");
           }
           else if(sourceNode == notificationButton){
               navigator.goToWithController("resellerNotificationPage.fxml", new ResellerNotificationGraphicController(notificationBeanList));
           }
        }


        // Metodi per gestire l'ingrandimento e il cambio di colore del bordo, quando il mouse passa sopra le imageview
        //
        @FXML
        void onMouseEnteredForStackPane1(MouseEvent event) {

            stackpaneId.setScaleX(1.05);
            stackpaneId.setScaleY(1.05);
            stackpaneId.setStyle("-fx-border-color: blue; -fx-border-width: 2; -fx-border-style: solid;");
        }

        @FXML
        void onMouseExitedForStackPane1(MouseEvent event) {

            stackpaneId.setScaleX(1.0); // Reimposta la scala del StackPane
            stackpaneId.setScaleY(1.0);
            stackpaneId.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-style: solid;");
        }

        @FXML
        void onMouseEnteredForStackPane2(MouseEvent event) {

            stackpaneId2.setScaleX(1.05);
            stackpaneId2.setScaleY(1.05);
            stackpaneId2.setStyle("-fx-border-color: blue; -fx-border-width: 2; -fx-border-style: solid;");
        }

        @FXML
        void onMouseExitedForStackPane2(MouseEvent event) {

            stackpaneId2.setScaleX(1.0);
            stackpaneId2.setScaleY(1.0);
            stackpaneId2.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-style: solid;");
        }
    }

