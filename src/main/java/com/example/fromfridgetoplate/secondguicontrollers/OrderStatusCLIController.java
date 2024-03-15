package com.example.fromfridgetoplate.secondguicontrollers;


import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.OrderListBean;
import com.example.fromfridgetoplate.logic.control.PendingOrdersController;

import java.util.List;
import java.util.Scanner;

public class OrderStatusCLIController {


    public void displayAssignedOrders() {

        PendingOrdersController pendingOrdersController = new PendingOrdersController();
        OrderListBean assignedOrdersBean = pendingOrdersController.getAssignedOrdersBean();
        List<OrderBean> orders = assignedOrdersBean.getOrderBeans();


        Utils.print("Ordini Assegnati:");
        for (OrderBean order : orders) {
            Utils.print(formatOrder(order));
        }


        handleUserInput();
    }

    private String formatOrder(OrderBean order) {

        return String.format("ID Ordine: %d, Stato: %s, Cliente: %s, Rivenditore: %s, Ora Ordine: %s, Rider: %d, Città Spedizione: %s",
                order.getOrderId(), order.getStatus(), order.getCustomerId(), order.getShopId(), order.getOrderTime(), order.getRiderId(), order.getShippingCity());
    }

    private void handleUserInput() {

        Scanner scanner = new Scanner(System.in);
        Utils.print("\nPremi 'r' per aggiornare la lista degli ordini, qualsiasi altra chiave per uscire.");
        String input = scanner.nextLine().trim();
        if ("r".equals(input)) {
            displayAssignedOrders(); // Refresh della lista degli ordini, cosi simulo un loop
        } else {
            Utils.print("Uscita dallo stato degli ordini.");
        }
    }

}
