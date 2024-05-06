package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.guicontrollers.GUIUtils;
import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.dao.NotificationDAO;
import com.example.fromfridgetoplate.logic.dao.OrderDAO;
import com.example.fromfridgetoplate.logic.dao.RiderDAO;
import com.example.fromfridgetoplate.logic.dao.SingletonConnector;
import com.example.fromfridgetoplate.logic.exceptions.*;
import com.example.fromfridgetoplate.logic.model.*;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOAbsFactory;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOFactoryProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class RiderHPController {


    private static final String ERROR_CREATING_DAO = "Errore nella configurazione durante la creazione dell'OrderDAO: ";
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
            nlb.setConcreteSubj(notificationList);
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
    public RiderBean getRiderDetailsFromSession() throws DAOException {

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        RiderDAO riderDAO = null;
        try {
            riderDAO = daoAbsFactory.createRiderDAO();
        } catch (ConfigurationException e) {
            // passo il messaggio e la causa originale dall'eccezione ConfigurationException alla nuova
            // DAOException per non perdere il contesto dell'errore originale

            throw new DAOException(ERROR_CREATING_DAO + e.getMessage(), e);
        }

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

    public void startNotificationPolling() {
        if (notificationPoller != null) {
            notificationPoller.cancel();
        }
        notificationPoller = new Timer();
        notificationPoller.scheduleAtFixedRate(new NotificationPollingTask(), 0, 5000); // Polling ogni 5 secondi
    }
    /*public void scheduleAtFixedRate(     java.util.TimerTask task,
    long delay,
    long period ) --- > quindi scheduleAtFixedRate prende come parametro un'istanza della classe Timertask, dove Timertask  è una classe astratta che  estendo per definire il compito che deve essere fatto dal timer.
    quindi sostituisco ad un istanza del padre un istanza del figlio, perche NotificationPollingTask is a kind of TimerTask*/


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

        List<NotificationBean> ntfBeanLst = new ArrayList<>();

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

    public void markNotificationAsRead(NotificationBean notificationToMark) throws NotificationHandlingException, DAOException {


        NotificationDAO ntfDAO = new NotificationDAO(SingletonConnector.getInstance().getConnection());

        // Marco la notifica specifica come letta, aggiornando il modello e il database
        ntfDAO.markNotificationAsRead(notificationToMark.getNotificationId());

        // Aggiorno lo stato della notifica nella lista in memoria, se è presente
        notificationList.updateNotificationAsRead(notificationToMark);


    }


    public void acceptOrder(NotificationBean notification) throws DAOException {

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        OrderDAO orderDAO = null;
        try {
            orderDAO = daoAbsFactory.createOrderDAO();
        } catch (ConfigurationException e) {
            // passo il messaggio e la causa originale dall'eccezione ConfigurationException alla nuova
            // DAOException per non perdere il contesto dell'errore originale

            throw new DAOException(ERROR_CREATING_DAO + e.getMessage(), e);
        }

        OrderBean notifiedOrder = notification.getOrderBean();
        orderDAO.acceptOrder(notifiedOrder.getOrderId(), notifiedOrder.getRiderId());
    }

    public void declineOrder(NotificationBean notification) throws DAOException {
        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        OrderDAO orderDAO = null;
        try {
            orderDAO = daoAbsFactory.createOrderDAO();
        } catch (ConfigurationException e) {
            // passo il messaggio e la causa originale dall'eccezione ConfigurationException alla nuova
            // DAOException per non perdere il contesto dell'errore originale

            throw new DAOException(ERROR_CREATING_DAO + e.getMessage(), e);
        }

        OrderBean notifiedOrder = notification.getOrderBean();
        orderDAO.declineOrder(notifiedOrder.getOrderId(), notifiedOrder.getRiderId());
    }


    public OrderListBean getConfirmedDeliveries(RiderBean riderInfo) throws RiderGcException, DAOException {
        OrderListBean orderListBean = new OrderListBean();
        try {
            OrderDAO orderDAO = createOrderDAO();
            OrderList confirmedOrders = orderDAO.getConfirmedDeliveriesForRider(riderInfo.getId());
            List<OrderBean> orderBeans = new ArrayList<>();// creiamo una nuova lista vuota per gli OrderBean

            // Ottieniamo la lista degli ordini dall'OrderList
            List<Order> orders = confirmedOrders.getOrders();
            for (Order order : orders) {
                orderBeans.add(convertToOrdBean(order));
            }
            orderListBean.setOrderBeans(orderBeans);
        } catch (DeliveryRetrievalException e) {
            throw new RiderGcException("Impossibile recuperare le consegne confermate.", e);
            // creiamo una nuova eccezione e passiamo la cause dell'eccezione originale per mantenere un tracciamento completo dello stack di chiamate che ha portato all'errore
        }

        return orderListBean;
    }

    private OrderDAO createOrderDAO() throws DAOException {
        try {
            // passo il messaggio e la causa originale dall'eccezione ConfigurationException alla nuova
            // DAOException per non perdere il contesto dell'errore originale
            DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
            return daoAbsFactory.createOrderDAO();
        } catch (ConfigurationException e) {
            throw new DAOException(ERROR_CREATING_DAO + e.getMessage(), e);
        }
    }


    private OrderBean convertToOrdBean(Order order) {

        return new OrderBean(order.getOrderId(), order.getCustomerId(), order.getShopId(), order.getStatus(), order.getOrderTime(), order.getRiderId());


    }


    public boolean checkForOrderInDelivery(RiderBean currentRider) throws DAOException {

        int riderId = currentRider.getId();

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        OrderDAO orderDAO = null;
        try {
            orderDAO = daoAbsFactory.createOrderDAO();
        } catch (ConfigurationException e) {
            // passo il messaggio e la causa originale dall'eccezione ConfigurationException alla nuova
            // DAOException per non perdere il contesto dell'errore originale

            throw new DAOException(ERROR_CREATING_DAO + e.getMessage(), e);
        }
        return orderDAO.checkForOrderInDelivery(riderId);
    }


    public OrderBean getInDeliveryOrderForRider(RiderBean riderInfo) throws RiderGcException, DAOException {
        try {
            OrderDAO orderDAO = createOrderDAO();
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
        OrderBean orderBn = new OrderBean(order.getOrderId(), address);
        orderBn.setStatus(order.getStatus());
        return  orderBn;

    }

    public void confirmDelivery(OrderBean orderBean) throws DAOException {
        if (orderBean != null) {
            LocalDateTime deliveryTime = LocalDateTime.now(); // Orario corrente

            DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
            OrderDAO orderDAO = null;
            try {
                orderDAO = daoAbsFactory.createOrderDAO();
            } catch (ConfigurationException e) {
                // passo il messaggio e la causa originale dall'eccezione ConfigurationException alla nuova
                // DAOException per non perdere il contesto dell'errore originale

                throw new DAOException(ERROR_CREATING_DAO + e.getMessage(), e);
            }
            orderDAO.updateOrderStatusToDelivered(orderBean.getOrderId(), deliveryTime);
        }
    }




    private class NotificationPollingTask extends TimerTask {
        @Override
        public void run() {
            try {
                pollForNotifications();
            } catch (NotificationPollingException e) {
                GUIUtils.showErrorAlert("Errore nel recupero periodico delle notifiche", "", "Dettagli errore: " + e.getMessage());
            }
        }

        private void pollForNotifications() throws NotificationPollingException {
            NotificationDAO ntfDAO = new NotificationDAO(SingletonConnector.getInstance().getConnection());
            RiderBean riderBean;
            try {
                riderBean = getRiderDetailsFromSession();
            } catch (DAOException e) {
                throw new NotificationPollingException("Errore nel recupero dei dettagli del rider, updating periodico delle notifiche disattivato..." + e.getMessage(), e);
            }

            try {
                List<Notification> newNotifications = ntfDAO.getNotificationsForRider(riderBean.getId(), lastNotificationId);
                if (!newNotifications.isEmpty()) {
                    for (Notification notification : newNotifications) {
                        if (notification.getNotificationId() > lastNotificationId) {
                            notificationList.addNotification(notification);
                            lastNotificationId = notification.getNotificationId();
                        }
                    }
                }
            } catch (DAOException e) {
                throw new NotificationPollingException("Errore nel recupero delle notifiche del rider: " + e.getMessage(), e);
            }
        }



    }








}

