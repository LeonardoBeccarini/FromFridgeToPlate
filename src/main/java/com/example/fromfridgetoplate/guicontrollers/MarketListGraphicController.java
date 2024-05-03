package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.guicontrollers.list_cell_factories.MarketListCellFactory;
import com.example.fromfridgetoplate.logic.bean.ShopSearchInfoBean;
import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
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
            addressLabel.setText((newValue == null) ? "" :newValue.getAddress());
            phoneLabel.setText((newValue == null) ? "" : newValue.getPhoneNumber());

        });

        super.initialize(location, resources);

    }

    @FXML
    void onButtonClicked(ActionEvent event){
        Node node = (Node) event.getSource();
        if(node == searchButton) {
            List<ShopBean> shopBeanList = null;
            MakeOrderControl makeOrderControl = new MakeOrderControl();
            if(nameTextField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Complete the field before!");
                alert.showAndWait();
            }
            else{
                try {
                    shopBeanList = makeOrderControl.loadShop(new ShopSearchInfoBean(nameTextField.getText()));
                } catch (DbException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                    alert.showAndWait();
                }
                marketListView.setItems(FXCollections.observableList(Objects.requireNonNull(shopBeanList)));
            }
        }
        else if(node == selectButton){
            ShopBean selectedShop = marketListView.getSelectionModel().getSelectedItem();
            BuyProductGraphicController buyProductGraphicController = new BuyProductGraphicController(selectedShop);
            try {
                navigator.goToWithController("buyProductPage.fxml", buyProductGraphicController);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.showAndWait();
            }

        }
    }

}
