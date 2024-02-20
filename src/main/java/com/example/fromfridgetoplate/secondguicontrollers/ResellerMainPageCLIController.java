package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;



public class ResellerMainPageCLIController {

    private List<NotificationBean> notificationBeanList;

    public ResellerMainPageCLIController() {
        MakeOrderControl makeOrderControl = new MakeOrderControl();
        notificationBeanList = makeOrderControl.loadNotification();
    }


    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        NavigatorCLI navigator = NavigatorCLI.getInstance();
        System.out.println("\n----- Reseller Main Page -----");
        System.out.println("1. View Pending Orders");
        System.out.println("2. View Order Status");
        System.out.println("3. View Notifications (" + notificationBeanList.size() + ")");
        if (!notificationBeanList.isEmpty()) {
            System.out.println("You have new notifications!");
        }
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");

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
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } catch (IOException e) {
            System.out.println("An error occurred. Please try again.");
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
