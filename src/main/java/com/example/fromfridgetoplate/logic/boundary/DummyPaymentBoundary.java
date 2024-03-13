package com.example.fromfridgetoplate.logic.boundary;

import com.example.fromfridgetoplate.logic.bean.TotalPriceBean;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class DummyPaymentBoundary {
    public boolean pay(TotalPriceBean totalPriceBean){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Vuoi pagare: " + totalPriceBean.getTotalPrice()+ "€");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            return true;
        }
        else return false;
    }
}
