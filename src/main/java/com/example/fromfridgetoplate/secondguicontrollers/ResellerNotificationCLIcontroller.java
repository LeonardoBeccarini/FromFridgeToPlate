package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.AddressBean;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ResellerNotificationCLIcontroller {

    private final List<NotificationBean> notificationBeanList;
    public ResellerNotificationCLIcontroller(List<NotificationBean> notificationBeanList){
        this.notificationBeanList = notificationBeanList;
    }
    public void showNotification(){
        Scanner scanner = new Scanner(System.in);
        NavigatorCLI navigatorCLI = NavigatorCLI.getInstance();

        printList();

        MakeOrderControl makeOrderControl = new MakeOrderControl();
        makeOrderControl.markNotificationAsRead(notificationBeanList);

        Printer.print("-------------------------------");
        Printer.print("1. Back");
        int choice = scanner.nextInt();
        try{
            if (choice == 1) {
                navigatorCLI.goTo("ResellerHomeCLI");
            } else {
                Printer.print("Invalid option. Please try again.");
            }
        }catch (IOException e){
            Printer.print(e.getMessage());
        }
    }

    private void printList(){
        for (NotificationBean notificationBean : notificationBeanList) {
            OrderBean orderBean = notificationBean.getOrderBean();
            AddressBean addressBean = orderBean.getShippingAddress();

            Printer.print(notificationBean.getMessageText() + " :" + "\n" +
                    "Customer: " + orderBean.getCustomerId() + "\n" +
                    "Address: " + addressBean.getShippingStreet() + addressBean.getShippingStreetNumber() + " " +
                    addressBean.getShippingCity() + " " + addressBean.getShippingProvince());

        }
    }
}
