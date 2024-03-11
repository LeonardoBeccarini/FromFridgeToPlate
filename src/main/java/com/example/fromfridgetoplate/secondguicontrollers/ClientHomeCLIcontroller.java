package com.example.fromfridgetoplate.secondguicontrollers;

import java.io.IOException;
import java.util.Scanner;

public class ClientHomeCLIcontroller {
    Scanner scanner;
    NavigatorCLI navigatorCLI = NavigatorCLI.getInstance();

    public ClientHomeCLIcontroller() {
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        while (true) {
            System.out.println("----- Client Home Page -----");
            System.out.println("1. Make order");
            System.out.println("2. View Order Status");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    makeOrder();
                    break;
                case 2:
                    // viewOrderStatus()
                    break;
                case 3:
                    System.out.println("Exiting Client Home Page.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

    }
    public void makeOrder(){
        try {
            navigatorCLI.goTo("MarketListCLI");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
