package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.FoodItemBean;
import com.example.fromfridgetoplate.logic.bean.FoodItemListBean;
import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;

import java.util.Scanner;

public class ProductListCLIcontroller {
    private ShopBean selectedShopBean;
    private final Scanner scanner = new Scanner(System.in);
    private final NavigatorCLI navigator = NavigatorCLI.getInstance();
    private final MakeOrderControl makeOrderControl = new MakeOrderControl(selectedShopBean);
    public ProductListCLIcontroller(ShopBean shopBean){
        this.selectedShopBean = shopBean;
    }

    public void addToCart(){

        while(true){
            System.out.println("1. addToCart");
            System.out.println("2. goToCart");

            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            FoodItemListBean foodItemBeanList;

            foodItemBeanList = makeOrderControl.loadProducts();
            int i = 1;
            for (FoodItemBean foodItemBean : foodItemBeanList.getList()) {
                String name = foodItemBean.getName();
                float price = foodItemBean.getPrice();

                System.out.println(i+" "+name+" "+price+"€" + "\n");
                i++;
            }

            switch (choice) {
                case 1:
                    System.out.println("Type index of the item to add: \n");
                    int selectedIndex = scanner.nextInt();
                    makeOrderControl.addToCart(foodItemBeanList.getList().get(selectedIndex-1));
                    break;
                case 2:
                    CartCLIcontroller cartCLIcontroller = new CartCLIcontroller(selectedShopBean);
                    navigator.goToWithCOntroller("CartCLI", cartCLIcontroller);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

}
