package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.UserBean;
import com.example.fromfridgetoplate.logic.control.LoginController;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
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
        try {
            UserBean loggedUser = loginController.login(userBean);

            // Controllo post-login per determinare la navigazione in base al ruolo
            navigateByRole(loggedUser);
        } catch (NotExistentUserException e) {
            Printer.print("Errore: l'utente non esiste " + e.getMessage());
        } catch (DAOException e) {
            Printer.print("Errore di accesso al database: " + e.getMessage());
        } catch (IOException e) {
            Printer.print("Errore di sistema: Impossibile navigare alla pagina desiderata.");
        }
    }

    private void navigateByRole(UserBean loggedUser) throws IOException {
        if (loggedUser.getRole() == Role.CLIENT) {
            navigator.goTo("ClientHomeCLI");
        } else if (loggedUser.getRole() == Role.RIDER) {
            navigator.goTo("RiderHomeCLI");
        } else if (loggedUser.getRole() == Role.OWNER) {
            navigator.goTo("ResellerHomeCLI");
        }
    }



}
