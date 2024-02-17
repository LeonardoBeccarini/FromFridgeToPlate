package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.NotificationManager;
import com.example.fromfridgetoplate.logic.model.Order;
import javafx.scene.control.Alert;

public class RiderSelectionListener {

    void onRiderSelected(RiderBean selectedRiderBean, OrderBean orderBean) { // in realtà dovrà mandare una notifica al rider

        /* Alert alert = new Alert(Alert.AlertType.INFORMATION);
        System.out.println("L'ordine è stato preso in carico dal rider : " + selectedRiderBean.getName());

        alert.setTitle("Conferma Assegnazione Rider");
        alert.setHeaderText("Assegnazione Rider Completata");
        alert.setContentText("L'ordine è stato preso in carico dal rider:\n" +
                    "Nome: " + selectedRiderBean.getName() + "\n" +
                    "Cognome: " + selectedRiderBean.getSurname() + "\n" +
                    "Città Assegnata: " + selectedRiderBean.getAssignedCity());
        alert.showAndWait(); */

        assignOrderToRider(orderBean, selectedRiderBean);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        System.out.println("Al rider : " + selectedRiderBean.getName() + " è stata inviata una notifica per l'incarico di consegna.");

        alert.setTitle("Sending messagge...");
        alert.setHeaderText("Notifica al rider inviata");
        alert.setContentText("Il rider:\n" +
                "Nome: " + selectedRiderBean.getName() + "\n" +
                "Cognome: " + selectedRiderBean.getSurname() + "\n" +
                "Città Assegnata: " + selectedRiderBean.getAssignedCity() + "\n" +
                "è stato contattato, attendere la conferma.");
        alert.showAndWait();
    }



    // questa impl nel controller applicativo
    private void assignOrderToRider(OrderBean orderBn, RiderBean riderBn) {


        System.out.println("assignOrderToRider");
        //NotificationManager.getInstance().notifyObservers(riderBn.getId(), orderBn);

        NotificationManager.getInstance().notifyRider(riderBn, orderBn);

    }







}
