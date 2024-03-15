package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.UserBean;
import com.example.fromfridgetoplate.logic.control.LoginController;
import com.example.fromfridgetoplate.logic.model.Role;
import java.io.IOException;
import java.util.Scanner;

public class LoginCLIController {
    private NavigatorCLI navigator;
    private Scanner scanner;

    public LoginCLIController() {
        this.navigator = NavigatorCLI.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public void displayLogin() {
        Utils.print("----- Login -----");
        Utils.print("Enter email: ");
        String email = scanner.nextLine();
        Utils.print("Enter password: ");
        String password = scanner.nextLine();

        if (email.isEmpty() || password.isEmpty()) {
            Utils.print("Both email and password are required.");
            return;
        }

        UserBean userBean = new UserBean(email, password);
        LoginController loginController = new LoginController();
        UserBean loggedUser = loginController.login(userBean);

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
                Utils.print("Login failed. Please check your credentials.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
