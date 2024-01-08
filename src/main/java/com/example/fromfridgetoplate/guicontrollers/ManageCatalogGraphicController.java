package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.FoodItemBean;
import com.example.fromfridgetoplate.logic.control.ManageCatalogController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManageCatalogGraphicController extends GenericGraphicController{

    @FXML
    private TextField nameText;
    @FXML
    private TextField typeText;
    @FXML
    private TextField priceText;
    @FXML
    private Button addButton;
    @FXML
    private Button refreshButton;
    @FXML
    private ListView<FoodItemBean> ingredientsList;

    @Override
    public void initialize(URL location, ResourceBundle resources){

        ManageCatalogController manageCatalogController = new ManageCatalogController();
        List<FoodItemBean> foodList = manageCatalogController.getIngredients();
  //      System.out.println(foodList.get(0));
        ingredientsList.getItems().addAll(foodList);
        super.initialize(location, resources);
    }
}
