package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.OrderListBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.RiderHPController;
import com.example.fromfridgetoplate.logic.exceptions.RiderGcException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;


public class RiderDeliveryReportController extends GenericGraphicController{


    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TableView<OrderBean> deliveriesTable;
    @FXML
    private TableColumn<OrderBean, Integer> orderIdColumn;
    @FXML
    private TableColumn<OrderBean, Integer> customerIdColumn;
    @FXML
    private TableColumn<OrderBean, String> shopIdColumn;
    @FXML
    private TableColumn<OrderBean, LocalDateTime> orderTimeColumn;
    @FXML
    private TableColumn<OrderBean, Integer> riderIdColumn;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources); // anche la classe padre: GenericGraphicController, ha il suo initialize, quindi bisogna chiamarlo
        // prima di chiamare l'initialize di questo controller
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        shopIdColumn.setCellValueFactory(new PropertyValueFactory<>("shopId"));
        orderTimeColumn.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        riderIdColumn.setCellValueFactory(new PropertyValueFactory<>("riderId"));

        loadDeliveries();
    }

    private void loadDeliveries() {
        RiderHPController riderCtrl = new RiderHPController();
        RiderBean riderBean = riderCtrl.getRiderDetailsFromSession();

        if (riderBean != null) {
            try {
                OrderListBean deliveredOrders = riderCtrl.getConfirmedDeliveries(riderBean);
                // Usero poi i dati in deliveredOrdersBean per popolare la TableView
                deliveriesTable.setItems(FXCollections.observableArrayList(deliveredOrders.getOrderBeans()));

            } catch (RiderGcException e) {
                String errorMessage = e.getMessage();

                // Ottieni il messaggio dall'eccezione originale, se presente
                Throwable cause = e.getCause();
                if (cause != null) {
                    errorMessage += "\nCausa: " + cause.getMessage();
                }

                // Mostra il messaggio d'errore all'utente, ad esempio usando un Alert in JavaFX
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("Si è verificato un errore");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            }
        }
    }




}
