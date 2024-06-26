package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.CartBean;
import com.example.fromfridgetoplate.logic.bean.CartItemBean;
import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;

import java.util.Scanner;

public class CartCLIcontroller {
    private final ShopBean selectedShopBean;
    Scanner scanner = new Scanner(System.in);
    NavigatorCLI navigatorCLI = NavigatorCLI.getInstance();
    private final MakeOrderControl makeOrderControl = new MakeOrderControl();
    public CartCLIcontroller(ShopBean shopBean){
        this.selectedShopBean = shopBean;
    }

    public void prova(){
       CartBean cartBean = showCart();
        boolean running = true;
        while(running){
            Printer.print("1. add");
            Printer.print("2. remove");
            Printer.print("3. complete");

            Printer.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice){
                case 1 -> {
                    Printer.print("Type index of the item to add: \n");
                    int selectedIndex = scanner.nextInt()-1;
                    makeOrderControl.changeQuantity(cartBean.getByIndex(selectedIndex), true);
                    showCart();
                }
                case 2 -> {
                    Printer.print("Type index of the item to remove: \n");
                    int selectedIndex = scanner.nextInt()-1;
                    makeOrderControl.changeQuantity(cartBean.getByIndex(selectedIndex), false);
                    showCart();
                }
                case 3 -> {
                    CompleteOrderCLIcontroller completeOrderCLIcontroller= new CompleteOrderCLIcontroller(selectedShopBean);
                    navigatorCLI.goToWithCOntroller(
                            "CompleteOrderCLI", completeOrderCLIcontroller
                    );
                    running = false;        //perchè qui break non funziona?
                }
                default -> Printer.print("Invalid option. Please try again.");
            }
        }


    }
    private CartBean showCart(){
        CartBean cartBean = makeOrderControl.loadCart();
        int i = 1;
        for (CartItemBean cartItemBean : cartBean.getItemBeanList()) {
            String name = cartItemBean.getName();
            float price = cartItemBean.getPrice();
            double quantity = cartItemBean.getQuantity();

            Printer.print(i+" "+name+" "+price+"€" + " "+ quantity+ "\n");
            i++;
        }
        return cartBean;
    }

}
