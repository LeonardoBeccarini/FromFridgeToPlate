package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.dao.NotificationDAO;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;

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

        DAOFactory daoFactory = new DAOFactory();
        NotificationDAO notificationDAO = daoFactory.getNotificationDAO();
        printList();
        for(NotificationBean notificationBean: notificationBeanList){
            notificationDAO.markNotificationAsRead(notificationBean.getNotificationId());
        }
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
            Printer.print(notificationBean.getMessageText() + ":" + "\n" +
                    "Customer: " + notificationBean.getCustomer() + "\n" +
                    "Address: " + notificationBean.getStreet() + notificationBean.getStreetNumber() +
                    notificationBean.getCity() + notificationBean.getProvince());

        }
    }
}
