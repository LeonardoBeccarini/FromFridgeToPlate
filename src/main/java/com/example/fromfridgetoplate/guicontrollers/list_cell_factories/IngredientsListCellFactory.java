package com.example.fromfridgetoplate.guicontrollers.list_cell_factories;

import com.example.fromfridgetoplate.logic.bean.FoodItemBean;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class IngredientsListCellFactory extends ListCell<FoodItemBean> {
    private FXMLLoader mLLoader;
    @FXML
    private VBox vBox;
    @FXML
    private Label ingredientNameLabel;
    @FXML
    private Label ingredientPriceLabel;
    @Override
    protected void updateItem(FoodItemBean item, boolean empty) {
        super.updateItem(item, empty);

        // voglio che succeda qualcosa solo se ho una classe foodItemBean e se la cella non è vuota
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("ingredientListCell.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ingredientNameLabel.setText(item.getName());
            ingredientPriceLabel.setText(String.valueOf(item.getPrice()));

            setText(null);
            setGraphic(vBox);
        }
    }

}

