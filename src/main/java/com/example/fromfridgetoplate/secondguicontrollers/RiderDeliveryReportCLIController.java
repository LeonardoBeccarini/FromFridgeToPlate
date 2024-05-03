package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.RiderHPController;
import com.example.fromfridgetoplate.logic.bean.OrderListBean;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.RiderGcException;

import java.time.format.DateTimeFormatter;

public class RiderDeliveryReportCLIController {

    public void showDeliveryReport() {
        Printer.print("\n*** Report delle Consegne ***");
        RiderHPController riderController = new RiderHPController();

        try {
            RiderBean riderBean = riderController.getRiderDetailsFromSession();
            if (riderBean == null) {
                Printer.print("Dettagli del rider non trovati. Prova a loggarti di nuovo.");
                return;
            }

            OrderListBean deliveredOrders = riderController.getConfirmedDeliveries(riderBean);
            if (deliveredOrders.getOrderBeans().isEmpty()) {
                Printer.print("Non ci sono consegne confermate da mostrare.");
                return;
            }

            Printer.print(String.format("%-10s %-12s %-12s %-20s %-7s", "Order ID", "Customer ID", "Shop ID", "Order Time", "Rider ID"));
            for (OrderBean order : deliveredOrders.getOrderBeans()) {
                String formattedOrderTime = order.getOrderTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                Printer.print(String.format("%-10d %-12s %-12s %-20s %-7d", order.getOrderId(), order.getCustomerId(), order.getShopId(), formattedOrderTime, order.getRiderId()));
            }
        } catch (DAOException | RiderGcException e) {
            Printer.print("Errore durante il recupero delle consegne: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        RiderDeliveryReportCLIController controller = new RiderDeliveryReportCLIController();
        controller.showDeliveryReport();
    }
}
