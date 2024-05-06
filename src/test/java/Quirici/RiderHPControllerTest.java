package Quirici;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.example.fromfridgetoplate.logic.bean.AddressBean;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import com.example.fromfridgetoplate.logic.control.NotificationManager;
import com.example.fromfridgetoplate.logic.control.RiderHPController;
import com.example.fromfridgetoplate.logic.dao.*;
import com.example.fromfridgetoplate.logic.exceptions.*;
import com.example.fromfridgetoplate.logic.model.CartItem;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOFactoryProvider;
import com.example.fromfridgetoplate.logic.model.OrderList;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class RiderHPControllerTest {


    @Test
    public void testAcceptOrder() throws DAOException, ConfigurationException, OrderAssignmentException {
        RiderHPController controller = new RiderHPController();
        OrderDAO orderDAO = DAOFactoryProvider.getInstance().getDaoFactory().createOrderDAO();
        DAOFactoryProvider.getInstance().setPersistenceType(PersistenceType.JDBC); // Set per usare db

        // Preparazione dei dati dell'ordine
        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem("Pasta", 2));

        Order testOrder = new Order(
                "reseller123",
                "customer@example.com",
                "Via Roma",
                123,
                "Roma",
                "RM",
                "pronto"  // Stato iniziale dell'ordine
        );
        testOrder.setItems(cartItems);

        boolean operationSuccess = true;

        // Salvataggio dell'ordine nel database
        try {
            testOrder = orderDAO.saveOrder(testOrder);
        } catch (DAOException e) {
             operationSuccess = false;
        }

        // Assegnazione del rider all'ordine
        int riderId = 5; // ID del rider per il test
        testOrder.setRiderId(riderId);  // Aggiorna l'ID del rider nell'ordine
        ResellerDAO resellerDAO = DAOFactoryProvider.getInstance().getDaoFactory().createResellerDAO();
        resellerDAO.assignRiderToOrder(testOrder.getOrderId(), riderId);


        // Conversione dell'Order salvato in OrderBean
        OrderBean testOrderBean = convertToOrderBean(testOrder);
        NotificationBean testNotification = new NotificationBean(testOrderBean, "Order needs to be accepted");

        resellerDAO.setAssignation(testOrder.getOrderId());


        try {
            controller.acceptOrder(testNotification);
        } catch (DAOException ex) {
            operationSuccess = false;
        }

        // Verifica dello stato dell'ordine
        if (operationSuccess) {
            try {
                RiderBean riderInfo = new RiderBean("NomeRide1", "cognomerider1", true, "Roma");
                riderInfo.setId(testOrder.getRiderId());  // ID corrispondente del rider
                OrderBean orderInDelivery = controller.getInDeliveryOrderForRider(riderInfo);
                if (!"in consegna".equals(orderInDelivery.getStatus())) {
                    operationSuccess = false;
                }
            } catch (RiderGcException | DAOException e) {
                operationSuccess = false;
            }
        }


        assertTrue("Order acceptance should succeed and order should be 'in consegna'", operationSuccess);
    }

    private OrderBean convertToOrderBean(Order order) {
        AddressBean address = new AddressBean(order.getShippingStreet(), order.getShippingStreetNumber(), order.getShippingCity(), order.getShippingProvince());
        OrderBean orderBean = new OrderBean(
                order.getCustomerId(), order.getOrderId(), address
        );
        orderBean.setStatus(order.getStatus());
        orderBean.setRiderId(order.getRiderId());
        return orderBean;
    }

    @Test
    public void testDeclineOrder() throws DAOException, ConfigurationException, OrderAssignmentException {
        RiderHPController controller = new RiderHPController();
        OrderDAO orderDAO = DAOFactoryProvider.getInstance().getDaoFactory().createOrderDAO();
        ResellerDAO resellerDAO =  DAOFactoryProvider.getInstance().getDaoFactory().createResellerDAO();
        DAOFactoryProvider.getInstance().setPersistenceType(PersistenceType.JDBC);

        // Creo e salvo l'ordine
        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem("Pasta", 2));
        Order testOrder = new Order(
                "reseller123", // sarebbe vat
                "customer@example.com",
                "Via Roma", 123, "Roma", "RM",
                "in consegna"
        );
        testOrder.setItems(cartItems);

        boolean flag = true;

        try {
            testOrder = orderDAO.saveOrder(testOrder);
        } catch (DAOException e) {
            flag = false;
        }



        // Assegnazione del rider all'ordine
        int riderId = 5; // ID del rider per il test
        testOrder.setRiderId(riderId);  // Aggiorno l'ID del rider nell'ordine
        resellerDAO.assignRiderToOrder(testOrder.getOrderId(), riderId);


        // Simulo l'invio della notifica necessaria per il metodo declineOrder
        OrderBean testOrderBean = convertToOrderBean(testOrder);
        NotificationBean testNotification = new NotificationBean(testOrderBean, "Order needs to be declined");

        resellerDAO.setAssignation(testOrder.getOrderId());



        // Tentativo di rifiutare l'ordine
        try {
            controller.declineOrder(testNotification);
        } catch (DAOException ex) {
            flag = false;
        }

        // Verifico che l'ordine rifiutato sia ora in stato "pronto", perchè l'assegnazione lo pone a "in consegna", se il declineOrder(), va a buon fine lo stato dell'ordine torna ad essere "pronto"
        try {
            OrderList pendingOrders = resellerDAO.getPendingOrders("quircioreseller@gmail.com"); // questa email deve essere quella corrispondente allo shopid passato sopra nella tabella ordini: ""reseller123"

            System.out.println("orderid:" + testOrder.getOrderId());
            for (Order order : pendingOrders.getOrders()) {
                System.out.println("pending orders id :" + order.getOrderId());
                if (order.getOrderId() == testOrder.getOrderId() && "pronto".equals(order.getStatus())) {
                    flag = true;
                    break;
                }
            }

        } catch (DAOException e) {
            flag = false;
        }


        assertTrue("Order should be found in 'pronto' status", flag);
    }





}


