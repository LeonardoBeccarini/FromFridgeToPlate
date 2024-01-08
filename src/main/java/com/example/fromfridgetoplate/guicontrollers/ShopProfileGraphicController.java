package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.model.Session;
import com.example.fromfridgetoplate.logic.model.Shop;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ShopProfileGraphicController extends GenericGraphicController {
    @FXML
    private Text textName;
    @FXML
    private Text textAddress;
    @FXML
    private Text textPhone;
    @FXML
    private Text textVAT;
    @FXML
    private Button manageCatalogButton;

    @Override
    public void initialize(URL location,
                           ResourceBundle resources) {

            Shop shop = new Shop();
            ShopBean shopBean = shop.getShopByEmail(Session.getSession().getUser().getEmail());
            textName.setText(shopBean.getName());
            textAddress.setText(shopBean.getAddress());
            textPhone.setText(shopBean.getPhoneNumber());
            textVAT.setText(shopBean.getVatNumber());

            manageCatalogButton.setOnMouseClicked(event->{
                try {
                    navigator.goTo("manageCatalogPage.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            super.initialize(location, resources);
        }
    }
