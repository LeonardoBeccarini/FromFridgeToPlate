package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.Utils;
import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.RiderHPController;
import com.example.fromfridgetoplate.logic.exceptions.RiderGcException;

import java.util.Scanner;

public class RiderCurrentOrderCLIController {

    public void loadOrderDetails() {

        RiderHPController riderController = new RiderHPController();
        try {
            RiderBean riderBean = riderController.getRiderDetailsFromSession();
            OrderBean currentOrderBean = riderController.getInDeliveryOrderForRider(riderBean);

            if (currentOrderBean != null) {
                Utils.print("\n*** Dettagli dell'Ordine Corrente ***");
                Utils.print("Id dell'ordine: " + currentOrderBean.getOrderId());
                Utils.print("Via: " + currentOrderBean.getShippingAddress().getShippingStreet());
                Utils.print("Città: " + currentOrderBean.getShippingAddress().getShippingCity());
                Utils.print("Provincia: " + currentOrderBean.getShippingAddress().getShippingProvince());
                Utils.print("Numero civico: " + currentOrderBean.getShippingAddress().getShippingStreetNumber());

                confirmDelivery(riderController, currentOrderBean);
            } else {
                Utils.print("Attualmente non hai ordini in consegna. Per favore, accetta un ordine nella sezione Notifiche.");
            }
        } catch (RiderGcException e) {
            Utils.print("Errore: " + e.getMessage());
            Throwable cause = e.getCause();
            if (cause != null) {
                Utils.print("Causa: " + cause.getMessage());
            }
        }
    }

    private void confirmDelivery(RiderHPController riderController, OrderBean currentOrderBean) {
        Utils.print("\nVuoi confermare la consegna di questo ordine? (sì/no):");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim().toLowerCase();

        if ("sì".equals(input) || "si".equals(input)) {
            try {
                riderController.confirmDelivery(currentOrderBean);
                Utils.print("Complimenti. Hai completato la consegna!");
                Utils.print("Controlla le tue notifiche, per verificare se ci sono nuovi ordini per te!");
            } catch (Exception e) {
                Utils.print("Errore nella conferma della consegna: " + e.getMessage());
            }
        } else {
            Utils.print("Consegna non confermata. Puoi confermare la consegna in qualsiasi momento.");
        }
    }

    public static void main(String[] args) {
        RiderCurrentOrderCLIController controller = new RiderCurrentOrderCLIController();
        controller.loadOrderDetails();
    }
}

