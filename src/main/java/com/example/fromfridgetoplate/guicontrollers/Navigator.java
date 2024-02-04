package com.example.fromfridgetoplate.guicontrollers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Navigator {
    private static Navigator navigator = null;
    private static Stage stage;

    private Navigator() {}
    public static Navigator getInstance(Stage newStage){
        if(navigator == null){
            navigator = new Navigator();
            stage = newStage;
        }
        return navigator;
    }
    public void goTo(String fxmlString) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlString)));
        Scene scene = new Scene(fxmlLoader, 850, 800);
        stage.setScene(scene);
        stage.show();
    }


    public void goToWithController(String fxmlString, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlString));
        loader.setController(controller); // Imposto il controller esistente
        Parent root = loader.load();
        stage.setScene(new Scene(root, 850, 800));
        stage.show();
    }

    public void setMainPage(String fxmlString) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlString));
        Scene scene = new Scene(fxmlLoader.load(), 850, 800);
        stage.setTitle("From Fridge To Plate");
        stage.setScene(scene);
        stage.show();
    }

}
