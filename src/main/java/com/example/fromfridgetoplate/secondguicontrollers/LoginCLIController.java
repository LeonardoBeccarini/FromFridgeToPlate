package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.UserBean;
import com.example.fromfridgetoplate.logic.control.LoginController;
import com.example.fromfridgetoplate.logic.exceptions.NotExistentUserException;
import com.example.fromfridgetoplate.logic.model.Role;
import java.io.IOException;
import java.util.Scanner;

public class LoginCLIController {
    private final NavigatorCLI navigator;
    private final Scanner scanner;

    public LoginCLIController() {
        this.navigator = NavigatorCLI.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public void displayLogin() {
        Printer.print("----- Login -----");
        Printer.print("Enter email: ");
        String email = scanner.nextLine();
        Printer.print("Enter password: ");
        String password = scanner.nextLine();

        if (email.isEmpty() || password.isEmpty()) {
            Printer.print("Both email and password are required.");
            return;
        }

        UserBean userBean = new UserBean(email, password);
        LoginController loginController = new LoginController();
        UserBean loggedUser = null;
        try {
            loggedUser = loginController.login(userBean);
        } catch (NotExistentUserException e) {
            Printer.print("errore login: "+ e.getMessage());
        }

        try {
            if (loggedUser != null) {
                if (loggedUser.getRole() == Role.CLIENT) {
                    navigator.goTo("ClientHomeCLI");
                } else if (loggedUser.getRole() == Role.RIDER) {
                    navigator.goTo("RiderHomeCLI");
                } else if (loggedUser.getRole() == Role.OWNER) {
                    navigator.goTo("ResellerHomeCLI");
                }
            } else {
                Printer.print("Login failed. Please check your credentials.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
