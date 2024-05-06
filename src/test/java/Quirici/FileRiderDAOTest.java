package Quirici;

import com.example.fromfridgetoplate.logic.dao.FileRiderDAO;
import com.example.fromfridgetoplate.logic.exceptions.ConfigurationException;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Rider;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class FileRiderDAOTest {

    private FileRiderDAO riderDAO;

    @Before
    public void setUp() throws ConfigurationException, DAOException {

        riderDAO = new FileRiderDAO();
        // Popolo  file con alcuni dati di test

        riderDAO.addRiderToFile(new Rider(8, "rider3", "cognomerider3", false, "Roma"));
        riderDAO.addRiderToFile(new Rider(9, "rider4", "cognomerider4", false, "Roma"));
    }

    @Test
    public void testSetRiderAvailable() throws DAOException {
        boolean flag = true; // Flag per accumulare il risultato delle verifiche

        // Cambiare la disponibilità del rider con ID 8
        riderDAO.setRiderAvailable(8, true);

        // Verificare che la disponibilità sia stata aggiornata correttamente
        List<Rider> updatedRiders = riderDAO.getAllRiders();
        for (Rider rider : updatedRiders) {
            if (rider.getId() == 8) {
                flag = rider.isAvailable(); // Verifica che il rider con ID 8 sia ora disponibile
            } else if (rider.getId() == 9) {
                flag = !rider.isAvailable(); // Verifica che la disponibilità di altri rider non sia cambiata
            }
        }

        assertTrue("La disponibilità del rider 8 dovrebbe essere aggiornata a true e nessun altro rider dovrebbe cambiare", flag);
    }

    @After
    public void remove() {
        // dovrei Rimuovere il file temporaneo o ripristinare lo stato originale del file principale se uso quello
    }
}
