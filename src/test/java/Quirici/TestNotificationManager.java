package Quirici;

import com.example.fromfridgetoplate.logic.bean.RegistrationBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.NotificationManager;
import com.example.fromfridgetoplate.logic.control.RegisterController;
import com.example.fromfridgetoplate.logic.dao.ResellerDAO;
import com.example.fromfridgetoplate.logic.dao.RiderDAO;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOFactoryProvider;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestNotificationManager {

    @Test
    public void testRegisterRiderAvailability() throws DAOException {

      /*  RegistrationBean registrationBean = new RegistrationBean(
                "newrider1@gmail.com", "newrider", "marco", "ildue", "via marco, 2", Role.RIDER
        );

        RegisterController registerController = new RegisterController();
        boolean registrationResult = registerController.register(registrationBean);
        */

        // Assumendo  che il metodo di registrazione ritorni 'true' se la registrazione va ok

        // Setup del test
        NotificationManager manager = new NotificationManager();
        int riderId = 3;
        RiderBean rider = new RiderBean(riderId, "nomeRider", "cognomeRider", true, "Roma");
        //rider.setId(1);  // Assumo che questo ID esista nel database per i test
        rider.setAvailable(true);  // Stato di disponibilità che vogliamo impostare

        boolean expectedResult = true;
        boolean actualResult = false;

        // execution  del metodo da testare
        try {
            manager.registerRiderAvailability(rider);
            // Verifica dei risultati attesi
            ResellerDAO resellerDAO = DAOFactoryProvider.getInstance().getDaoFactory().createResellerDAO();
            actualResult = resellerDAO.isRiderAvailable(rider.getId()) == rider.isAvailable();
        } catch (Exception e) {
            System.out.println("Test fallito a causa di un'eccezione: " + e.getMessage());
            actualResult = false;
        }

        // Assert finale che verifica che il risultato effettivo corrisponda al risultato atteso
        assertEquals(expectedResult, actualResult, "La disponibilità del rider non è stata aggiornata correttamente nel database o si è verificata un'eccezione.");
    }
    // testa poi la non avalaibility




}
