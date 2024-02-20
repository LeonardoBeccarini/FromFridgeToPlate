package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.NotificationBean;

import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.RiderHPController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

// -----------------------------------------notification page per il RIDER----------------------------------------------
public class RiderNotificationPageGraphicController extends GenericGraphicController  {



    @FXML
    private Button backButton;


    @FXML
    private TableView<NotificationBean> notTable;

    @FXML
    private TableColumn<NotificationBean, Integer> orderColumn;

    @FXML
    private TableColumn<NotificationBean, String> streetColumn;

    @FXML
    private TableColumn<NotificationBean, String> cityColumn;

    @FXML
    private TableColumn<NotificationBean, String> msgColumn;

    @FXML
    private TableColumn<NotificationBean, String> provinceColumn;

    @FXML
    private TableColumn<NotificationBean, Integer> streetNumberColumn;

    @FXML
    private Button acceptButton;

    @FXML
    private Button declineButton;

    private RiderHomePageGraphicController riderGC;


    @FXML
    void goBack(ActionEvent event) throws IOException { // non funziona gesucristo

        //riderGC.goOnline(event);

        // come gestisco il cambio scena ??

        if (riderGC != null) {
            Scene existingScene = riderGC.getRootNode().getScene();
            if (existingScene != null) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(existingScene);
                stage.show();
            } else {
                System.out.println("error in goback");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        super.initialize(location, resources);
        assert notTable != null : "fx:id=\"notTable\" was not injected: check your FXML file 'Untitled'.";
        assert orderColumn != null : "fx:id=\"orderColumn\" was not injected: check your FXML file 'Untitled'.";
        assert streetColumn != null : "fx:id=\"streetColumn\" was not injected: check your FXML file 'Untitled'.";

        orderColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        msgColumn.setCellValueFactory(new PropertyValueFactory<>("messageText"));
        provinceColumn.setCellValueFactory(new PropertyValueFactory<>("province"));
        streetNumberColumn.setCellValueFactory(new PropertyValueFactory<>("streetNumber"));

        notTable.setOnMouseClicked(event -> {
            boolean isRowSelected = notTable.getSelectionModel().getSelectedItem() != null;
            acceptButton.setDisable(!isRowSelected);
            declineButton.setDisable(!isRowSelected);
        });
    }

    @FXML
    private void handleAccept(ActionEvent event) {
        NotificationBean selectedNotification = notTable.getSelectionModel().getSelectedItem();
        if (selectedNotification != null) {
            RiderHPController riderCtrl = new RiderHPController();
            RiderBean currentRider = riderCtrl.getRiderDetailsFromSession();

            // verifichiamo se il rider ha già un ordine in consegna
            boolean hasOrderInDelivery = riderCtrl.checkForOrderInDelivery(currentRider);

            if (hasOrderInDelivery) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Consegna in corso");
                alert.setHeaderText("Hai già un ordine in consegna");
                alert.setContentText("Devi completare la consegna corrente prima di accettarne un'altra.");
                alert.showAndWait();
            } else {
                // Se non ci sono ordini in consegna, allora siprocederà con l'accettazione dell'ordine
                riderCtrl.acceptOrder(selectedNotification);
                System.out.println("Accettato incarico per l'ordine ID: " + selectedNotification.getOrderId());


                riderGC.SetNotificationAsRead(selectedNotification);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Consegna accettata");
                alert.setHeaderText("Hai preso in carico l'ordine");
                alert.setContentText("con orderId: " + selectedNotification.getOrderId());
                alert.showAndWait();
            }
        }
    }



    @FXML
    private void handleDecline(ActionEvent event) {
        NotificationBean selectedNotification = notTable.getSelectionModel().getSelectedItem();
        if (selectedNotification != null) {

            RiderHPController riderCtrl = new RiderHPController();
            riderCtrl.declineOrder(selectedNotification);
            System.out.println("Rifiutato incarico per l'ordine ID: " + selectedNotification.getOrderId());
        }

        riderGC.SetNotificationAsRead(selectedNotification);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle("Hai rifiutato l'ordine");

        alert.setContentText("Hai rifiutato l'ordine con orderId : "+ selectedNotification.getOrderId());
        alert.showAndWait();
    }

    public void update(ObservableList<NotificationBean> notificationBeans) {

        notTable.setItems(notificationBeans);
    }

    public void setCallback(RiderHomePageGraphicController riderHomePageGraphicController) {
        riderGC = riderHomePageGraphicController;
    }
}
