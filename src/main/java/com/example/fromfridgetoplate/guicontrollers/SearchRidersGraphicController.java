package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.bean.RiderPrefBean;
import com.example.fromfridgetoplate.logic.bean.SearchBean;
import com.example.fromfridgetoplate.logic.control.PendingOrdersController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;



public class SearchRidersGraphicController extends GenericGraphicController {

    private String assignedCity;
    private IRiderSelectionListener riderSelectionListener;

    private OrderBean orderBean;


    @FXML
    private TableView<RiderBean> ridersTable;
    @FXML
    private TableColumn<RiderBean, String> cityColumn;

    @FXML
    private TableColumn<RiderBean, Integer> idColumn;

    @FXML
    private TableColumn<RiderBean, String> nameColumn;

    @FXML
    private TableColumn<RiderBean, String> surnameColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        super.initialize(location, resources); // anche la classe padre: GenericGraphicController, ha il suo initialize, quindi bisogna chiamarlo
        // prima di chiamare l'initialize di questo controller

        // Collega le colonne agli attributi di OrderBean:

        //PropertyValueFactory, implementa l'interfaccia "Callback", e setCellValueFactory riceve come parametro di tipo
        // Callback un'istanza di " PropertyValueFactory" , e internamente chiama il metodo "call" sovrascritto da
        // PropertyValueFactory, in cui chiama il getter per l'attributo il cui nome viene passato come parametro

        cityColumn.setCellValueFactory(new PropertyValueFactory<>("assignedCity"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        /*come funziona il collegamento tra colonna della tableview e valore dell’attributo del Bean-->>:
        Ogni volta che una riga viene visualizzata nella TableView, la PropertyValueFactory associata a una specifica colonna
        viene attivata per quella riga; la PropertyValueFactory poi sa quale oggetto (in questo caso, unOrderBean) è associato a
        quella particolare riga perché la TableView associa ogni riga agli oggetti nell'ObservableList che abbiamo fornito noi alla
        tabella. Quindi, quando chiamaimo setCellValueFactory(new PropertyValueFactory<>("orderId")) su una colonna, usa (javafx) il
        metodo getOrderId(), cioè chiama il getter dell' attributo che gli passiamo, di OrderBean per ottenere il valore da mostrare in quella colonna
        per ogni riga. */



    }


    //choose_rider() è invocato quando si fa clic sul pulsante continue nella scene, e recupera il RiderBean
    // selezionato dalla TableView(ogni riga è un riderBean). Se un rider è selezionato dall'utente, il metodo selectRider
    // viene chiamato.
    public void loadData(SearchBean searchBean) {
        // Chiamiamo prima il controller applicativo per ottenere i dati
        PendingOrdersController pendingOrdersControl = new PendingOrdersController();

        this.setAssignedCity(searchBean.getCity());
        this.setRiderSelectionListener(searchBean.getRiderSelListener());
        this.orderBean = searchBean.getSelectedOrderBean(); // ordine che deve assegnato al rider selezionato

        List<RiderBean> avRidersBean = pendingOrdersControl.getAvalaibleRiders(searchBean);


        for (RiderBean rider : avRidersBean) {
            System.out.println("ID: " + rider.getId() + ", Nome: " + rider.getName() +
                    ", Cognome: " + rider.getSurname() + ", Città: " + rider.getAssignedCity());
        }

        // Popola la TableView con i dati
        ridersTable.setItems(FXCollections.observableArrayList(avRidersBean));

        //updateUI(pendingOrders); ??
        System.out.println("checkloaddatasearchriders");
    }

    @FXML
    void choose_rider(ActionEvent event) throws IOException{
        RiderBean selectedRiderBean = ridersTable.getSelectionModel().getSelectedItem();

        if (selectedRiderBean != null) {
            selectRider(selectedRiderBean);

        } else {
            // qui va un altro alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("Errore nella Selezione");
            alert.setHeaderText("Nessun Rider Selezionato");
            alert.setContentText("Per favore, seleziona un rider prima di confermare.");
            alert.showAndWait();
        }
    }



    // Metodo chiamato quando un rider viene selezionato
    private void selectRider(RiderBean selectedRiderBean) throws IOException {
        // quando lutente seleziona un rider e lo conferma col button, chiamiamo il metodo di
        // riderSelectionListener.onRiderSelected(rider), e gli passiamo
        // il rider , in realtà dpvrebbe essere un RiderBean.


        if (this.riderSelectionListener != null) {

            riderSelectionListener.onRiderSelected(selectedRiderBean, orderBean);
            Stage currentStage = (Stage) ridersTable.getScene().getWindow();
            Navigator nav = Navigator.getInstance(currentStage);
            nav.goTo("viewPendingOrders2.fxml");
        }

        else{
            System.out.println("Error");
        }


    }

    public String getAssignedCity() {
        return assignedCity;
    }

    public void setAssignedCity(String assignedCity) {
        this.assignedCity = assignedCity;
    }

    public void setRiderSelectionListener(IRiderSelectionListener listener) {
        this.riderSelectionListener = listener;
    }



}



