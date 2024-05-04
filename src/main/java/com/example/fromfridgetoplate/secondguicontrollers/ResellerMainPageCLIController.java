package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;



public class ResellerMainPageCLIController {

    private List<NotificationBean> notificationBeanList;

    public ResellerMainPageCLIController() {
        MakeOrderControl makeOrderControl = new MakeOrderControl();
        try {
            notificationBeanList = makeOrderControl.loadNotification();
        } catch (DbException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }


    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        NavigatorCLI navigator = NavigatorCLI.getInstance();
        Printer.print("\n----- Reseller Main Page -----");
        Printer.print("1. View Pending Orders");
        Printer.print("2. View Order Status");
        Printer.print("3. View Notifications (" + notificationBeanList.size() + ")");
        if (!notificationBeanList.isEmpty()) {
            Printer.print("You have new notifications!");
        }
        Printer.print("4. Exit");
        Printer.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        try {
            switch (choice) {
                case 1:
                    // Logic to view pending orders
                    navigator.goTo("ViewPendingOrdersCLI");
                    break;
                case 2:
                    // Logic to view order status
                    navigator.goTo("OrderStatusCLI");
                    break;
                case 3:
                    navigator.goToWithCOntroller("ResellerNotificationCLI", new ResellerNotificationCLIcontroller(notificationBeanList));

                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    Printer.print("Invalid option. Please try again.");
                    break;
            }
        } catch (IOException e) {
            Printer.print("An error occurred. Please try again.");
        }

        // Display the menu again after an option has been executed
        showMenu();
    }


}
