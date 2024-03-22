package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.guicontrollers.list_cell_factories.IngredientsListCellFactory;
import com.example.fromfridgetoplate.logic.bean.FoodItemBean;
import com.example.fromfridgetoplate.logic.bean.FoodItemListBean;
import com.example.fromfridgetoplate.logic.control.ManageCatalogController;
import com.example.fromfridgetoplate.logic.dao.PersistenceType;
import com.example.fromfridgetoplate.logic.exceptions.CatalogDAOFactoryError;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
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
        // qui volevo fare se ho già un catalogo salvato sul db disattivcavo il bottone in modo da non creare un secondo catalogo su file,
        // è commentato perchè mi entrava sempre nel primo if
        if((foodItemListBean = loadData(PersistenceType.JDBC)) != null){
          //  addFileButton.setDisable(true);
            //refreshFileButton.setDisable(true);
            setListView(foodItemListBean);
        }
        else if((foodItemListBean = loadData(PersistenceType.FILE_SYSTEM))!=null){
        //    addButton.setDisable(true);
          //  refreshButton.setDisable(true);
            setListView(foodItemListBean);
        }

        super.initialize(location, resources);
    }
    public FoodItemListBean loadData(PersistenceType persistenceType){
        FoodItemListBean foodItemListBean = null;
        ManageCatalogController manageCatalogController = new ManageCatalogController(persistenceType);
        try {
            foodItemListBean = manageCatalogController.getIngredients();
        } catch (DbException | IOException | CatalogDAOFactoryError e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
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
            FoodItemListBean foodItemListBean = loadData(PersistenceType.JDBC);
           setListView(foodItemListBean);
        }
        if(sourceNode == refreshFileButton){
            System.out.println("Cliccato bottone refresh file");        // debug casalingo
            FoodItemListBean foodItemListBean = loadData(PersistenceType.FILE_SYSTEM);
            setListView(foodItemListBean);
        }
        if(sourceNode == addButton){
            ManageCatalogController manageCatalogController = new ManageCatalogController(PersistenceType.JDBC);
            FoodItemBean foodItemBean = new FoodItemBean(nameText.getText(), Float.parseFloat(priceText.getText()));
            try {
                manageCatalogController.addIngredient(foodItemBean);
                refreshFileButton.setDisable(true);
            } catch (DbException | IOException | CatalogDAOFactoryError e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.showAndWait();
            }
            nameText.clear();
            priceText.clear();
        }
        if(sourceNode == addFileButton){
            ManageCatalogController manageCatalogController = new ManageCatalogController(PersistenceType.FILE_SYSTEM);
            try{
                manageCatalogController.addIngredient(new FoodItemBean(nameText.getText(), Float.parseFloat(priceText.getText())));
                refreshButton.setDisable(true);
            }catch (DbException | IOException | CatalogDAOFactoryError e){
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.showAndWait();
            }
        }
    }
}
