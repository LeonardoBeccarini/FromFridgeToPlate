package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.dao.*;
import com.example.fromfridgetoplate.logic.exceptions.*;
import com.example.fromfridgetoplate.logic.model.*;
import com.example.fromfridgetoplate.patterns.abstractFactory.DAOAbsFactory;
import com.example.fromfridgetoplate.patterns.abstractFactory.DAOFactoryProvider;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;
import com.example.fromfridgetoplate.patterns.factory.FileDAOFactory;
import com.example.fromfridgetoplate.patterns.observer.NotificationList;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class RiderHPController {


    //private RiderBean riderBean;
    private Timer notificationPoller;


    CachingNotificationList notificationList;


    private int lastNotificationId = 0;



    /**
     * Costruttore principale che accetta una NotificationBeanList.
     */
    public RiderHPController(NotificationBeanList nlb) {
        //this.riderBean = getRiderDetailsFromSession(); // Accede alle informazioni del rider dalla sessione
        this.notificationList = new CachingNotificationList();

        if (nlb != null) {
            notificationList.attach(nlb);
        }
    }

    /**
     * Costruttore che invece non richiede parametri per la lista delle notifiche.
     * Utilizza 'null' per nlb.
     */
    public RiderHPController() {
        this(null); // Delego al costruttore principale
    }



    public void setRiderAvailable(boolean available) throws DAOException {
        RiderBean riderBean = getRiderDetailsFromSession(); // Accede alle informazioni del rider dalla sessione
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

    public List<NotificationBean> getCurrentNotifications() {
        return convertToNotificationBeans(notificationList.getNotifications());
    }


    private List<NotificationBean> convertToNotificationBeans(List<Notification> notifications) {

        List <NotificationBean> ntfBeanLst = new ArrayList<>();

        for (Notification notification : notifications) {

            Order order = notification.getOrder();
            AddressBean addressBean = new AddressBean(order.getShippingStreet(), order.getShippingStreetNumber(), order.getShippingCity(), order.getShippingProvince());
            OrderBean orderBean = new OrderBean(order.getRiderId(), order.getOrderId(), addressBean);

            NotificationBean ntfBean = new NotificationBean(
                    orderBean,
                    notification.getMessageText()
            );
            ntfBean.setNotificationId(notification.getNotificationId());
            ntfBeanLst.add(ntfBean);

        }

        return ntfBeanLst;
    }


    private class NotificationPollingTask extends TimerTask {
        @Override
        public void run()  {
            pollForNotifications();
        }

        private void pollForNotifications()  {
            NotificationDAO ntfDAO = new NotificationDAO(SingletonConnector.getInstance().getConnection());
            RiderBean riderBean = null; // Accede alle informazioni del rider dalla sessione
            try {
                riderBean = getRiderDetailsFromSession();
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
            List<Notification> newNotifications = ntfDAO.getNotificationsForRider(riderBean.getId(), lastNotificationId);


            if (!newNotifications.isEmpty())
            {
                for (Notification notification : newNotifications)
                {
                    if (notification.getNotificationId() > lastNotificationId)
                    {
                        notificationList.addNotification(notification);

                        lastNotificationId = notification.getNotificationId();
                    }
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



    public void markNotificationAsRead(NotificationBean notificationToMark) throws NotificationHandlingException {
        if (notificationToMark == null) {
            throw new IllegalArgumentException("La notifica da marcare come letta non può essere null.");
        }

        NotificationDAO ntfDAO = new NotificationDAO(SingletonConnector.getInstance().getConnection());

        // Marco la notifica specifica come letta, aggiornando il modello e il database
        ntfDAO.markNotificationAsRead(notificationToMark.getNotificationId());

        // Aggiorno lo stato della notifica nella lista in memoria, se è presente
        notificationList.updateNotificationAsRead(notificationToMark);


    }



    public void acceptOrder(NotificationBean notification) throws DAOException {

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();;
        OrderDAO orderDAO = daoAbsFactory.createOrderDAO();
        //DbOrderDAO orderDAO = new DAOFactory().getOrderDAO();

        OrderBean notifiedOrder = notification.getOrderBean();
        orderDAO.acceptOrder(notifiedOrder.getOrderId(), notifiedOrder.getRiderId());
    }

    public void declineOrder(NotificationBean notification) throws DAOException{
        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        OrderDAO orderDAO = daoAbsFactory.createOrderDAO();
        //DbOrderDAO orderDAO = new DAOFactory().getOrderDAO();

        OrderBean notifiedOrder = notification.getOrderBean();
        orderDAO.declineOrder(notifiedOrder.getOrderId(), notifiedOrder.getRiderId());
    }

    public OrderListBean getConfirmedDeliveries(RiderBean riderInfo) throws RiderGcException, DAOException {

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


    public RiderBean getRiderDetailsFromSession() throws DAOException {

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        RiderDAO riderDAO = daoAbsFactory.createRiderDAO();

        Rider loggedRider = riderDAO.getRiderDetailsFromSession();

        return convertToRiderBean(loggedRider);
    }

    private RiderBean convertToRiderBean(Rider rider) {

        if (rider == null) {
            return null;
        }

        RiderBean riderBean = new RiderBean(
                rider.getId(),
                rider.getName(),
                rider.getSurname(),
                rider.isAvailable(),
                rider.getAssignedCity()
        );
        riderBean.setId(rider.getId());
        return riderBean;
    }

    public boolean checkForOrderInDelivery(RiderBean currentRider) throws DAOException {

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
        } catch (OrderNotFoundException | DAOException e) {
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

    public void confirmDelivery(OrderBean orderBean) throws DAOException {
        if (orderBean != null) {
            LocalDateTime deliveryTime = LocalDateTime.now(); // Orario corrente

            DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
            OrderDAO orderDAO = daoAbsFactory.createOrderDAO();
            orderDAO.updateOrderStatusToDelivered(orderBean.getOrderId(), deliveryTime);
        }
    }



}

