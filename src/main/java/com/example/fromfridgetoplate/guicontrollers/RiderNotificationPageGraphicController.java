package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.RiderHPController;
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

// -----------------------------------------notification page per il RIDER----------------------------------------------
public class RiderNotificationPageGraphicController {



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
    void goBack(ActionEvent event) {
        try {
            if (riderGC != null) {
                Scene existingScene = riderGC.getRootNode().getScene();
                if (existingScene != null) {
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(existingScene);
                    stage.show();
                } else {
                    throw new IllegalStateException("La scena precedente non è disponibile.");
                }
            }
        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di navigazione");
            alert.setHeaderText(null);
            alert.setContentText("Impossibile tornare alla vista precedente: " + e.getMessage());
            alert.showAndWait();
        }
    }




    public void initialize() {


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
            try {
                RiderHPController riderCtrl = new RiderHPController();
                RiderBean currentRider = riderCtrl.getRiderDetailsFromSession();
                // verifica se il rider ha già un ordine in consegna
                boolean hasOrderInDelivery = riderCtrl.checkForOrderInDelivery(currentRider);

                if (hasOrderInDelivery) {
                    throw new IllegalStateException("Hai già un ordine in consegna.");
                } else {
                    riderCtrl.acceptOrder(selectedNotification);
                    riderGC.SetNotificationAsRead(selectedNotification);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Consegna accettata");
                    alert.setHeaderText("Hai preso in carico l'ordine");
                    alert.setContentText("con orderId: " + selectedNotification.getOrderBean().getOrderId());
                    alert.showAndWait();
                }
            } catch (Exception e) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore nell'accettazione dell'ordine");
                alert.setHeaderText(null);
                alert.setContentText("Non è stato possibile accettare l'ordine: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }



    @FXML
    private void handleDecline(ActionEvent event) {
        NotificationBean selectedNotification = notTable.getSelectionModel().getSelectedItem();
        if (selectedNotification != null) {
            try {
                RiderHPController riderCtrl = new RiderHPController();
                riderCtrl.declineOrder(selectedNotification);
                riderGC.SetNotificationAsRead(selectedNotification);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ordine rifiutato");
                alert.setHeaderText(null);
                alert.setContentText("Hai rifiutato l'ordine con orderId: " + selectedNotification.getOrderBean().getOrderId());
                alert.showAndWait();

            } catch (Exception e) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore nel rifiuto dell'ordine");
                alert.setHeaderText(null);
                alert.setContentText("Non è stato possibile rifiutare l'ordine: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }


    public void update(ObservableList<NotificationBean> notificationBeans) {

        notTable.setItems(notificationBeans);
    }

    public void setCallback(RiderHomePageGraphicController riderHomePageGraphicController) {
        riderGC = riderHomePageGraphicController;
    }
}
