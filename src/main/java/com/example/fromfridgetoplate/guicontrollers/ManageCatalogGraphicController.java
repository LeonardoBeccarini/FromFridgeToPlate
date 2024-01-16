package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.guicontrollers.list_cell_factories.IngredientsListCellFactory;
import com.example.fromfridgetoplate.logic.bean.FoodItemBean;
import com.example.fromfridgetoplate.logic.bean.FoodItemListBean;
import com.example.fromfridgetoplate.logic.control.ManageCatalogController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
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
    private ListView<FoodItemBean> ingredientsListView;



    @Override
    public void initialize(URL location, ResourceBundle resources){

        FoodItemListBean foodItemListBean = loadData();
        ingredientsListView.setItems(FXCollections.observableList(foodItemListBean.getList()));     // forse queste due righe dovrei farle diventare tipo un metodo
        ingredientsListView.setCellFactory(param -> new IngredientsListCellFactory());               // setView in modo da non avere linee di codice ripetute
        super.initialize(location, resources);
    }
    public FoodItemListBean loadData(){
        ManageCatalogController manageCatalogController = new ManageCatalogController();
        return manageCatalogController.getIngredients();
    }

    @FXML
    void onButtonClicked(ActionEvent event){
        Node sourceNode = (Node) event.getSource();
        if(sourceNode == refreshButton){
            FoodItemListBean foodItemListBean = loadData();
            ingredientsListView.setCellFactory(param -> new IngredientsListCellFactory());
            ingredientsListView.setItems(FXCollections.observableList(foodItemListBean.getList()));
        }
        if(sourceNode == addButton){
            ManageCatalogController manageCatalogController = new ManageCatalogController();
            FoodItemBean foodItemBean = new FoodItemBean(nameText.getText(), Float.parseFloat(priceText.getText()));
            manageCatalogController.addIngredient(foodItemBean);
            nameText.clear();
            priceText.clear();
        }
    }
}
