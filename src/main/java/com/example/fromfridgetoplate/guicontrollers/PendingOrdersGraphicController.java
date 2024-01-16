package com.example.fromfridgetoplate.guicontrollers;


import com.example.fromfridgetoplate.logic.bean.*;

import com.example.fromfridgetoplate.logic.control.NotificationManager;
import com.example.fromfridgetoplate.logic.dao.OrderDAO;
import com.example.fromfridgetoplate.logic.model.FoodItem;
import com.example.fromfridgetoplate.logic.model.Rider;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import com.example.fromfridgetoplate.logic.control.PendingOrdersController;
import com.example.fromfridgetoplate.logic.model.Order;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public class PendingOrdersGraphicController extends GenericGraphicController  {



    @FXML
    private TableView<OrderBean> ordersTable;
    @FXML
    private TableColumn<OrderBean, Void> detailsColumn;


    @FXML
    private TableColumn<OrderBean, Integer> orderIdColumn;
    @FXML
    private TableColumn<OrderBean, Integer> customerIdColumn;
    @FXML
    private TableColumn<OrderBean, LocalDateTime> orderTimeColumn;
    @FXML
    private TableColumn<OrderBean, String> shippingCityColumn;

    private OrderListBean orderListBean;
   // private OrderBean selectedOrder;



    @FXML  // Questo metodo viene chiamato quando si clicca sul pulsante per cercare i rider
    void search_riders(ActionEvent event) throws IOException {
        OrderBean selectedOrder = ordersTable.getSelectionModel().getSelectedItem();

        System.out.println("Order ID: " + selectedOrder.getOrderId());
        System.out.println("Customer ID: " + selectedOrder.getCustomerId());
        System.out.println("Order Time: " + selectedOrder.getOrderTime());

        List<Food_item> foodItems = selectedOrder.getFoodItems();
        if (foodItems != null && !foodItems.isEmpty()) {
            System.out.println("Food Items:");
            for (Food_item foodItem : foodItems) {
                System.out.println("   - " + foodItem.toString());
            }
        } else {
            System.out.println("No Food Items.");
        }

        AddressBean shippingAddress = selectedOrder.getShippingAddress();
        if (shippingAddress != null) {
            System.out.println("Shipping Address:");
            System.out.println("   - Street: " + shippingAddress.getShippingStreet());
            System.out.println("   - Street Number: " + shippingAddress.getShippingStreetNumber());
            System.out.println("   - City: " + shippingAddress.getShippingCity());
            System.out.println("   - Province: " + shippingAddress.getShippingProvince());
        } else {
            System.out.println("No Shipping Address.");
        }

        System.out.println("Shipping City: " + selectedOrder.getShippingCity());
        System.out.println("Status: " + selectedOrder.getStatus());


        if (selectedOrder != null) {
            String shippingCity = selectedOrder.getShippingCity(); // prendiamo un riferimento alla città in cui deve essere consegnato l'ordine,

            System.out.println("city dopo aver cliccatto search riders: " + shippingCity);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searchRiders.fxml"));
            Parent root = loader.load();

            SearchRidersGraphicController searchRidersGController = loader.getController();
            SearchBean sBean = new SearchBean(shippingCity, new RiderSelectionListener(), selectedOrder);

            //searchRidersGController.setAssignedCity(shippingCity);
            //searchRidersGController.setRiderSelectionListener(new RiderSelectionListener());
            searchRidersGController.loadData(sBean); // Carica i dati nella TableView relativa alla scelta del rider


            Stage currentStage = (Stage) ordersTable.getScene().getWindow();
            currentStage.setScene(new Scene(root));

            // risiamo qua quando il controller grafico SearchRidersGraphicController ha finito il suo compito(prima di terminare
            // chiama onRiderSelected() di RiderSelectionListener,
            // e quando ha finito chiude la sua scena da solo:


            //--------------------------------
            // qua bisogna chiamare un metodo del controller applicativo, in modo che contatti RiderDao e OrderDao in modo che :
            //             - RiderDao vada a rimuovere il rider cha ha accettato l'ordine , impostando che non è piu disponibile
            // anche se questo poi andrà fatto da un metodo relativo all'interfaccia del rider, che una volta
            // ricevuta la notifica, e averla accettat dovrà impostare il suo status da "disponibile"->"occupato".
            // Effettuata la consegna, lo reimpostera da "occupato" a "disponibile"
            // - OrderDao vada a cambiare lo stato dell' ordine da "pronto" -> "in consegna"

            // tanto per mettiamo per ora l'aggiornamento chiamando la dao qua, poi andra fatto dall AController
            PendingOrdersController poController = new PendingOrdersController();
            selectedOrder.setStatus("in consegna");
            poController.update_orderStatus(selectedOrder);


            System.out.println("Fine metodo search_riders");



        } else {
            // caso in cui nessuna riga è selezionata

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selezione mancante");
            alert.setHeaderText("Attenzione:");
            alert.setContentText("Per favore, seleziona un ordine prima di cercare i rider.");
            alert.showAndWait();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        super.initialize(location, resources); // anche la classe padre: GenericGraphicController, ha il suo initialize, quindi bisogna chiamarlo
                                                // prima di chiamare l'initialize di questo controller
        this.orderListBean = new OrderListBean();

        // Collega le colonne agli attributi di OrderBean


        //PropertyValueFactory, implementa l'interfaccia "Callback", e setCellValueFactory riceve come parametro di tipo
        // Callback un'istanza di " PropertyValueFactory" , e internamente chiama il metodo "call" sovrascritto da
        // PropertyValueFactory, in cui chiama il getter per l'attributo il cui nome viene passato come parametro

        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        orderTimeColumn.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        shippingCityColumn.setCellValueFactory(new PropertyValueFactory<>("shippingCity"));
        // quindi con "orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));"
        // linko la colonna "orderId" della tableView al valore dell'attributo "orderId" , passato a
        // PropertyValueFactory
        System.out.println("check1");
        // Imposta la CellFactory per la colonna dei dettagli, cioè per ogni cella nella colonna "detailsColumn"
        // della TableView, JavaFX utilizzerà DetailButtonCell per creare il contenuto della cella.
        detailsColumn.setCellFactory((TableColumn<OrderBean, Void> column) -> {
            return new DetailButtonCell();
        });
        // ritorna un oggetto DetailButtonCell che is a kind of TableCell, come richiesto dall'interfaccia funzionale

        // Carica i dati nella TableView
        loadData();
        setupRefreshTimer();
    }


    private void loadData() {
        // Chiama il controller applicativo per ottenere i dati
        PendingOrdersController pendingOrdersControl = new PendingOrdersController();
        OrderListBean order_listBean = pendingOrdersControl.getPendingOrderListBean();

        // Popola la TableView con i dati
        ordersTable.setItems(FXCollections.observableArrayList(order_listBean.getOrderBeans()));// forse questo da rivedere per
        // evitare duplicazione di codice
        //updateUI(pendingOrders); ??
        System.out.println("check");

    }

    private void updateUI(List<Order> orders) {
        // Aggiorna gli elementi della UI (ad esempio, una ListView) con gli ordini
    }

// modello pull , in cui la view attraverso la bean fa la get sul model, in realtà la bean fa la get sul controller
    // invece che sul model, ma cmq dovrebbe restare il fatto da rispettare che è che: se evolve il model, evolverà solo
    // il bean (qui in realtà neanche il bean ma solo il controller che cmq dovrebbe evolvere uguale se cambiasse il model,
    // e non la parte grafica , poi i nrealtà vale anche che: in una modalità pull, il controller scrive i dati nel bean, che poi
    // vengono letti dalla view
    private void setupRefreshTimer() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> { // vedi nota 3.5 tsackoverflow
                    orderListBean.refreshOrders();
                    updateTableView();
                });
            }
        }, 0, 15000); // Aggiorna ogni 15 secondi
    }


    private void updateTableView() {
        ObservableList<OrderBean> updatedList = FXCollections.observableArrayList(orderListBean.getOrderBeans());
        ordersTable.setItems(updatedList);
    }




}







// per ora la dichiariamo package-private
class DetailButtonCell extends TableCell<OrderBean, Void> {
    private final Button detailButton;

    public DetailButtonCell() {
        detailButton = new Button("➤");
        detailButton.setOnAction((ActionEvent event) -> {
            // this.getTableView(): Chiamata all'interno di una classe che estende TableCell, restituisce la TableView a
            // cui la cella attuale appartiene.
            TableView<OrderBean> tableView = this.getTableView();

            // tableView.getItems(): Recupera l'ObservableList<OrderBean> che è la fonte dei dati per la TableView.
            // Ogni elemento in questa lista rappresenta un oggetto OrderBean visualizzato in una riga della TableView.
            ObservableList<OrderBean> items = tableView.getItems();

            //this.getIndex(): Restituisce l'indice della riga della TableView associata alla cella corrente
            int rowIndex = this.getIndex();

            //Utilizza l'indice ottenuto per accedere all'OrderBean corrispondente nella lista degli elementi.
            // Questo è l'oggetto OrderBean associato alla riga della TableView in cui si trova la cella.
            OrderBean order = items.get(rowIndex);

            // Mostra i dettagli dell'ordine
            showFoodItemsPopup(order);
        });
    }


    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else { // fa vedere il bottone nella tableview
            setGraphic(detailButton);
        }
    }



    //  metodo per mostrare un popup con i dettagli dell'ordine
    private void showFoodItemsPopup(OrderBean order) {
        if (order != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dettagli dell'Ordine");
            alert.setHeaderText("Food Items per Order ID: " + order.getOrderId());

            StringBuilder content = new StringBuilder();
            for (FoodItem item : order.getFoodItems()) {
                content.append(item.getName()).append(" - Quantità: ").append(item.getQuantity()).append("\n");
            }

            alert.setContentText(content.toString());
            alert.showAndWait();
        }
    }



}
























