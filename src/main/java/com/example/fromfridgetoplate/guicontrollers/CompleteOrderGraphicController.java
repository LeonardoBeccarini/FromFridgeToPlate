package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.guicontrollers.list_cell_factories.CouponListCellFactory;
import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import com.example.fromfridgetoplate.logic.exceptions.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
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


    MakeOrderControl makeOrderControl = new MakeOrderControl(); //questo dovrebbe essere un attributo o na local variable della classe?
    private final ShopBean shopBean;

    public CompleteOrderGraphicController(ShopBean shopBean){
        this.shopBean = shopBean;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        couponListView.setCellFactory(param -> new CouponListCellFactory());
        TotalPriceBean totalPriceBean = makeOrderControl.getOriginalPrice();
        totalPriceLabel.setText(String.valueOf(totalPriceBean.getTotalPrice()));
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

            } catch (CouponNotFoundException | NegativePriceException | DAOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage()) ;
                alert.showAndWait();
            }
        }
        else if(sourceNode == payButton){

            if(streetText.getText().isEmpty()||numberText.getText().isEmpty()||cityText.getText().isEmpty()||provinceText.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING, "complete all fields before||");
                alert.showAndWait();
            }
            AddressBean addressBean = new AddressBean(streetText.getText(), Integer.parseInt(numberText.getText()), cityText.getText(), provinceText.getText());
            OrderBean orderBean = new OrderBean(shopBean.getVatNumber(), addressBean);

            saveOrder(orderBean);

            try {
                navigator.goTo("clientHomePage.fxml");
            } catch (IOException e) {
                Alert alert2 = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert2.showAndWait();


            }
        }
    }
    private void saveOrder(OrderBean orderBean) {
        try {
            makeOrderControl.completeOrder(orderBean);
        } catch ( PaymentFailedException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage()) ;
            alert.showAndWait();
        }catch(DAOException e) {
            GUIUtils.showErrorAlert("Errore di scrittura/lettura", "Errore nel salvataggio dell'ordine", "" + e.getMessage());
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ordine salvato con successo");
        alert.showAndWait();
    }
}
