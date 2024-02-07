package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.guicontrollers.list_cell_factories.IngredientsListCellFactory;
import com.example.fromfridgetoplate.logic.bean.FoodItemBean;
import com.example.fromfridgetoplate.logic.bean.FoodItemListBean;
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
import java.util.ResourceBundle;

public class BuyProductGraphicController extends GenericGraphicController{
    private  ShopBean shopBean;
    private MakeOrderControl makeOrderControl;
    @FXML
    private Button addButton;

    @FXML
    private Button goToCartButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField nameTextField;
    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private ListView<FoodItemBean> productListView;

    public BuyProductGraphicController(ShopBean shopBean) {

        this.shopBean = shopBean;
        this.makeOrderControl = new MakeOrderControl(shopBean);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productListView.setCellFactory(param -> new IngredientsListCellFactory());
        FoodItemListBean foodItemListBean = loadFoodItems();

        productListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            nameLabel.setText((newValue == null) ? "" : newValue.getName());
            priceLabel.setText((newValue == null) ? "" :  "€" + newValue.getPrice() );
     });
        productListView.setItems(FXCollections.observableList(foodItemListBean.getList()));
        super.initialize(location, resources);
    }
    // metodo che inizializza la listView, usa il controller per andare a prendere tutti i prodotti
    // in catalogo del negozio scelto in precedenza
    public FoodItemListBean loadFoodItems(){
        FoodItemListBean foodItemListBean;
        foodItemListBean  = makeOrderControl.loadProducts();
        return foodItemListBean ;
    }
    @FXML
    void onButtonClicked(ActionEvent event){
        Node sourceNode = (Node) event.getSource();
        if(sourceNode == searchButton){
            FoodItemBean foodItemBean = new FoodItemBean(nameTextField.getText());
            FoodItemListBean filteredListBean = makeOrderControl.searchProduct(foodItemBean);
            productListView.setItems(FXCollections.observableList(filteredListBean.getList()));

        }
        else if(sourceNode == addButton){
            FoodItemBean selectedFoodItemBean = productListView.getSelectionModel().getSelectedItem();
            System.out.println(selectedFoodItemBean.getName()); // debug casalingo
            makeOrderControl.addToCart(selectedFoodItemBean);
        }
        else if(sourceNode == goToCartButton){
            try {
                navigator.goToWithController("cartPage.fxml", new CartGraphicController(shopBean));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
