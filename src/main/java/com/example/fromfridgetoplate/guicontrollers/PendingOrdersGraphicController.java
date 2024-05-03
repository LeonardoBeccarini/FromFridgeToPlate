
package com.example.fromfridgetoplate.guicontrollers;


import com.example.fromfridgetoplate.logic.bean.*;

import com.example.fromfridgetoplate.logic.model.CartItem;
import com.example.fromfridgetoplate.logic.model.FoodItem;
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


public class PendingOrdersGraphicController extends GenericGraphicController {


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




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        super.initialize(location, resources); // anche la classe padre: GenericGraphicController, ha il suo initialize, quindi bisogna chiamarlo
        // prima di chiamare l'initialize di questo controller
        //this.orderListBean = new OrderListBean();

        // Collego le colonne agli attributi di OrderBean


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


    @FXML  // Questo metodo viene chiamato quando si clicca sul pulsante per cercare i rider
    void search_riders(ActionEvent event) throws IOException {
        OrderBean selectedOrder = ordersTable.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            String shippingCity = selectedOrder.getShippingCity(); // prendiamo un riferimento alla città in cui deve essere consegnato l'ordine,


            FXMLLoader loader = new FXMLLoader(getClass().getResource("searchRiders.fxml"));
            Parent root = loader.load();

            SearchRidersGraphicController searchRidersGController = loader.getController();

            SearchBean sBean = new SearchBean(shippingCity, selectedOrder);
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

        } else {
            // caso in cui nessuna riga è selezionata

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selezione mancante");
            alert.setHeaderText("Attenzione:");
            alert.setContentText("Per favore, seleziona un ordine prima di cercare i rider.");
            alert.showAndWait();
        }
    }



    private void loadData() {

        PendingOrdersController pendingOrdersControl = new PendingOrdersController();
        refreshOrders(pendingOrdersControl);

    }


    // modello pull , in cui la view attraverso la bean fa la get sul model, in realtà la bean fa la get sul controller
    // invece che sul model, ma cmq dovrebbe restare il fatto da rispettare che è che: se evolve il model, evolverà solo
    // il bean .
    private void setupRefreshTimer() {
        Timer timer = new Timer(true);
        PendingOrdersController poc = new PendingOrdersController();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> refreshOrders(poc));
            }
        }, 0, 15000); // Refresh every 15 seconds
    }

    private void refreshOrders(PendingOrdersController poc) {
        List<OrderBean> updatedOrderList = poc.getUpdatedPendingOrders();
        updateTableView(updatedOrderList);
    }


    private void updateTableView(List<OrderBean> updatedOrderList) {
        ObservableList<OrderBean> updatedList = FXCollections.observableArrayList(updatedOrderList);
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
            for (CartItem item : order.getCartItems()) {
                content.append(item.getName()).append(" - Quantità: ").append(item.getQuantity()).append("\n");
            }

            alert.setContentText(content.toString());
            alert.showAndWait();
        }
    }



}

