package com.example.fromfridgetoplate.guicontrollers;


import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.RiderHPController;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.RiderGcException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RiderCurrentOrderGraphicController extends GenericGraphicController {

    @FXML
    private Label orderIdLabel;
    @FXML
    private Label shippingStreetLabel;
    @FXML
    private Label shippingCityLabel;
    @FXML
    private Label shippingProvinceLabel;
    @FXML
    private Label shippingStreetNumberLabel;

    @FXML
    private Button confirmDeliveryButton;
    private OrderBean currentOrderBean;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        loadOrderDetails();
    }

    public void loadOrderDetails()  {

        try {
            RiderHPController riderCtrl = new RiderHPController();
            RiderBean riderBn = riderCtrl.getRiderDetailsFromSession();

            this.currentOrderBean = riderCtrl.getInDeliveryOrderForRider(riderBn);

            orderIdLabel.setText("Id dell'ordine: " + currentOrderBean.getOrderId());
            shippingStreetLabel.setText("Via: " + currentOrderBean.getShippingAddress().getShippingStreet());
            shippingCityLabel.setText("Città: " + currentOrderBean.getShippingAddress().getShippingCity());
            shippingProvinceLabel.setText("Provincia: " + currentOrderBean.getShippingAddress().getShippingProvince());
            shippingStreetNumberLabel.setText("Numero civico: " + currentOrderBean.getShippingAddress().getShippingStreetNumber());

        }catch(RiderGcException e) {
            showNoOrderAlert(e);
        } catch(DAOException e) {
            GUIUtils.showErrorAlert("Errore di caricamento", "Errore nel recupero delle informazioni del rider", "" + e.getMessage());
        }
    }


    private void showNoOrderAlert(RiderGcException e) {

        String errorMessage = e.getMessage();
        Throwable cause = e.getCause();
        if (cause != null) {

            errorMessage += " Causa: " + cause.getMessage();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nessun ordine in consegna");
        alert.setHeaderText(null);
        alert.setContentText("Attualmente non hai ordini in consegna. Per favore, accetta un ordine nella sezione Notifiche.\n\nDettagli Errore: " + errorMessage);
        alert.showAndWait();
    }


    @FXML
    private void handleConfirmDelivery(ActionEvent event) {
        RiderHPController riderCtrl = new RiderHPController();
        try {
            riderCtrl.confirmDelivery(currentOrderBean);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Ordine correttamente consegnato");
            alert.setHeaderText("Complimenti. Hai completato la consegna!");
            alert.setContentText("Controlla le tue notifiche, per verificare se ci sono nuovi ordini per te!");
            alert.showAndWait();
            navigator.goTo("RiderDeliveryReport.fxml");
        } catch (DAOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Errore di Consegna");
            alert.setHeaderText("Errore durante la conferma della consegna");
            alert.setContentText("Dettagli errore: " + e.getMessage());
            alert.showAndWait();
        } catch (IOException e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Errore del Navigator");
            alert.setHeaderText("Errore durante il cambio di scena");
            alert.setContentText("Dettagli errore: " + e.getMessage());
            alert.showAndWait();
        }

    }




}
