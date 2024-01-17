
package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.bean.NotificationListBean;
import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.RiderHPController;
import com.example.fromfridgetoplate.logic.control.SessionController;
import com.example.fromfridgetoplate.logic.model.Session;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.List;

public class RiderHomePageGraphicController extends GenericGraphicController {

    @FXML
    private AnchorPane root;

    @FXML
    private Button homeButton;

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

    private RiderHPController riderController;
    private NotificationPageGraphicController notificationPageGController;
    private NotificationListBean nlb = new NotificationListBean();
    private boolean isOnline = false;


    @FXML
    void initialize() {
        assert homeButton != null : "fx:id=\"homeButton\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert msgImage != null : "fx:id=\"msgImage\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert notificationsButton != null : "fx:id=\"notificationsButton\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert orderImage != null : "fx:id=\"orderImage\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert profileButton != null : "fx:id=\"profileButton\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert reportImg != null : "fx:id=\"reportImg\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert stackpaneId != null : "fx:id=\"stackpaneId\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert stackpaneId2 != null : "fx:id=\"stackpaneId2\" was not injected: check your FXML file 'riderMainPage.fxml'.";
        assert stackpaneId3 != null : "fx:id=\"stackpaneId3\" was not injected: check your FXML file 'riderMainPage.fxml'.";


    }

    @FXML
    void onClick(MouseEvent event) {

    }

    @FXML
    void goOffline(ActionEvent event) {

        offlineButton.setStyle("-fx-background-color: gold;");
        offlineButton.setDisable(true);
        serviceBtn.setStyle("-fx-background-color: originalColor;"); // Sostituisci con il colore originale
        serviceBtn.setDisable(false);

        riderController.stopNotificationPolling();
        //SetNotificationsAsRead();

        System.out.println("Uscita dal servizio effettuata.");

    }




    @FXML
    void goOnline(ActionEvent event) {
        // quando andro online chiamero il notificationmanager per segnalare la mia entrata in servizio
        // in qualche modo il controller grafico di login dovrà passare in caso di successo di login, il rispettivo riderBean, a quest'altro
        // controller grafico.
        // qui ne faccio uno adhoc per esempio

        //RiderBean riderBean = new RiderBean("marco", "giggi", true, "Milano");
        //riderBean.setId(5);

        if (!isOnline) {
            //  istruzioni da eseguire la prima volta che si clicca sul pulsante goOnline
            isOnline = true;
            SessionController sessionCtrl = new SessionController();
            RiderBean riderBn = sessionCtrl.getRiderDetailsFromSession();// serve per accedere alle informazioni immesse al momento della
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
            this.riderController = new RiderHPController(riderBn, nlb);
            nlb.setGraphicController(this);
            System.out.println("Rider ID: " + riderBn.getId()+ "nome: " + riderBn.getName() + "cognome: " + riderBn.getSurname());
        }


        riderController.setRiderAvailable(true);
       // riderController.setRgp(this); // da eliminare

        System.out.println("Entra in servizio button clicked");



        serviceBtn.setStyle("-fx-background-color: gold;");
        // Disabilito il pulsante per evitare ulteriori clic
        serviceBtn.setDisable(true);
        offlineButton.setStyle("-fx-background-color: originalColor;");
        offlineButton.setDisable(false);
        riderController.startNotificationPolling();



    }

    public void updateNotifications(List<NotificationBean> notificationBns) {
        // per aggiornare la vista con le nuove notifiche

        int newNotificationsCount = notificationBns.size();

        // aggiorna il testo del bottone notificationsButton
        Platform.runLater(() -> {
            notificationsButton.setText("Notifiche (" + newNotificationsCount + ")");
        });

        /*for (NotificationBean notificationBean : notificationBns) {
            System.out.println("Rider ID: " + notificationBean.getRiderId());
            System.out.println("Order ID: " + notificationBean.getOrderId());
            System.out.println("Street: " + notificationBean.getStreet());
            System.out.println("Street Number: " + notificationBean.getStreetNumber());
            System.out.println("City: " + notificationBean.getCity());
            System.out.println("Province: " + notificationBean.getProvince());
            System.out.println("Message: " + notificationBean.getMessageText());
            System.out.println("------------------------------------");
        }*/
    }



    @FXML
    void onNotificationsButtonClick(ActionEvent event
    ) throws IOException {

        // Chiama il metodo nel controller applicativo per marcare le notifiche come lette

        //riderController.markNotificationsAsRead();

        // dopo che sono state
        // mostrate nella gui del rider, queste notifiche verranno marcate come lette, in modo che poi al prossimo retrieving dal db, non
        // vengano riestratte
        // Aggiorna la vista per riflettere che le notifiche sono state lette
        //notificationsButton.setText("Notifiche (0)");
        //navigator.goTo("riderNotificationPage.fxml");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("riderNotificationPage.fxml"));
        Parent root = loader.load();
        notificationPageGController = loader.getController();
        notificationPageGController.setCallback(this);

        notificationPageGController.update(FXCollections.observableArrayList(nlb.getNotifications()));

        //notificationPageController.setNotificationListBean(nlb);

        //nlb.setGraphicController(this);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }



    public void update(ObservableList<NotificationBean> notificationBeans) {
        int newNotificationsCount = notificationBeans.size();
        System.out.println("update riderhomepagegraphiccontroller check");
        // aggiorna il testo del bottone notificationsButton
        Platform.runLater(() -> {
            notificationsButton.setText("Notifiche (" + newNotificationsCount + ")");
        });

        if (notificationPageGController != null){
            notificationPageGController.update(FXCollections.observableArrayList(notificationBeans));
        } // perchè se è null, significa che ancora non si è cliccato sul bottone delle notifiche, quindi il
        // notificationPageGController è null, e quindi non serve aggiornare la tableview

    }


    public void updateMsgView(OrderBean orderBean){
        // impl:
        System.out.println("sono updateMsgView di riderhomegraphiccontroller, mi è stato assegnato l'ordine con id:" + orderBean.getOrderId());
    }

    public void SetNotificationsAsRead() {
        riderController.markNotificationsAsRead();
        //this.nlb.clearNotifications();
    }

    public Node getRootNode() {
        return root; // 'root' è il tuo AnchorPane o qualsiasi altro nodo radice
    }

}
