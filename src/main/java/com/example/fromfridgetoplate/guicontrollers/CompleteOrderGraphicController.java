package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.guicontrollers.list_cell_factories.CouponListCellFactory;
import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import com.example.fromfridgetoplate.logic.exceptions.CouponNotFoundException;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.exceptions.NegativePriceException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class CompleteOrderGraphicController extends GenericGraphicController{

    @FXML
    private Button payButton;
    @FXML
    private Button applyButton;

    @FXML
    private TextField streetText;
    @FXML
    private TextField numberText;
    @FXML
    private TextField cityText;
    @FXML
    private TextField provinceText;
    @FXML
    private TextField couponNumberText;
    @FXML
    private ListView<CouponBean> couponListView;
    @FXML
    private Label totalPriceLabel;


    private MakeOrderControl makeOrderControl = new MakeOrderControl();
    private ShopBean shopBean;

    public CompleteOrderGraphicController(ShopBean shopBean){
        this.shopBean = shopBean;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        couponListView.setCellFactory(param -> new CouponListCellFactory());
        super.initialize(location, resources);
    }

    @FXML
    void onButtonClicked(ActionEvent event){
        Node sourceNode = (Node) event.getSource();
        if(sourceNode == applyButton){
            CouponBean couponBean = new CouponBean(Integer.parseInt(couponNumberText.getText()), shopBean.getVatNumber());

            try {
                TotalPriceBean totalPriceBean = makeOrderControl.applyCoupon(couponBean);

                couponListView.setItems(FXCollections.observableList(totalPriceBean.getCouponBeanList()));
                totalPriceLabel.setText(String.valueOf(totalPriceBean.getTotalPrice()));

            } catch (CouponNotFoundException | NegativePriceException | DbException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage()) ;
                alert.showAndWait();
            }
        }
        else if(sourceNode == payButton){
            AddressBean addressBean = new AddressBean(streetText.getText(), Integer.parseInt(numberText.getText()), cityText.getText(), provinceText.getText());
            OrderBean orderBean = new OrderBean(shopBean.getVatNumber(), addressBean);
            try {
                makeOrderControl.completeOrder(orderBean);
            } catch (DbException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage()) ;     // non so se devo fare il catch qui o nel controller applicativo
                alert.showAndWait();
            }
        }

    }
}
