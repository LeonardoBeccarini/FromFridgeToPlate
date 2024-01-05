package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.bean.RiderPrefBean;
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

/*public class SearchRidersGraphicController {

    @FXML
    private TableColumn<?, ?> CityColumn;

    @FXML
    private TableColumn<?, ?> IdColumn;

    @FXML
    private TableColumn<?, ?> NameColumn;

    @FXML
    private TableView<?> RidersTable;

    @FXML
    private TableColumn<?, ?> SurnameColumn;

    @FXML
    private Button homeButton;

    @FXML
    private Button profileButton;

    @FXML
    void choose_rider(ActionEvent event) {

    }

}*/


public class SearchRidersGraphicController extends GenericGraphicController {

    private String assignedCity;
    private RiderSelectionListener riderSelectionListener;


    @FXML
    private TableView<RiderBean> RidersTable;
    @FXML
    private TableColumn<RiderBean, String> CityColumn;

    @FXML
    private TableColumn<RiderBean, Integer> IdColumn;

    @FXML
    private TableColumn<RiderBean, String> NameColumn;

    @FXML
    private TableColumn<RiderBean, String> SurnameColumn;



    public void initialize(URL location, ResourceBundle resources) {

        super.initialize(location, resources); // anche la classe padre: GenericGraphicController, ha il suo initialize, quindi bisogna chiamarlo
        // prima di chiamare l'initialize di questo controller

        // Collega le colonne agli attributi di OrderBean:

        //PropertyValueFactory, implementa l'interfaccia "Callback", e setCellValueFactory riceve come parametro di tipo
        // Callback un'istanza di " PropertyValueFactory" , e internamente chiama il metodo "call" sovrascritto da
        // PropertyValueFactory, in cui chiama il getter per l'attributo il cui nome viene passato come parametro

        CityColumn.setCellValueFactory(new PropertyValueFactory<>("assignedCity"));
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        SurnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        /*come funziona il collegamento tra colonna della tableview e valore dell’attributo del Bean-->>:
        Ogni volta che una riga viene visualizzata nella TableView, la PropertyValueFactory associata a una specifica colonna
        viene attivata per quella riga; la PropertyValueFactory poi sa quale oggetto (in questo caso, unOrderBean) è associato a
        quella particolare riga perché la TableView associa ogni riga agli oggetti nell'ObservableList che abbiamo fornito noi alla
        tabella. Quindi, quando chiamaimo setCellValueFactory(new PropertyValueFactory<>("orderId")) su una colonna, usa (javafx) il
        metodo getOrderId(), cioè chiama il getter dell' attributo che gli passiamo, di OrderBean per ottenere il valore da mostrare in quella colonna
        per ogni riga. */

        //System.out.println("bobbo, city assegnata nell'iitialize di searchridersgraphiccontro0ller: " + this.assignedCity);


        //loadData();
        //setupRefreshTimer();
    }


    //choose_rider() è invocato quando si fa clic sul pulsante continue nella scene, e recupera il RiderBean
    // selezionato dalla TableView(ogni riga è un riderBean). Se un rider è selezionato dall'utente, il metodo selectRider
    // viene chiamato
    @FXML
    void choose_rider(ActionEvent event) throws IOException{
        RiderBean selectedRiderBean = RidersTable.getSelectionModel().getSelectedItem();
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


        if (riderSelectionListener != null) {
            riderSelectionListener.onRiderSelected(selectedRiderBean);
            Stage currentStage = (Stage) RidersTable.getScene().getWindow();
            //currentStage.close(); // Chiude la finestra dopo la selezione del rider
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

    public void setRiderSelectionListener(RiderSelectionListener listener) {
        this.riderSelectionListener = listener;
    }




    public void loadData() {
        // Chiamiamo prima il controller applicativo per ottenere i dati
        PendingOrdersController pendingOrdersControl = new PendingOrdersController();
        RiderPrefBean riderPrefBean = new RiderPrefBean(assignedCity);
        System.out.println("assigned city :" + assignedCity);
        List<RiderBean> avRidersBean = pendingOrdersControl.getAvalaibleRiders(riderPrefBean);
        //List<RiderBean> avRidersBean = pendingOrdersControl.getAvalaibleRiders(assignedCity);

        for (RiderBean rider : avRidersBean) {
            System.out.println("ID: " + rider.getId() + ", Nome: " + rider.getName() +
                    ", Cognome: " + rider.getSurname() + ", Città: " + rider.getAssignedCity());
        }

        // Popola la TableView con i dati
        RidersTable.setItems(FXCollections.observableArrayList(avRidersBean));

        //updateUI(pendingOrders); ??
        System.out.println("checkloaddatasearchriders");
    }




    // per provare se ji dati sono giusti
    public static void main(String[] args) {

        SearchRidersGraphicController controller = new SearchRidersGraphicController();


        controller.setAssignedCity("Milano");


        List<RiderBean> availableRiders = controller.pippo();


        for (RiderBean rider : availableRiders) {
            System.out.println("ID: " + rider.getId() + ", Nome: " + rider.getName() +
                    ", Cognome: " + rider.getSurname() + ", Città: " + rider.getAssignedCity());
        }
    }


    public List<RiderBean> pippo() {

        PendingOrdersController pendingOrdersControl = new PendingOrdersController();
        RiderPrefBean riderPrefBean = new RiderPrefBean(assignedCity);
        return pendingOrdersControl.getAvalaibleRiders(riderPrefBean);
    }






}



