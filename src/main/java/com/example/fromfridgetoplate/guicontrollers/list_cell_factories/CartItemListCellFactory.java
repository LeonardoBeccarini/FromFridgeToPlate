package com.example.fromfridgetoplate.guicontrollers.list_cell_factories;

import com.example.fromfridgetoplate.logic.bean.CartItemBean;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CartItemListCellFactory extends ListCell<CartItemBean> {
    private FXMLLoader mLLoader;
    @FXML
    private VBox vBox;
    @FXML
    private Label ingredientNameLabel;
    @FXML
    private Label ingredientPriceLabel;
    @FXML
    private Label quantityLabel
            ;
    @Override
    protected void updateItem(CartItemBean item, boolean empty){
        super.updateItem(item, empty);

        // voglio che succeda qualcosa solo se ho una classe foodItemBean e se la cella non è vuota
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("cartItemListCell.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "errore nella lista: " + e.getMessage());
                    alert.showAndWait();
                }
            }
            ingredientNameLabel.setText(item.getName());
            ingredientPriceLabel.setText(String.valueOf(item.getPrice()));
            quantityLabel.setText(String.valueOf(item.getQuantity()));


            setText(null);
            setGraphic(vBox);
        }
    }

}

