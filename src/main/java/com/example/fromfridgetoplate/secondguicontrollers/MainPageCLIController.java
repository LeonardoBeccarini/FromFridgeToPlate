package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.Utils;

import java.io.IOException;
import java.util.Scanner;

public class MainPageCLIController {


    private Scanner scanner;

    NavigatorCLI navigator = NavigatorCLI.getInstance();

    public MainPageCLIController() {

        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        Utils.print("1. Login");
        Utils.print("2. Sign In");
        Utils.print("3. Exit");
        System.out.print("Choose an option: ");

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
                    Utils.print("Invalid option. Please try again.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MainPageCLIController mainPageCLIController = new MainPageCLIController();
        mainPageCLIController.displayMenu();
    }


}
