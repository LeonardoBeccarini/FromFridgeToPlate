
package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.RiderHPController;
import com.example.fromfridgetoplate.logic.control.SessionController;
import com.example.fromfridgetoplate.logic.model.Session;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.List;

public class RiderHomePageGraphicController extends GenericGraphicController {

    @FXML
    private Button homeButton;

    @FXML
    private ImageView msgImage;

    @FXML
    private Button notificationsButton;

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
    private Button serviceBtn;

    private RiderHPController riderController;


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

        if (riderController != null) {
            riderController.stopNotificationPolling();
            System.out.println("Uscita dal servizio effettuata.");
        }
    }


    @FXML
    void goOnline(ActionEvent event) {
        // quando andro online chiamero il notificationmanager per segnalare la mia entrata in servizio
        // in qualche modo il controller grafico di login dovrà passare in caso di successo di login, il rispettivo riderBean, a quest'altro
        // controller grafico.
        // qui ne faccio uno adhoc per esempio

        //RiderBean riderBean = new RiderBean("marco", "giggi", true, "Milano");
        //riderBean.setId(5);
        SessionController sessionCtrl = new SessionController();
        RiderBean riderBn = sessionCtrl.getRiderDetailsFromSession(); // serve per accedere alle informazioni immesse al momento della
        // registrazione, che mi servono, per popolare il riderbean
        if (riderBn == null) {
            // Mostra un messaggio di errore se non sono disponibili dettagli del rider
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di Login");
            alert.setHeaderText("Dettagli Rider Non Trovati");
            alert.setContentText("Impossibile trovare i dettagli del rider. Assicurati di essere loggato correttamente.");
            alert.showAndWait();
            return; // Interrompi ulteriore esecuzione del metodo
        }

        System.out.println("Rider ID: " + riderBn.getId()+ "nome: " + riderBn.getName() + "cognome: " + riderBn.getSurname());
        // invece di chaimare un metodo del ridercontroller , faacimoa che chiamiamo un metodo del sessioncontroller
        this.riderController = new RiderHPController(riderBn);
        riderController.setRiderAvailable(true);
        riderController.setRgp(this);
        System.out.println("Entra in servizio button clicked");

        riderController.startNotificationPolling();



    }

    public void updateNotifications(List<NotificationBean> notificationBns) {
        // per aggiornare la vista con le nuove notifiche

        int newNotificationsCount = notificationBns.size();

        // aggiorna il testo del bottone notificationsButton
        Platform.runLater(() -> {
            notificationsButton.setText("Notifiche (" + newNotificationsCount + ")");
        });

        for (NotificationBean notificationBean : notificationBns) {
            System.out.println("Rider ID: " + notificationBean.getRiderId());
            System.out.println("Order ID: " + notificationBean.getOrderId());
            System.out.println("Street: " + notificationBean.getStreet());
            System.out.println("Street Number: " + notificationBean.getStreetNumber());
            System.out.println("City: " + notificationBean.getCity());
            System.out.println("Province: " + notificationBean.getProvince());
            System.out.println("Message: " + notificationBean.getMessageText());
            System.out.println("------------------------------------");
        }
    }



    @FXML
    void onNotificationsButtonClick(ActionEvent event) throws IOException {

        // Chiama il metodo nel controller applicativo per marcare le notifiche come lette
        riderController.markNotificationsAsRead();
        // dopo che sono state
        // mostrate nella gui del rider, queste notifiche verranno marcate come lette, in modo che poi al prossimo retrieving dal db, non
        // vengano riestratte
        // Aggiorna la vista per riflettere che le notifiche sono state lette
        notificationsButton.setText("Notifiche (0)");
        navigator.goTo("riderNotificationPage.fxml");
    }



    public void updateMsgView(OrderBean orderBean){
        // impl:
        System.out.println("sono updateMsgView di riderhomegraphiccontroller, mi è stato assegnato l'ordine con id:" + orderBean.getOrderId());
    }

}
