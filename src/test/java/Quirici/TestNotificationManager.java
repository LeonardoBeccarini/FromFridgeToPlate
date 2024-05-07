package Quirici;

import com.example.fromfridgetoplate.logic.bean.RegistrationBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.NotificationManager;
import com.example.fromfridgetoplate.logic.control.RegisterController;
import com.example.fromfridgetoplate.logic.dao.PersistenceType;
import com.example.fromfridgetoplate.logic.dao.ResellerDAO;
import com.example.fromfridgetoplate.logic.dao.RiderDAO;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOFactoryProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestNotificationManager {

    @Before
    public void setUp() {
        // Imposto il tipo di persistenza su JDBC per utilizzare il database nei test
        DAOFactoryProvider.getInstance().setPersistenceType(PersistenceType.JDBC);
    }

    @Test
    public void testRegisterRiderAvailability() {

        // Si assume che questo ID esista nel database per i test, creato appositamente come rider di test
        int riderId = 5;

        // Setup del test
        NotificationManager manager = new NotificationManager();
        RiderBean rider = new RiderBean(riderId, "nomeRider", "cognomeRider", true, "Roma");
        rider.setAvailable(true);  // Stato di disponibilità che vogliamo impostare

        boolean expectedResult = true;
        boolean actualResult;

        // execution  del metodo da testare
        try {
            manager.registerRiderAvailability(rider);
            // Verifica dei risultati attesi
            ResellerDAO resellerDAO = DAOFactoryProvider.getInstance().getDaoFactory().createResellerDAO();
            actualResult = resellerDAO.isRiderAvailable(rider.getId()) == rider.isAvailable(); // actual result è inizializzata a true , se il metodo ha correttamente impostato la disponibilità del rider
        } catch (Exception e) {
            System.out.println("Test fallito a causa di un'eccezione: " + e.getMessage());
            actualResult = false;
        }

        // Assert finale che verifica che il risultato effettivo corrisponda al risultato atteso
        assertEquals(expectedResult, actualResult, "La disponibilità del rider non è stata aggiornata correttamente nel database oppure si è verificata un'eccezione.");
    }
    // testa poi la non avalaibility




}
