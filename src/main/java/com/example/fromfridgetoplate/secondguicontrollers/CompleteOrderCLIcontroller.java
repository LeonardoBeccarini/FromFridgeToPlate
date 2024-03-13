package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.CouponBean;
import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.bean.TotalPriceBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import com.example.fromfridgetoplate.logic.exceptions.CouponNotFoundException;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.exceptions.NegativePriceException;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.Scanner;

public class CompleteOrderCLIcontroller {
    private ShopBean shopBean;
    private MakeOrderControl makeOrderControl = new MakeOrderControl();
    public CompleteOrderCLIcontroller(ShopBean shopBean){
        this.shopBean = shopBean;
    }
    Scanner scanner = new Scanner(System.in);
    public void showMenu(){
        boolean running = true;
        while(running){
            System.out.println("1. Apply coupon");
            System.out.println("2. Pay");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    applyCoupon();
                    break;
                case 2:
                    pay();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private void applyCoupon() {
        System.out.println("Inserisci il codice del coupon: ");
        int code = scanner.nextInt();
        CouponBean couponBean = new CouponBean(code, shopBean.getVatNumber());

        try {
            TotalPriceBean totalPriceBean = makeOrderControl.applyCoupon(couponBean);

            System.out.println("Il nuovo prezzo dell'ordine è : " + totalPriceBean.getTotalPrice());

        } catch (CouponNotFoundException | NegativePriceException | DbException e) {
            System.out.println(e.getMessage());

        }
    }

    private void pay() {
        System.out.println("da fare");
    }
}
