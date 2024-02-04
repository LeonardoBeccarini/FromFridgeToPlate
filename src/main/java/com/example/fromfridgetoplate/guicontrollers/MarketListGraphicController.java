package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.guicontrollers.list_cell_factories.MarketListCellFactory;
import com.example.fromfridgetoplate.logic.bean.SearchInfoBean;
import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class MarketListGraphicController extends GenericGraphicController {
    @FXML
    private ListView<ShopBean> marketListView;
    @FXML
    private TextField nameTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Button selectButton;
    @FXML
    private Label nameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label phoneLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        marketListView.setCellFactory(param -> new MarketListCellFactory());

        marketListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            nameLabel.setText((newValue == null) ? "" : newValue.getName());
            addressLabel.setText((newValue == null) ? "" : "€" + newValue.getAddress());
            phoneLabel.setText((newValue == null) ? "" : newValue.getPhoneNumber());

        });

        super.initialize(location, resources);

    }

    @FXML
    void onButtonClicked(ActionEvent event){
        Node node = (Node) event.getSource();
        if(node == searchButton) {
            List<ShopBean> shopBeanList;
            MakeOrderControl makeOrderControl = new MakeOrderControl();
            shopBeanList = makeOrderControl.loadShop(new SearchInfoBean(nameTextField.getText()));
            marketListView.setItems(FXCollections.observableList(shopBeanList));
        }
        else if(node == selectButton){
            ShopBean selectedShop = marketListView.getSelectionModel().getSelectedItem();
            BuyProductGraphicController buyProductGraphicController = new BuyProductGraphicController(selectedShop);
            try {
                navigator.goToWithController("buyProductPage.fxml", buyProductGraphicController);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
