package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.FoodItemBean;
import com.example.fromfridgetoplate.logic.bean.FoodItemListBean;
import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.EmptyCatalogException;

import java.io.IOException;
import java.util.Scanner;

public class ProductListCLIcontroller {
    private final ShopBean selectedShopBean;
    private final Scanner scanner = new Scanner(System.in);
    private final NavigatorCLI navigator = NavigatorCLI.getInstance();
    private  MakeOrderControl makeOrderControl = null;
    public ProductListCLIcontroller(ShopBean shopBean){
        this.selectedShopBean = shopBean;
        try {
            makeOrderControl = new MakeOrderControl(shopBean);
        } catch (DAOException | IOException  e) {
            Printer.print(e.getMessage());
        }catch(EmptyCatalogException e){
            Printer.print(e.getMessage());
            try {
                navigator.goTo("MarketListCLI");
            } catch (IOException ex) {
                Printer.print(e.getMessage());
            }
        }
    }

    public void addToCart(){
        FoodItemListBean foodItemBeanList;

        foodItemBeanList = makeOrderControl.loadProducts();
        int i = 1;
        for (FoodItemBean foodItemBean : foodItemBeanList.getList()) {
            String name = foodItemBean.getName();
            float price = foodItemBean.getPrice();

            Printer.print(i+" "+name+" "+price+"€" + "\n");
            i++;
        }
        boolean running = true;
        while(running){
            Printer.print("1. addToCart");
            Printer.print("2. goToCart");

            Printer.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    Printer.print("Type index of the item to add: \n");
                    int selectedIndex = scanner.nextInt();
                    makeOrderControl.addToCart(foodItemBeanList.getList().get(selectedIndex - 1));

                }
                case 2 -> {
                    CartCLIcontroller cartCLIcontroller = new CartCLIcontroller(selectedShopBean);
                    navigator.goToWithCOntroller("CartCLI", cartCLIcontroller);
                    running = false;
                }
                default -> Printer.print("Invalid option. Please try again.");
            }

        }
    }

}
