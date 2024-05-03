package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.guicontrollers.IRiderSelectionListener;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.control.NotificationManager;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.OrderAssignmentException;


public class RiderSelectionListenerCLI implements IRiderSelectionListener {

    @Override
    public void onRiderSelected(RiderBean selectedRiderBean, OrderBean orderBean) {
        assignOrderToRiderCLI(orderBean, selectedRiderBean);

        Printer.print("Notifica inviata al rider " + selectedRiderBean.getName() + " " + selectedRiderBean.getSurname() +
                " per l'ordine ID: " + orderBean.getOrderId() + " in " + selectedRiderBean.getAssignedCity());
    }

    private void assignOrderToRiderCLI(OrderBean orderBean, RiderBean riderBean) {

        Printer.print("L'ordine ID: " + orderBean.getOrderId() + " è stato assegnato al rider ID: " + riderBean.getId());

        NotificationManager notManager = new NotificationManager();
        try {
            notManager.notifyRider(riderBean, orderBean);
            Printer.print("Notifica inviata con successo al rider ID: " + riderBean.getId());
        } catch (OrderAssignmentException e) {
            Printer.print("Errore durante la notifica al rider: " + e.getMessage());
        }catch (DAOException e) {
            Printer.print("Errore durante il recupero della disponibilità del rider: " + e.getMessage());
        }
    }
}

