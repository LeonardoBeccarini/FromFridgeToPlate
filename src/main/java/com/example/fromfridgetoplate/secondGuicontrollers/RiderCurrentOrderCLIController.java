package com.example.fromfridgetoplate.secondGuicontrollers;

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
                System.out.println("\n*** Dettagli dell'Ordine Corrente ***");
                System.out.println("Id dell'ordine: " + currentOrderBean.getOrderId());
                System.out.println("Via: " + currentOrderBean.getShippingAddress().getShippingStreet());
                System.out.println("Città: " + currentOrderBean.getShippingAddress().getShippingCity());
                System.out.println("Provincia: " + currentOrderBean.getShippingAddress().getShippingProvince());
                System.out.println("Numero civico: " + currentOrderBean.getShippingAddress().getShippingStreetNumber());

                confirmDelivery(riderController, currentOrderBean);
            } else {
                System.out.println("Attualmente non hai ordini in consegna. Per favore, accetta un ordine nella sezione Notifiche.");
            }
        } catch (RiderGcException e) {
            System.out.println("Errore: " + e.getMessage());
            Throwable cause = e.getCause();
            if (cause != null) {
                System.out.println("Causa: " + cause.getMessage());
            }
        }
    }

    private void confirmDelivery(RiderHPController riderController, OrderBean currentOrderBean) {
        System.out.println("\nVuoi confermare la consegna di questo ordine? (sì/no):");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim().toLowerCase();

        if ("sì".equals(input) || "si".equals(input)) {
            try {
                riderController.confirmDelivery(currentOrderBean);
                System.out.println("Complimenti. Hai completato la consegna!");
                System.out.println("Controlla le tue notifiche, per verificare se ci sono nuovi ordini per te!");
            } catch (Exception e) {
                System.out.println("Errore nella conferma della consegna: " + e.getMessage());
            }
        } else {
            System.out.println("Consegna non confermata. Puoi confermare la consegna in qualsiasi momento.");
        }
    }

    public static void main(String[] args) {
        RiderCurrentOrderCLIController controller = new RiderCurrentOrderCLIController();
        controller.loadOrderDetails();
    }
}

