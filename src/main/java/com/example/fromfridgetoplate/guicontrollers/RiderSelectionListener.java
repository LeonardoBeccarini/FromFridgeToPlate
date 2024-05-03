package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.NotificationManager;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.OrderAssignmentException;

import javafx.scene.control.Alert;

public class RiderSelectionListener implements IRiderSelectionListener {


    public void onRiderSelected(RiderBean selectedRiderBean, OrderBean orderBean) {
        try {
            assignOrderToRider(orderBean, selectedRiderBean);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sending message...");
            alert.setHeaderText("Notifica al rider inviata");
            alert.setContentText("Il rider:\n" +
                    "Nome: " + selectedRiderBean.getName() + "\n" +
                    "Cognome: " + selectedRiderBean.getSurname() + "\n" +
                    "Città Assegnata: " + selectedRiderBean.getAssignedCity() + "\n" +
                    "è stato contattato, attendere la conferma.");
            alert.showAndWait();
        } catch (OrderAssignmentException e) {
            showErrorAlert("Errore", "Errore nell'assegnazione dell'ordine", "Dettaglio errore: " + e.getMessage());
        } catch (DAOException e) {
            showErrorAlert("Errore nell' accesso allo strato di persistenza", "Problema di accesso alla persistenza delle notifiche inviate", "Dettaglio errore: " + e.getMessage());
        }
    }

    private void showErrorAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private void assignOrderToRider(OrderBean orderBn, RiderBean riderBn) throws OrderAssignmentException, DAOException {

        NotificationManager notManager = new NotificationManager();
        notManager.notifyRider(riderBn, orderBn);

    }



}
