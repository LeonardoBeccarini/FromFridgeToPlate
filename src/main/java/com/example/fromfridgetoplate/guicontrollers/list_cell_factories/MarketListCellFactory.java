package com.example.fromfridgetoplate.guicontrollers.list_cell_factories;

import com.example.fromfridgetoplate.logic.bean.ShopBean;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MarketListCellFactory extends ListCell<ShopBean> {
    private FXMLLoader mLLoader;
    @FXML
    private VBox vBox;
    @FXML
    private Label shopNameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label phoneLabel;

    @Override
    protected void updateItem(ShopBean item, boolean empty) {
        super.updateItem(item, empty);

        // voglio che succeda qualcosa solo se ho una classe foodItemBean e se la cella non è vuota
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("shopListCell.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "errore nella lista: " + e.getMessage());
                    alert.showAndWait();
                }
            }
            shopNameLabel.setText(item.getName());
            addressLabel.setText(item.getAddress());
            phoneLabel.setText(item.getPhoneNumber());

            setText(null);
            setGraphic(vBox);
        }
    }

}