package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.guicontrollers.RiderHomePageGraphicController;
import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.dao.NotificationDAO;
import com.example.fromfridgetoplate.logic.dao.OrderDAO;
import com.example.fromfridgetoplate.logic.dao.RiderDAO;
import com.example.fromfridgetoplate.logic.model.Notification;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class RiderHPController {


    private RiderBean riderBean;
    private Timer notificationPoller;
    private RiderHomePageGraphicController rgp; // da eliminare e sistemare

    List<Notification> deliveredNotification;
    private int lastNotificationId = 0;

    private NotificationListBean nlb;


    public RiderHPController(RiderBean riderBean, NotificationListBean nlb) {
        this.riderBean = riderBean;
        this.nlb = nlb;
        this.deliveredNotification = new ArrayList<>();

    }

    // 2o costruttore
    public RiderHPController() {

    }


    public void setRiderAvailable(boolean available) {
        riderBean.setAvailable(available);

        NotificationManager ntfManager = NotificationManager.getInstance();
        ntfManager.registerRiderAvailability(riderBean);
        //startNotificationPolling();

    }

    public void startNotificationPolling() {
        if (notificationPoller != null) {
            notificationPoller.cancel();
        }
        notificationPoller = new Timer();
        notificationPoller.scheduleAtFixedRate(new NotificationPollingTask(), 0, 5000); // Polling ogni 5 secondi
    }


    public void stopNotificationPolling() {
        if (notificationPoller != null) {
            notificationPoller.cancel();
            notificationPoller = null;
        }
    }


    private class NotificationPollingTask extends TimerTask {
        @Override
        public void run() {
            pollForNotifications();
        }
    }


    private void pollForNotifications() {
        DAOFactory daoFactory = new DAOFactory();
        NotificationDAO ntfDAO = daoFactory.getNotificationDAO();
        List<Notification> newNotifications = ntfDAO.getNotificationsForRider(riderBean.getId(), lastNotificationId);

        System.out.println("NewNot size : "+ newNotifications.size() );
        for (Notification notification : newNotifications) {
            System.out.println("Notification ID: " + notification.getNotificationId());
            System.out.println("lastNotification ID: " + lastNotificationId);
        }

        if (!newNotifications.isEmpty()) {
            List<NotificationBean> newNotificationBeans = new ArrayList<>();
            for (Notification notification : newNotifications) {
                if (notification.getNotificationId() > lastNotificationId) {
                        System.out.println("check");

                        System.out.println("Rider ID: " + notification.getRiderId());
                        System.out.println("Order ID: " + notification.getOrderId());
                        System.out.println("Street: " + notification.getStreet());
                        System.out.println("Street Number: " + notification.getStreetNumber());
                        System.out.println("City: " + notification.getCity());
                        System.out.println("Province: " + notification.getProvince());
                        System.out.println("Message: " + notification.getMessageText());
                        System.out.println("------------------------------------");

                    deliveredNotification.add(notification);
                    NotificationBean notificationBean = convertToNotificationBean(notification);
                    newNotificationBeans.add(notificationBean);
                    lastNotificationId = notification.getNotificationId();
                }
            }
            if (!newNotificationBeans.isEmpty()) {
                nlb.addNotifications(newNotificationBeans); // Aggiorna la lista nella NotificationListBean
                System.out.println("Rilevate nuove notifiche, lastNotification id: "+ lastNotificationId);
            }
        } else {
            System.out.println("Non rilevate nuove notifiche, lastNotification id: "+ lastNotificationId);
        }
    }


    private NotificationBean convertToNotificationBean(Notification notification) {
        NotificationBean ntfBean = new NotificationBean(
                notification.getRiderId(),
                notification.getOrderId(),
                notification.getStreet(),
                notification.getStreetNumber(),
                notification.getCity(),
                notification.getProvince(),
                notification.getMessageText()
        );
        ntfBean.setNotificationId(notification.getNotificationId());
        return ntfBean;
    }



    public void markNotificationsAsRead() {

        //this.stopNotificationPolling(); // interrompo momentaneamente il polling del db

        DAOFactory daoFactory = new DAOFactory();
        NotificationDAO ntfDAO = daoFactory.getNotificationDAO();
        for (NotificationBean notificationBn : nlb.getNotifications()) {
            ntfDAO.markNotificationAsRead(notificationBn.getNotificationId()); // marca le notifiche come lette , aggiornando il model e il db
            // Aggiorna la lista deliveredNotification
            for (Notification deliveredNotif : deliveredNotification) {
                if (deliveredNotif.getNotificationId() == notificationBn.getNotificationId()) {
                    deliveredNotif.markAsRead(); // Aggiorna lo stato nella lista in memoria
                    break;
                }
            }
        }
        nlb.clearNotifications();// pulisco la lista(buffer delle notifiche), in modo da rappresentare che l'utente rider abbia visualizzato tutte le
        // notifiche consegnate, questo metodo in effetti viene chiamato dal controller grafico quando l'utente rider
        // clicca per visualizzare e quindi leggere le notifihce
        //this.startNotificationPolling(); // ristarto
    }

    public void markNotificationAsRead(NotificationBean notificationToMark) {
        if (notificationToMark == null) {
            return;
        }
        // Interrompo momentaneamente il polling del db
        // this.stopNotificationPolling();

        DAOFactory daoFactory = new DAOFactory();
        NotificationDAO ntfDAO = daoFactory.getNotificationDAO();

        // Marca la notifica specifica come letta, aggiornando il modello e il database
        ntfDAO.markNotificationAsRead(notificationToMark.getNotificationId());

        // Aggiorna lo stato della notifica nella lista in memoria, se presente
        for (Notification deliveredNotif : deliveredNotification) {
            if (deliveredNotif.getNotificationId() == notificationToMark.getNotificationId()) {
                deliveredNotif.markAsRead();
                break; // Interrompo il ciclo una volta trovata la notifica corrispondente
            }
        }

        // Rimuovo la notifica marcata come letta dalla lista delle notifiche (NotificationListBean)
        nlb.removeNotification(notificationToMark);

        // Riavvia il polling delle notifiche, se necessario
        // this.startNotificationPolling();
    }




    public void acceptOrder(NotificationBean notification) {


        RiderDAO riderDAO = new DAOFactory().getRiderDAO();
        riderDAO.acceptOrder(notification.getOrderId(), notification.getRiderId());
    }

    public void declineOrder(NotificationBean notification) {

        RiderDAO riderDAO = new DAOFactory().getRiderDAO();
        riderDAO.declineOrder(notification.getOrderId(), notification.getRiderId());
    }

    public OrderListBean getConfirmedDeliveries(RiderBean riderInfo) {
        RiderDAO riderDAO = new DAOFactory().getRiderDAO();
        OrderList confirmedOrders = riderDAO.getConfirmedDeliveriesForRider(riderInfo.getId());

        OrderListBean orderListBean = new OrderListBean();

        // creiamo una nuova lista vuota per gli OrderBean
        List<OrderBean> orderBeans = new ArrayList<>();

        // Ottieniamo la lista degli ordini dall'OrderList
        List<Order> orders = confirmedOrders.getOrders();

        for (Order order : orders) {
            OrderBean orderBean = convert_toOrderBean(order);
            orderBeans.add(orderBean);
        }

        // setta la lista di OrderBean nel OrderListBean
        orderListBean.setOrderBeans(orderBeans);


        return orderListBean;
    }

    private OrderBean convert_toOrderBean(Order order) {

        OrderBean orderBean = new OrderBean(order.getOrderId(), order.getCustomerId(), order.getShopId(), order.getStatus(), order.getOrderTime(), order.getRiderId() );

        return orderBean;
    }


    public RiderBean getRiderDetailsFromSession() {

        DAOFactory daoFactory = new DAOFactory();
        RiderDAO riderDAO = daoFactory.getRiderDAO();
        return riderDAO.getRiderDetailsFromSession();
    }

    public boolean checkForOrderInDelivery(RiderBean currentRider) {

        int riderId = currentRider.getId();

        RiderDAO riderDAO = new DAOFactory().getRiderDAO();
        return riderDAO.checkForOrderInDelivery(riderId);
    }

    public OrderBean getInDeliveryOrderForRider(RiderBean riderInfo) {
        // retrieviamo l'ordine 'in consegna' per il rider
        RiderDAO riderDAO = new DAOFactory().getRiderDAO();
        Order order = riderDAO.getInDeliveryOrderForRider(riderInfo.getId());

        if (order != null) {
            return convertToOrderBean(order);
        }
        // Se non ci sono ordini 'in consegna', ritorniamo null
        return null;
    }

    private OrderBean convertToOrderBean(Order order) {
        AddressBean address = new AddressBean(
                order.getShippingStreet(),
                order.getShippingStreetNumber(),
                order.getShippingCity(),
                order.getShippingProvince()
        );

        return new OrderBean(
                order.getOrderId(),
                address
        );
    }

    public void confirmDelivery(OrderBean orderBean) {
        if (orderBean != null) {
            LocalDateTime deliveryTime = LocalDateTime.now(); // Orario corrente
            RiderDAO riderDAO = new DAOFactory().getRiderDAO();
            riderDAO.updateOrderStatusToDelivered(orderBean.getOrderId(), deliveryTime);
        }
    }






}

