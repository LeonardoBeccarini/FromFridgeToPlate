package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.AddressBean;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.dao.NotificationDAO;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ResellerNotificationGraphicController extends GenericGraphicController{

    private List<NotificationBean> notificationBeanList;

    @FXML
    private ListView<Label> notificationListView;
    @FXML
    private Label label;
    @FXML
    private Button updateButton;

    public ResellerNotificationGraphicController() {
    }

    public ResellerNotificationGraphicController(List<NotificationBean> notificationBeanList){
        this.notificationBeanList = notificationBeanList;

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DAOFactory daoFactory = new DAOFactory();
        NotificationDAO notificationDAO = daoFactory.getNotificationDAO();
        populateListView();
        for(NotificationBean notificationBean: notificationBeanList){
            notificationDAO.markNotificationAsRead(notificationBean.getNotificationId());
        }
        super.initialize(location, resources);
    }

    private void populateListView() {
        for (NotificationBean notificationBean : notificationBeanList) {
            label = new Label();
            OrderBean orderBean = notificationBean.getOrderBean();
            AddressBean addressBean = orderBean.getShippingAddress();
            label.setText(notificationBean.getMessageText() + ":" + "\n" +
                    "Customer: " + orderBean.getCustomerId() + "\n" +
                    "OrderId: " + orderBean.getOrderId() + "\n" +
                    "Address: " + addressBean.getShippingStreet() +" "+ addressBean.getShippingStreetNumber()+" " + addressBean.getShippingCity()+", " + addressBean.getShippingProvince());
            notificationListView.getItems().add(label);
        }
    }
}
