package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.AddressBean;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
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


    @FXML
    private ListView<Label> notificationListView;
    @FXML
    private Label label;
    @FXML
    private Button updateButton;
    private List<NotificationBean> notificationBeanList;

    public ResellerNotificationGraphicController() {
    }

    public ResellerNotificationGraphicController(List<NotificationBean> notificationBeanList){
        this.notificationBeanList = notificationBeanList;

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateListView();
        MakeOrderControl makeOrderControl = new MakeOrderControl();
        makeOrderControl.markNotificationAsRead(notificationBeanList);
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
