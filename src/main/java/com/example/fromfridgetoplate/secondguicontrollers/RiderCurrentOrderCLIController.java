package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.RiderHPController;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.RiderGcException;

import java.util.Scanner;

public class RiderCurrentOrderCLIController {

    public void loadOrderDetails() {

        RiderHPController riderController = new RiderHPController();
        try {
            RiderBean riderBean = riderController.getRiderDetailsFromSession();
            OrderBean currentOrderBean = riderController.getInDeliveryOrderForRider(riderBean);

            if (currentOrderBean != null) {
                Printer.print("\n*** Dettagli dell'Ordine Corrente ***");
                Printer.print("Id dell'ordine: " + currentOrderBean.getOrderId());
                Printer.print("Via: " + currentOrderBean.getShippingAddress().getShippingStreet());
                Printer.print("Città: " + currentOrderBean.getShippingAddress().getShippingCity());
                Printer.print("Provincia: " + currentOrderBean.getShippingAddress().getShippingProvince());
                Printer.print("Numero civico: " + currentOrderBean.getShippingAddress().getShippingStreetNumber());

                confirmDelivery(riderController, currentOrderBean);
            } else {
                Printer.print("Attualmente non hai ordini in consegna. Per favore, accetta un ordine nella sezione Notifiche.");
            }
        } catch (RiderGcException e) {
            Printer.print("Errore: " + e.getMessage());
            Throwable cause = e.getCause();
            if (cause != null) {
                Printer.print("Causa: " + cause.getMessage());
            }
        }
    }

    private void confirmDelivery(RiderHPController riderController, OrderBean currentOrderBean) {
        Printer.print("\nVuoi confermare la consegna di questo ordine? (sì/no):");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim().toLowerCase();

        if ("sì".equals(input) || "si".equals(input)) {
            try {
                riderController.confirmDelivery(currentOrderBean);
                Printer.print("Complimenti. Hai completato la consegna!");
                Printer.print("Controlla le tue notifiche, per verificare se ci sono nuovi ordini per te!");
            } catch (DAOException e) {
                Printer.print("Errore nella conferma della consegna: " + e.getMessage());
            }
        } else {
            Printer.print("Consegna non confermata. Puoi confermare la consegna in qualsiasi momento.");
        }
    }

    public static void main(String[] args) {
        RiderCurrentOrderCLIController controller = new RiderCurrentOrderCLIController();
        controller.loadOrderDetails();
    }
}

