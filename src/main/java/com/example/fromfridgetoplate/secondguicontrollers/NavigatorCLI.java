package com.example.fromfridgetoplate.secondguicontrollers;

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
                RiderHomePageCLIController riderCLI = new RiderHomePageCLIController();
                riderCLI.mainMenu();
                break;
            case "ResellerHomeCLI":
                ResellerMainPageCLIController resellerCLI = new ResellerMainPageCLIController();
                resellerCLI.showMenu();
                break;
            case "ViewPendingOrdersCLI":
                // Assumi che esista una classe ViewPendingOrdersCLI con un metodo displayMenu
                PendingOrdersCLIController viewPendingOrdersCLI = new PendingOrdersCLIController();
                viewPendingOrdersCLI.displayPendingOrders();
                break;
            case "OrderStatusCLI":

              OrderStatusCLIController orderStatusCLICtrl = new OrderStatusCLIController();
              orderStatusCLICtrl.displayAssignedOrders();
                break;
            case "ClientHomeCLI":
                ClientHomeCLIcontroller clientHomeCLIcontroller = new ClientHomeCLIcontroller();
                clientHomeCLIcontroller.displayMenu();
                break;

            case "MarketListCLI":
                MarketListCLIcontroller marketListCLIcontroller = new MarketListCLIcontroller();
                marketListCLIcontroller.searchShop();
                break;

            // Aggiungi altri case qui per le nuove pagine CLI
            default:
                Utils.print("Unrecognized command. Please try again.");
                break;
        }
    }
    public void goToWithCOntroller(String cliPage, Object controller){
        switch(cliPage){
            case "ProductListCLI":
                ProductListCLIcontroller productListCLIcontroller = (ProductListCLIcontroller) controller;
                productListCLIcontroller.addToCart();
                break ;
            case "CartCLI" :
                CartCLIcontroller cartCLIcontroller = (CartCLIcontroller) controller;
                cartCLIcontroller.prova();
                break;
            case "CompleteOrderCLI":
                CompleteOrderCLIcontroller completeOrderCLIcontroller = (CompleteOrderCLIcontroller) controller;
                completeOrderCLIcontroller.showMenu();
                break ;
            case "ResellerNotificationCLI":
                ResellerNotificationCLIcontroller resellerNotificationCLIcontroller = (ResellerNotificationCLIcontroller) controller;
                resellerNotificationCLIcontroller.showNotification();
                break ;
            default:
                Utils.print("Unrecognized command. Please try again.");
                break;
        }
    }

}


