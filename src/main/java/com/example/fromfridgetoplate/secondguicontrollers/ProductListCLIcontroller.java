package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.Utils;
import com.example.fromfridgetoplate.logic.bean.FoodItemBean;
import com.example.fromfridgetoplate.logic.bean.FoodItemListBean;
import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;

import java.util.Scanner;

public class ProductListCLIcontroller {
    private final ShopBean selectedShopBean;
    private final Scanner scanner = new Scanner(System.in);
    private final NavigatorCLI navigator = NavigatorCLI.getInstance();
    private final MakeOrderControl makeOrderControl;
    public ProductListCLIcontroller(ShopBean shopBean){
        this.selectedShopBean = shopBean;
        makeOrderControl = new MakeOrderControl(shopBean);
    }

    public void addToCart(){
        FoodItemListBean foodItemBeanList;

        foodItemBeanList = makeOrderControl.loadProducts();
        int i = 1;
        for (FoodItemBean foodItemBean : foodItemBeanList.getList()) {
            String name = foodItemBean.getName();
            float price = foodItemBean.getPrice();

            Utils.print(i+" "+name+" "+price+"€" + "\n");
            i++;
        }
        boolean running = true;
        while(running){
            Utils.print("1. addToCart");
            Utils.print("2. goToCart");

            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    Utils.print("Type index of the item to add: \n");
                    int selectedIndex = scanner.nextInt();
                    makeOrderControl.addToCart(foodItemBeanList.getList().get(selectedIndex - 1));

                }
                case 2 -> {
                    CartCLIcontroller cartCLIcontroller = new CartCLIcontroller(selectedShopBean);
                    navigator.goToWithCOntroller("CartCLI", cartCLIcontroller);
                    running = false;
                }
                default -> Utils.print("Invalid option. Please try again.");
            }

        }
    }

}
