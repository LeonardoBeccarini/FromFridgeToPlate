package com.example.fromfridgetoplate.guicontrollers;


import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.OrderListBean;
import com.example.fromfridgetoplate.logic.control.PendingOrdersController;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;


public class OrderStatusGraphicController extends GenericGraphicController {

    @FXML
    private TableView<OrderBean> assignedOrdersTable;

    @FXML
    private TableColumn<OrderBean, Integer> orderIdColumn;
    @FXML
    private TableColumn<OrderBean, String> statusColumn;
    @FXML
    private TableColumn<OrderBean, Integer> customerColumn;
    @FXML
    private TableColumn<OrderBean, String> retailerColumn;
    @FXML
    private TableColumn<OrderBean, LocalDateTime> orderTimeColumn;
    @FXML
    private TableColumn<OrderBean, Integer> riderColumn;
    @FXML
    private TableColumn<OrderBean, String> shippingCityColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        super.initialize(location, resources);
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        retailerColumn.setCellValueFactory(new PropertyValueFactory<>("shopId"));
        orderTimeColumn.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        riderColumn.setCellValueFactory(new PropertyValueFactory<>("riderId"));
        shippingCityColumn.setCellValueFactory(new PropertyValueFactory<>("shippingCity"));


        assignedOrdersTable.setRowFactory(tableview -> new TableRow<OrderBean>() {
            @Override
            protected void updateItem(OrderBean item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if ("in consegna".equals(item.getStatus())) {
                    setStyle("-fx-background-color: lightgreen;");
                } else if ("assegnato".equals(item.getStatus())) {
                    setStyle("-fx-background-color: salmon;");
                } else {
                    setStyle(""); // Colore di default per gli  stati che non siano in consegna o assegnato
                }
            }
        });

        loadAssignedOrders();
    }


    private void loadAssignedOrders() {

        PendingOrdersController pendingOrdersController = new PendingOrdersController();
        OrderListBean assignedOrdersBean = pendingOrdersController.getAssignedOrdersBean();

        this.assignedOrdersTable.setItems(FXCollections.observableArrayList(assignedOrdersBean.getOrderBeans()));
    }




    // Metodo x ad es. refresh della lista ordini
}

// setRowFactory si aspetta un oggetto che implementi l'interfaccia Callback, che è un'interfaccia funzionale,
// cioè un'interfaccia che contiene un singolo metodo astratto. In questo caso, Callback ha un metodo astratto call.

//Il metodo call dell'interfaccia Callback per setRowFactory ha un tipo di parametro specifico, che è TableView<S>, dove S
// è il tipo di oggetto contenuto nelle righe della TableView.Qui abbiamo che S è OrderBean.

// il parametro della lambda (table_view in questo caso) rappresenta automaticamente il parametro del metodo call. Quindi,
// table_view rappresenta la TableView<OrderBean> passata al metodo call.Anche se poi qui non viene usata

//Non c'è bisogno di un'associazione esplicita o di passare l'oggetto TableView alla lambda perché Java associa implicitamente
// il parametro della lambda al parametro del metodo call dell'interfaccia funzionale Callback basandosi sul contesto.
// La TableView che sta utilizzando il rowFactory passa se stessa come argomento a call, e quindi a table_view nella lambda.

// la TableRow viene ritornata come istanza ogni volta che la TableView necessita di una nuova riga