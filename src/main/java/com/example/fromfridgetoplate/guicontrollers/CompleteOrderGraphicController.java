package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.guicontrollers.list_cell_factories.CouponListCellFactory;
import com.example.fromfridgetoplate.logic.bean.CouponBean;
import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import com.example.fromfridgetoplate.logic.exceptions.CouponNotFoundException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CompleteOrderGraphicController extends GenericGraphicController{

    @FXML
    private Button payButton;
    @FXML
    private Button applyButton;
    @FXML
    private Button searchButton;
    @FXML
    private TextField streetText;
    @FXML
    private TextField numberText;
    @FXML
    private TextField provinceText;
    @FXML
    private TextField couponNumberText;
    @FXML
    private ListView<CouponBean> couponListView;



    private MakeOrderControl makeOrderControl = new MakeOrderControl();
    private ShopBean shopBean;
    private CouponBean coupon;

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
        if(sourceNode == searchButton){
            CouponBean couponBean = new CouponBean(Integer.parseInt(couponNumberText.getText()), shopBean.getVatNumber());
            try {
                this.coupon = makeOrderControl.verifyCoupon(couponBean);
                ArrayList<CouponBean> couponList = new ArrayList<>();
                couponList.add(coupon);
                System.out.println(coupon.getCode()); // debug casalingo
                couponListView.setItems(FXCollections.observableList(couponList));
            } catch (CouponNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage()) ;
                alert.showAndWait();
            }
        }
        else if(sourceNode == applyButton){
            //todo
        }
        else if(sourceNode == payButton){
            //todo
        }

    }
}
