package com.example.fromfridgetoplate.secondGuicontrollers;

import java.io.IOException;

public class NavigatorCLI {
    private static NavigatorCLI navigator = null;

    private NavigatorCLI() {}

    public static NavigatorCLI getInstance(){
        if (navigator == null) {
            navigator = new NavigatorCLI();
        }
        return navigator;
    }

    public void goTo(String cliPage) throws IOException {
        switch (cliPage) {
            case "MainCLI":
                MainPageCLIController mainPageCLIController = new MainPageCLIController();
                mainPageCLIController.displayMenu();
                break;
            case "LoginCLI":
                LoginCLIController loginCLIController = new LoginCLIController();
                loginCLIController.displayLogin();
                break;

            case "RiderHomeCLI":
                RiderHomeCLI riderCLI = new RiderHomeCLI();
                riderCLI.displayMenu();
            default:
                System.out.println("Unrecognized command. Please try again.");
                break;
        }
    }


}
