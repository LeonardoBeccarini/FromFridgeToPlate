package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.CartBean;
import com.example.fromfridgetoplate.logic.bean.CartItemBean;
import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;

import java.util.Scanner;

public class CartCLIcontroller {
    private ShopBean selectedShopBean;
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
            System.out.println("1. add");
            System.out.println("2. remove");
            System.out.println("3. complete");

            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice){
                case 1 -> {
                    System.out.println("Type index of the item to add: \n");
                    int selectedIndex = scanner.nextInt()-1;
                    makeOrderControl.changeQuantity(cartBean.getByIndex(selectedIndex), true);
                    showCart();
                }
                case 2 -> {
                    System.out.println("Type index of the item to remove: \n");
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
                default -> System.out.println("Invalid option. Please try again.");
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

            System.out.println(i+" "+name+" "+price+"€" + " "+ quantity+ "\n");
            i++;
        }
        return cartBean;
    }

}
