package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.dao.*;
import com.example.fromfridgetoplate.logic.exceptions.*;
import com.example.fromfridgetoplate.logic.model.Notification;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;
import com.example.fromfridgetoplate.patterns.abstractFactory.DAOAbsFactory;
import com.example.fromfridgetoplate.patterns.abstractFactory.DAOFactoryProvider;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;
import com.example.fromfridgetoplate.patterns.factory.FileDAOFactory;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class RiderHPController {


    private RiderBean riderBean;
    private Timer notificationPoller;


    List<Notification> deliveredNotification;
    private int lastNotificationId = 0;

    private NotificationListBean nlb;


    public RiderHPController(NotificationListBean nlb) {

        this.nlb = nlb;
        this.deliveredNotification = new ArrayList<>();
        this.riderBean = getRiderDetailsFromSession();// serve per accedere alle informazioni immesse al momento della
        // registrazione, che mi servono, per popolare il riderbean

    }

    // 2o costruttore
    public RiderHPController() {

        this.deliveredNotification = new ArrayList<>();
        this.riderBean = getRiderDetailsFromSession();// serve per accedere alle informazioni immesse al momento della
        // registrazione, che mi servono, per popolare il riderbean


    }


    public void setRiderAvailable(boolean available) {
        riderBean.setAvailable(available);

        NotificationManager ntfManager = new NotificationManager();
        ntfManager.registerRiderAvailability(riderBean);


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

        private void pollForNotifications() {
            NotificationDAO ntfDAO = new NotificationDAO(SingletonConnector.getInstance().getConnection());
            List<Notification> newNotifications = ntfDAO.getNotificationsForRider(riderBean.getId(), lastNotificationId);


            if (!newNotifications.isEmpty()) {
                List<NotificationBean> newNotificationBeans = new ArrayList<>();
                for (Notification notification : newNotifications) {
                    if (notification.getNotificationId() > lastNotificationId) {
                        deliveredNotification.add(notification);
                        NotificationBean notificationBean = convertToNotificationBean(notification);
                        newNotificationBeans.add(notificationBean);
                        lastNotificationId = notification.getNotificationId();
                    }
                }
                if (!newNotificationBeans.isEmpty()) {
                    nlb.addNotifications(newNotificationBeans); // Aggiorna la lista nella NotificationListBean
                }
            }
        }


        private NotificationBean convertToNotificationBean(Notification notification) {


            Order order = notification.getOrder();
            AddressBean addressBean = new AddressBean(order.getShippingStreet(), order.getShippingStreetNumber(), order.getShippingCity(), order.getShippingProvince());
            OrderBean orderBean = new OrderBean(order.getRiderId(), order.getOrderId(), addressBean);

            NotificationBean ntfBean = new NotificationBean(
                    orderBean,
                    notification.getMessageText()
            );
            ntfBean.setNotificationId(notification.getNotificationId());
            return ntfBean;
        }


    }


    public void markNotificationsAsRead() {

        //this.stopNotificationPolling(); // interrompo momentaneamente il polling del db


        NotificationDAO ntfDAO = new NotificationDAO(SingletonConnector.getInstance().getConnection());
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

    }

    public void markNotificationAsRead(NotificationBean notificationToMark) throws NotificationProcessingException {
        if (notificationToMark == null) {
            throw new IllegalArgumentException("La notifica da marcare come letta non può essere null.");
        }

        NotificationDAO ntfDAO = new NotificationDAO(SingletonConnector.getInstance().getConnection());

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
        try {
            nlb.removeNotification(notificationToMark);
        } catch (NotificationHandlingException e) {
            // wrappo l'eccezione in una nuova eccezione con ulteriore contesto
            throw new NotificationProcessingException("Errore nella gestione della notifica con ID: " + notificationToMark.getNotificationId(), e);
        }

    }




    public void acceptOrder(NotificationBean notification) {

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();;
        OrderDAO orderDAO = daoAbsFactory.createOrderDAO();
        //DbOrderDAO orderDAO = new DAOFactory().getOrderDAO();

        OrderBean notifiedOrder = notification.getOrderBean();
        orderDAO.acceptOrder(notifiedOrder.getOrderId(), notifiedOrder.getRiderId());
    }

    public void declineOrder(NotificationBean notification) throws IOException {
        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        OrderDAO orderDAO = daoAbsFactory.createOrderDAO();
        //DbOrderDAO orderDAO = new DAOFactory().getOrderDAO();

        OrderBean notifiedOrder = notification.getOrderBean();
        orderDAO.declineOrder(notifiedOrder.getOrderId(), notifiedOrder.getRiderId());
    }

    public OrderListBean getConfirmedDeliveries(RiderBean riderInfo) throws RiderGcException {

        OrderListBean orderListBean = new OrderListBean();
        try {

            DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
            OrderDAO orderDAO = daoAbsFactory.createOrderDAO();
            OrderList confirmedOrders = orderDAO.getConfirmedDeliveriesForRider(riderInfo.getId());


            // creiamo una nuova lista vuota per gli OrderBean
            List<OrderBean> orderBeans = new ArrayList<>();

            // Ottieniamo la lista degli ordini dall'OrderList
            List<Order> orders = confirmedOrders.getOrders();
            for (Order order : orders) {
                OrderBean orderBean = convertToOrdBean(order);
                orderBeans.add(orderBean);
            }


            orderListBean.setOrderBeans(orderBeans);
        }catch (DeliveryRetrievalException e) {
            throw new RiderGcException("Impossibile recuperare le consegne confermate.", e);
        }
        // creiamo una nuova eccezione e passiamo la cause dell'eccezione originale per mantenere un tracciamento completo dello stack di chiamate che ha portato all'errore

        return orderListBean;
    }

    private OrderBean convertToOrdBean(Order order) {

        return new OrderBean(order.getOrderId(), order.getCustomerId(), order.getShopId(), order.getStatus(), order.getOrderTime(), order.getRiderId() );


    }


    public RiderBean getRiderDetailsFromSession() {

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        RiderDAO riderDAO = daoAbsFactory.createRiderDAO();

        return riderDAO.getRiderDetailsFromSession();
    }

    public boolean checkForOrderInDelivery(RiderBean currentRider) {

        int riderId = currentRider.getId();

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        OrderDAO orderDAO = daoAbsFactory.createOrderDAO();
        return orderDAO.checkForOrderInDelivery(riderId);
    }


    public OrderBean getInDeliveryOrderForRider(RiderBean riderInfo) throws RiderGcException {
        try {

            DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
            OrderDAO orderDAO = daoAbsFactory.createOrderDAO();
            Order order = orderDAO.getInDeliveryOrderForRider(riderInfo.getId());
            return convertToOrderBean(order);
        } catch (OrderNotFoundException e) {
            throw new RiderGcException("Errore durante il recupero dell'ordine 'in consegna' per il rider con ID: " + riderInfo.getId(), e);
            // cosi questo errore potrebbe esser sia relativo al fatto che non c'è nessun ordine in consegna da parte
            // del rider, sia che c'è stato un errore da parte del db, l'nfo sul tipo di errore è contenuta in e
        }
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

            DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
            OrderDAO orderDAO = daoAbsFactory.createOrderDAO();
            orderDAO.updateOrderStatusToDelivered(orderBean.getOrderId(), deliveryTime);
        }
    }


    public void setNlb (NotificationListBean nlb){
        this.nlb = nlb;
    }

    public RiderBean getRiderBean() {
        return riderBean;
    }
}

