package com.example.fromfridgetoplate.logic.boundary;

import com.example.fromfridgetoplate.logic.bean.TotalPriceBean;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DummyPaymentBoundary {
    private static final String PAYMENTS_FILE = "paymentsFile";

    public boolean pay(TotalPriceBean totalPriceBean)throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYMENTS_FILE, true))) {
            writer.write("Pagamento totale dell'ordine: " + totalPriceBean.getTotalPrice());
            writer.newLine();
        } catch (IOException ioException) {
            throw new IOException("errore nella scrittura del file di pagamento: "+ ioException.getMessage());
        }
        return true;
    }
}
