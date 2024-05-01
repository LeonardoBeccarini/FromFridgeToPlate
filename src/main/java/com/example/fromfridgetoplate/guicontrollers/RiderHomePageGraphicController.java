
package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.bean.NotificationBeanList;
//import com.example.fromfridgetoplate.logic.bean.NotificationListBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.RiderHPController;

import com.example.fromfridgetoplate.logic.exceptions.NotificationHandlingException;
import com.example.fromfridgetoplate.logic.exceptions.NotificationProcessingException;
import com.example.fromfridgetoplate.patterns.state.RiderStateContext;
import com.example.fromfridgetoplate.secondguicontrollers.IUpdateable;
import com.example.fromfridgetoplate.secondguicontrollers.Printer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;



public class RiderHomePageGraphicController extends GenericGraphicController implements IUpdateable {

    @FXML
    private AnchorPane root;


    @FXML
    private ImageView msgImage;

    @FXML
    private  Button notificationsButton;

    @FXML
    private ImageView orderImage;

    @FXML
    private Button profileButton;

    @FXML
    private ImageView reportImg;

    @FXML
    private StackPane stackpaneId;

    @FXML
    private StackPane stackpaneId2;

    @FXML
    private StackPane stackpaneId3;

    @FXML
    private  Button serviceBtn;

    @FXML
    private  Button offlineButton;

    private RiderHPController riderController;// è necessario mantenere un riferimento allo stesso controller applicativo , senno si avvierebbero piu timer , ecc.
    private RiderNotificationPageGraphicController notificationPageGController;
    //private NotificationListBean nlb = new NotificationListBean();

    private RiderStateContext stateContext;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        assert homeButton != null : "fx:id=\"homeButton\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert msgImage != null : "fx:id=\"msgImage\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert notificationsButton != null : "fx:id=\"notificationsButton\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert orderImage != null : "fx:id=\"orderImage\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert profileButton != null : "fx:id=\"profileButton\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert reportImg != null : "fx:id=\"reportImg\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert stackpaneId != null : "fx:id=\"stackpaneId\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert stackpaneId2 != null : "fx:id=\"stackpaneId2\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert stackpaneId3 != null : "fx:id=\"stackpaneId3\" was not injected: check your FXML file 'riderMainPage.fxml'.";

        this.stateContext = new RiderStateContext(this);

        setOnlineStatus();
        goOnline(null);


    }



    private void setOnlineStatus() {
        // quando andro online chiamero il notificationmanager per segnalare la mia entrata in servizio
        // in qualche modo il controller grafico di login dovrà passare in caso di successo di login, il rispettivo riderBean, a quest'altro
        // controller grafico.


        RiderHPController riderCtrl = new RiderHPController();
        RiderBean riderBn = riderCtrl.getRiderDetailsFromSession();// serve per accedere alle informazioni immesse al momento della
        // registrazione, che mi servono, per popolare il riderbean

        if (riderBn == null) {
            // Mostra un messaggio di errore se non sono disponibili dettagli del rider
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di Login");
            alert.setHeaderText("Dettagli Rider Non Trovati");
            alert.setContentText("Impossibile trovare i dettagli del rider. Assicurati di essere loggato correttamente.");
            alert.showAndWait();
            return;
        }
        NotificationBeanList notificationBeanList = new NotificationBeanList(this); // in modo che la classe bean possa poi aggiornare gli elementi grafici a cui è associata
        this.riderController = new RiderHPController(notificationBeanList);

        //nlb.setGraphicController(this);
        //nlb.attach(this);

    }


    @FXML
    void goOffline(ActionEvent event) {
        Printer.print("goOffline");
        stateContext.goOffline();
    }

    @FXML
    void goOnline(ActionEvent event) {
        Printer.print("goOnline");
        stateContext.goOnline();
    }

    public void showAlreadyOnlineAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Attenzione");
        alert.setHeaderText("Sei già in servizio!");
        alert.setContentText("Attendi di ricevere degli incarichi.");
        alert.showAndWait();
    }

    public void showAlreadyOfflineAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Attenzione");
        alert.setHeaderText("Sei già fuori servizio!");
        alert.setContentText("Torna online per ricevere nuovi incarichi.");
        alert.showAndWait();

    }


    /*
     @FXML
    void goOffline(ActionEvent event) {

        offlineButton.setStyle("-fx-background-color: gold;");
        offlineButton.setDisable(true);
        serviceBtn.setStyle("-fx-background-color: originalColor;"); // Sostituisci con il colore originale
        serviceBtn.setDisable(false);

        riderController.stopNotificationPolling();

    }



    @FXML
    void goOnline(ActionEvent event) {

        processOnline();
    }

    private void processOnline() {
        // quando andro online chiamero il notificationmanager per segnalare la mia entrata in servizio
        // in qualche modo il controller grafico di login dovrà passare in caso di successo di login, il rispettivo riderBean, a quest'altro
        // controller grafico.
        // qui ne faccio uno adhoc per esempio

        if (!isOnline) {
            //  istruzioni da eseguire la prima volta che si clicca sul pulsante goOnline
            isOnline = true;
            RiderHPController riderCtrl = new RiderHPController();
            RiderBean riderBn = riderCtrl.getRiderDetailsFromSession();// serve per accedere alle informazioni immesse al momento della
            // registrazione, che mi servono, per popolare il riderbean

            if (riderBn == null) {
                // Mostra un messaggio di errore se non sono disponibili dettagli del rider
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore di Login");
                alert.setHeaderText("Dettagli Rider Non Trovati");
                alert.setContentText("Impossibile trovare i dettagli del rider. Assicurati di essere loggato correttamente.");
                alert.showAndWait();
                return;
            }
            this.riderController = new RiderHPController(nlb);
            //nlb.setGraphicController(this);
            nlb.attach(this);

        }


        riderController.setRiderAvailable(true);

        serviceBtn.setStyle("-fx-background-color: gold;");
        // Disabilito il pulsante per evitare ulteriori clic
        serviceBtn.setDisable(true);
        offlineButton.setStyle("-fx-background-color: originalColor;");
        offlineButton.setDisable(false);
        riderController.startNotificationPolling();


    } */



    @FXML
    void onNotificationsButtonClick(ActionEvent event
    ) throws IOException {

        // Chiama il metodo nel controller applicativo per marcare le notifiche come lette

        // dopo che sono state
        // mostrate nella gui del rider, queste notifiche verranno marcate come lette, in modo che poi al prossimo retrieving dal db, non
        // vengano riestratte
        // Aggiorniamo poi la vista per riflettere che le notifiche sono state lette

        FXMLLoader loader = new FXMLLoader(getClass().getResource("riderNotificationPage2.fxml"));
        Parent nroot = loader.load();
        notificationPageGController = loader.getController();
        notificationPageGController.setCallback(this);

        List<NotificationBean> lst = riderController.getCurrentNotifications();
        notificationPageGController.update(FXCollections.observableArrayList(lst));

        Scene scene = new Scene(nroot);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void updateUIForOnlineState() {

        riderController.setRiderAvailable(true);

        serviceBtn.setStyle("-fx-background-color: gold;");
        // Disabilito il pulsante per evitare ulteriori clic
        //serviceBtn.setDisable(true);
        offlineButton.setStyle("-fx-background-color: originalColor;");
        //offlineButton.setDisable(false);
        riderController.startNotificationPolling();

    }

    public void updateUIForOfflineState() {
        offlineButton.setStyle("-fx-background-color: gold;");
        //offlineButton.setDisable(true);
        serviceBtn.setStyle("-fx-background-color: originalColor;");
        //serviceBtn.setDisable(false);
        riderController.stopNotificationPolling();

    }


    public void update(List<NotificationBean> notificationBeans) {

        try {

            int newNotificationsCount = notificationBeans.size();

            // aggiorno il testo del bottone notificationsButton
            Platform.runLater(() -> {
                notificationsButton.setText("Notifiche (" + newNotificationsCount + ")");
            });

            if (notificationPageGController != null) {
                notificationPageGController.update(FXCollections.observableArrayList(notificationBeans));
            } // perchè se è null, significa che ancora non si è cliccato sul bottone delle notifiche, quindi il
            // notificationPageGController è null, e quindi non serve aggiornare la tableview
        }catch(Exception e) {
            onUpdateFailed("Impossibile aggiornare le notifiche: " + e.getMessage());
        }

    }

    private void onUpdateFailed(String reason) {


            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di aggiornamento");
            alert.setHeaderText(null);
            alert.setContentText(reason);
            alert.showAndWait();

    }



    public void SetNotificationAsRead(NotificationBean notifBn) {
        try {
            riderController.markNotificationAsRead(notifBn);
        } catch (NotificationProcessingException | NotificationHandlingException e) {
            // Gestione dell'eccezione, ad esempio mostrando un Alert con l'errore
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore nella gestione della notifica");
            alert.setHeaderText("Non è stato possibile marcare la notifica come letta");
            alert.setContentText("Dettaglio errore: " + e.getMessage() + "\nCausa: " + e.getCause());
            alert.showAndWait();
        }
    }


    public Node getRootNode() {
        return root; // 'root' è l'AnchorPane
    }


    @FXML
    public void onClick(MouseEvent event) throws IOException {
        Node sourceNode = (Node) event.getSource() ;
        if(sourceNode == reportImg){
            goOffline(null);
            navigator.goTo("RiderDeliveryReport.fxml");
        } else if (sourceNode == orderImage) {
            goOffline(null);
            navigator.goTo("RiderCurrentOrder.fxml");
        }

    }

}
