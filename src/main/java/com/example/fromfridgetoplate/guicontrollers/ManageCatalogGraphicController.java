package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.guicontrollers.list_cell_factories.IngredientsListCellFactory;
import com.example.fromfridgetoplate.logic.bean.FoodItemBean;
import com.example.fromfridgetoplate.logic.bean.FoodItemListBean;
import com.example.fromfridgetoplate.logic.control.ManageCatalogController;
import com.example.fromfridgetoplate.logic.dao.PersistenceType;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.patterns.abstractFactory.DAOFactoryProvider;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
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
    private Button addFileButton;
    @FXML
    private  Button refreshFileButton;
    @FXML
    private ListView<FoodItemBean> ingredientsListView;



    @Override
    public void initialize(URL location, ResourceBundle resources){
        FoodItemListBean foodItemListBean;
        foodItemListBean = loadData();

        if(DAOFactoryProvider.getInstance().getType().equals(PersistenceType.JDBC)){
            addFileButton.setDisable(true);
            refreshFileButton.setDisable(true);
            setListView(foodItemListBean);
        }
        else {
            addButton.setDisable(true);
            refreshButton.setDisable(true);
            setListView(foodItemListBean);
        }

        super.initialize(location, resources);
    }
    public FoodItemListBean loadData(){
        FoodItemListBean foodItemListBean = null;
        ManageCatalogController manageCatalogController = new ManageCatalogController();
        try {
            foodItemListBean = manageCatalogController.getIngredients();
        } catch (DbException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
        return foodItemListBean;
    }
    private void setListView(FoodItemListBean foodItemListBean){
        ingredientsListView.setItems(FXCollections.observableList(foodItemListBean.getList()));
        ingredientsListView.setCellFactory(param -> new IngredientsListCellFactory());
    }
    @FXML
    void onButtonClicked(ActionEvent event){
        Node sourceNode = (Node) event.getSource();
        if(sourceNode == refreshButton){
            FoodItemListBean foodItemListBean = loadData();
           setListView(foodItemListBean);
        }
        if(sourceNode == refreshFileButton){
            FoodItemListBean foodItemListBean = loadData();
            setListView(foodItemListBean);
        }
        if(sourceNode == addButton){
            ManageCatalogController manageCatalogController = new ManageCatalogController();
            FoodItemBean foodItemBean = new FoodItemBean(nameText.getText(), Float.parseFloat(priceText.getText()));
            try {
                manageCatalogController.addIngredient(foodItemBean);
                refreshFileButton.setDisable(true);
            } catch (DbException | IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.showAndWait();
            }
            nameText.clear();
            priceText.clear();
        }
        if(sourceNode == addFileButton){
            ManageCatalogController manageCatalogController = new ManageCatalogController();
            try{
                manageCatalogController.addIngredient(new FoodItemBean(nameText.getText(), Float.parseFloat(priceText.getText())));
                refreshButton.setDisable(true);
            }catch (DbException | IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.showAndWait();
            }
            nameText.clear();
            priceText.clear();
        }
    }
}
