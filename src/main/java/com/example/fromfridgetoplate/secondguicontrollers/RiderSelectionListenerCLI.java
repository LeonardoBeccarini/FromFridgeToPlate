package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.guicontrollers.IRiderSelectionListener;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.control.NotificationManager;


public class RiderSelectionListenerCLI implements IRiderSelectionListener {

    @Override
    public void onRiderSelected(RiderBean selectedRiderBean, OrderBean orderBean) {
        assignOrderToRiderCLI(orderBean, selectedRiderBean);

        Utils.print("Notifica inviata al rider " + selectedRiderBean.getName() + " " + selectedRiderBean.getSurname() +
                " per l'ordine ID: " + orderBean.getOrderId() + " in " + selectedRiderBean.getAssignedCity());
    }

    private void assignOrderToRiderCLI(OrderBean orderBean, RiderBean riderBean) {

        Utils.print("L'ordine ID: " + orderBean.getOrderId() + " è stato assegnato al rider ID: " + riderBean.getId());

        NotificationManager notManager = new NotificationManager();
        notManager.notifyRider(riderBean, orderBean);
    }
}

