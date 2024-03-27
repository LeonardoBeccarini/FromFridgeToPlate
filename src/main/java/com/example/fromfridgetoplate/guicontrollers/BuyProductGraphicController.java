package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.guicontrollers.list_cell_factories.IngredientsListCellFactory;
import com.example.fromfridgetoplate.logic.bean.FoodItemBean;
import com.example.fromfridgetoplate.logic.bean.FoodItemListBean;
import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import com.example.fromfridgetoplate.logic.exceptions.CatalogDAOFactoryError;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.exceptions.EmptyCatalogException;
import com.example.fromfridgetoplate.logic.model.Session;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class BuyProductGraphicController extends GenericGraphicController{
    private final ShopBean shopBean;
    private MakeOrderControl makeOrderControl = null;
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
        try {
            this.makeOrderControl = new MakeOrderControl(shopBean);
        } catch (DbException | IOException | CatalogDAOFactoryError e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }catch (EmptyCatalogException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            Optional<ButtonType> result = alert.showAndWait();

            // se il catalogo del negozio è vuoto torno alla schermata di ricerca di quest'ultimo
            if(result.isPresent() && result.get() == ButtonType.OK){
                try {
                    navigator.goTo("marketListPage");
                } catch (IOException ex) {
                    Alert alert2 = new Alert(Alert.AlertType.WARNING, e.getMessage());
                    alert2.showAndWait();
                }
            }
        }
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
            if(nameTextField.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING, "complete the field before!!");
                alert.showAndWait();
            }
            else{
                FoodItemBean foodItemBean = new FoodItemBean(nameTextField.getText());
                FoodItemListBean filteredListBean = makeOrderControl.searchProduct(foodItemBean);
                productListView.setItems(FXCollections.observableList(filteredListBean.getList()));
            }
        }
        else if(sourceNode == addButton){
            FoodItemBean selectedFoodItemBean = productListView.getSelectionModel().getSelectedItem();
            if(selectedFoodItemBean == null){
                Alert alert = new Alert(Alert.AlertType.WARNING, "first select a product to add!!");
                alert.showAndWait();
            }
            else makeOrderControl.addToCart(selectedFoodItemBean);
            //per resettare la lista di prodotti nel caso sia stata usata la barra di ricerca
            FoodItemListBean foodItemListBean = loadFoodItems();
            productListView.setItems(FXCollections.observableList(foodItemListBean.getList()));

        }
        else if(sourceNode == goToCartButton){
           if(Session.getSession().getCart().isEmpty()){
               Alert alert = new Alert(Alert.AlertType.WARNING, "First select at least one product!");
               alert.showAndWait();
           }
           else{
               try {
                   navigator.goToWithController("cartPage.fxml", new CartGraphicController(shopBean));
               } catch (IOException e) {
                   Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                   alert.showAndWait();
               }
           }
        }
    }

}
