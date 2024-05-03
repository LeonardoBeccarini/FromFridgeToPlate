package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.NotificationManager;
import com.example.fromfridgetoplate.logic.model.Order;
import javafx.scene.control.Alert;

public class RiderSelectionListener implements IRiderSelectionListener {

    public void onRiderSelected(RiderBean selectedRiderBean, OrderBean orderBean) { // in realtà dovrà mandare una notifica al rider

        assignOrderToRider(orderBean, selectedRiderBean);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Sending messagge...");
        alert.setHeaderText("Notifica al rider inviata");
        alert.setContentText("Il rider:\n" +
                "Nome: " + selectedRiderBean.getName() + "\n" +
                "Cognome: " + selectedRiderBean.getSurname() + "\n" +
                "Città Assegnata: " + selectedRiderBean.getAssignedCity() + "\n" +
                "è stato contattato, attendere la conferma.");
        alert.showAndWait();
    }


    private void assignOrderToRider(OrderBean orderBn, RiderBean riderBn) {

        NotificationManager notManager = new NotificationManager();
        notManager.notifyRider(riderBn, orderBn);

    }



}
