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
        Utils.print("\n----- Reseller Main Page -----");
        Utils.print("1. View Pending Orders");
        Utils.print("2. View Order Status");
        Utils.print("3. View Notifications (" + notificationBeanList.size() + ")");
        if (!notificationBeanList.isEmpty()) {
            Utils.print("You have new notifications!");
        }
        Utils.print("4. Exit");
        Utils.print("Choose an option: ");

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
                    // Logic to view notifications -----> BECCA

                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    Utils.print("Invalid option. Please try again.");
                    break;
            }
        } catch (IOException e) {
            Utils.print("An error occurred. Please try again.");
            e.printStackTrace();
        }

        // Display the menu again after an option has been executed
        showMenu();
    }

    public static void main(String[] args) {
        ResellerMainPageCLIController resellerMainPageCLIController = new ResellerMainPageCLIController();
        resellerMainPageCLIController.showMenu();
    }
}
