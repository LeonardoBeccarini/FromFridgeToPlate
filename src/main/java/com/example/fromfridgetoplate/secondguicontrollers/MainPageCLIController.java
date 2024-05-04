package com.example.fromfridgetoplate.secondguicontrollers;

import java.io.IOException;
import java.util.Scanner;

public class MainPageCLIController {


    private Scanner scanner;

    NavigatorCLI navigator = NavigatorCLI.getInstance();

    public MainPageCLIController() {

        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        Printer.print("1. Login");
        Printer.print("2. Sign In");
        Printer.print("3. Exit");
        Printer.print("Choose an option: ");

        int choice = scanner.nextInt();
        try {
            switch (choice) {
                case 1:
                    navigator.goTo("LoginCLI");
                    break;
                case 2:
                    navigator.goTo("SignInCLI");
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    Printer.print("Invalid option. Please try again.");
            }
        } catch (IOException e) {
            Printer.print("errore di I/O: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        MainPageCLIController mainPageCLIController = new MainPageCLIController();
        mainPageCLIController.displayMenu();
    }


}
