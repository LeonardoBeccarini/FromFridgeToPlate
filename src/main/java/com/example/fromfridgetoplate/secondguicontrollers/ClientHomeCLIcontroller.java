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
            Utils.print("----- Client Home Page -----");
            Utils.print("1. Make order");
            Utils.print("2. View Order Status");
            Utils.print("3. Exit");
            Utils.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    makeOrder();
                    break;
                case 2:
                    // viewOrderStatus()
                    break;
                case 3:
                    Utils.print("Exiting Client Home Page.");
                    return;
                default:
                    Utils.print("Invalid option. Please try again.");
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
