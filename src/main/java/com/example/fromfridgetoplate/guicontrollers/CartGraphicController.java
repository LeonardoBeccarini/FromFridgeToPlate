package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.guicontrollers.list_cell_factories.CartItemListCellFactory;
import com.example.fromfridgetoplate.logic.bean.CartBean;
import com.example.fromfridgetoplate.logic.bean.CartItemBean;
import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CartGraphicController extends GenericGraphicController{

    @FXML
    private Button plusButton;

    @FXML
    private Button minusButton;

    @FXML
    private Button completeButton;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;


    @FXML
    private ListView<CartItemBean> cartListView;
    private final MakeOrderControl makeOrderControl;
    private final ShopBean shopBean;

    public CartGraphicController(ShopBean shopBean){
        makeOrderControl = new MakeOrderControl();
        this.shopBean = shopBean;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cartListView.setCellFactory(param -> new CartItemListCellFactory());
        updateCart();
        cartListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            nameLabel.setText((newValue == null) ? "" : newValue.getName());
            priceLabel.setText((newValue == null) ? "" :  "€" + newValue.getPrice() );
        });
        super.initialize(location, resources);
    }

    public void updateCart(){
        CartBean cartBean = makeOrderControl.loadCart();
        cartListView.setItems(FXCollections.observableList(cartBean.getItemBeanList()));
    }
    @FXML
    void onButtonClicked(ActionEvent event) throws IOException {
        Node sourceNode = (Node) event.getSource();
        CartItemBean selectedCartItemBean = cartListView.getSelectionModel().getSelectedItem();
        if(sourceNode == plusButton){
            makeOrderControl.changeQuantity(selectedCartItemBean, true);
            updateCart();
        }
        if(sourceNode == minusButton){
            makeOrderControl.changeQuantity(selectedCartItemBean, false);
            updateCart();
        }
        if(sourceNode == completeButton){
            navigator.goToWithController("completeOrderPage.fxml", new CompleteOrderGraphicController(shopBean));
        }
    }
}


