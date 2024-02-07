package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.AddressBean;
import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.OrderListBean;
import com.example.fromfridgetoplate.logic.control.PendingOrdersController;
//import com.example.fromfridgetoplate.logic.model.Food_item;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.List;

public class OrderStatusGraphicController {
    @FXML
    private TableView<OrderBean> assignedOrdersTable;

    @FXML
    private TableColumn<OrderBean, Integer> orderIdColumn;

    @FXML
    private TableColumn<OrderBean, String> statusColumn;

    @FXML
    private TableColumn<OrderBean, Integer> customerColumn;

    @FXML
    private TableColumn<OrderBean, Integer> resellerColumn;

    @FXML
    private TableColumn<OrderBean, LocalDateTime> dateColumn;

    @FXML
    private TableColumn<OrderBean, Integer> riderColumn;






    @FXML
    public void initialize() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        // Inizializzazione di altre colonne...

        loadAssignedOrders();
    }

    private void loadAssignedOrders() {

        PendingOrdersController pendingOrdersController = new PendingOrdersController();
        OrderListBean assignedOrdersBean = pendingOrdersController.getAssignedOrdersBean();

        this.assignedOrdersTable.setItems(FXCollections.observableArrayList(assignedOrdersBean.getOrderBeans()));
    }

    private List<OrderBean> getOrdersFromDatabase() {

        //return new ArrayList<>();
        return null;
    }

    // Metodo x ad es. refresh della lista ordini
}

