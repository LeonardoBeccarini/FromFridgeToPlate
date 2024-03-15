package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import com.example.fromfridgetoplate.logic.exceptions.CouponNotFoundException;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.exceptions.NegativePriceException;
import com.example.fromfridgetoplate.logic.exceptions.PaymentFailedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class CompleteOrderCLIcontroller {
    private final ShopBean shopBean;
    private final MakeOrderControl makeOrderControl = new MakeOrderControl();
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
                case 1 -> applyCoupon();
                case 2 -> {
                    pay();
                    running = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
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
        NavigatorCLI navigatorCLI = NavigatorCLI.getInstance();
        AddressBean addressBean = getAddressBean();
        //verifico se l'utente ha messo i dati
        if(addressBean.getShippingStreet() == null || addressBean.getShippingStreetNumber() == 0||addressBean.getShippingCity() == null ||addressBean.getShippingProvince() == null ){
            System.out.println("Inserire i dati di consegna!!");
            addressBean = getAddressBean();
        }
        OrderBean orderBean = new OrderBean(shopBean.getVatNumber(), addressBean);
        try {
            makeOrderControl.completeOrder(orderBean);
        } catch (DbException | PaymentFailedException e) {
            System.out.println("il completamento dell'ordine non è andato a buon fine: " +e.getMessage());
        }
        try {
            navigatorCLI.goTo("ClientHomeCLI");
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
    private AddressBean getAddressBean(){
        String street = null;
        int streetNumber = 0;
        String city = null;
        String province = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Inserisci la via della consegna: ");
            street = bufferedReader.readLine();
            System.out.println("Inserisci il numero civico: ");
            streetNumber = scanner.nextInt();
            System.out.println("Inserisci la città: ");
            city = bufferedReader.readLine();
            System.out.println("Inserisci la provincia: ");
            province = bufferedReader.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }

        return new AddressBean(street, streetNumber, city ,  province);
    }
}
