package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.Utils;
import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.RiderHPController;
import com.example.fromfridgetoplate.logic.bean.OrderListBean;
import java.time.format.DateTimeFormatter;

public class RiderDeliveryReportCLIController {

    public void showDeliveryReport() {
        Utils.print("\n*** Report delle Consegne ***");
        RiderHPController riderController = new RiderHPController();
        RiderBean riderBean = riderController.getRiderDetailsFromSession();
        if (riderBean != null) {
            try {
                OrderListBean deliveredOrders = riderController.getConfirmedDeliveries(riderBean);
                if (deliveredOrders.getOrderBeans().isEmpty()) {
                    Utils.print("Non ci sono consegne confermate da mostrare.");
                    return;
                }

                Utils.print(String.format("%-10s %-12s %-12s %-20s %-7s", "Order ID", "Customer ID", "Shop ID", "Order Time", "Rider ID"));
                for (OrderBean order : deliveredOrders.getOrderBeans()) {
                    String formattedOrderTime = order.getOrderTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    Utils.print(String.format("%-10d %-12s %-12s %-20s %-7d", order.getOrderId(), order.getCustomerId(), order.getShopId(), formattedOrderTime, order.getRiderId()));
                }

            } catch (Exception e) {
                Utils.print("Si è verificato il seguente errore: " + e.getMessage());
                Throwable cause = e.getCause();
                if (cause != null) {
                    Utils.print("Causa: " + cause.getMessage());
                }
            }
        } else {
            Utils.print("Dettagli del rider non trovati. Prova a loggarti di nuovo");
        }
    }

    public static void main(String[] args) {
        RiderDeliveryReportCLIController controller = new RiderDeliveryReportCLIController();
        controller.showDeliveryReport();
    }
}
