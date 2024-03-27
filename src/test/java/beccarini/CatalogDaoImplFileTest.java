package beccarini;

import com.example.fromfridgetoplate.logic.dao.CatalogDAOImplFile;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CatalogDaoImplFileTest {
    @Test
    public void writeOnFile(){
        CatalogDAOImplFile catalogDAOImplFile = new CatalogDAOImplFile();
        try {
            catalogDAOImplFile.addItem("mozzarella", 6.99f, "12345678945");
        } catch (IOException e) {
            //dont'care
        }
        assertTrue(catalogDAOImplFile.getOutcome());
    }

    @AfterClass
    public static void cleanUpCatalogFile(){
        try(BufferedReader br = new BufferedReader(new FileReader("catalog.txt"))){
            List<String> lineStorage = new ArrayList<>();
            String line;
            while((line=br.readLine()) !=null) {
                lineStorage.add(line);
            }
            try(BufferedWriter bw = new BufferedWriter(new FileWriter("catalog.txt"))){
                int lines = lineStorage.size()-1;
                for(int i = 0; i < lines; i++) {
                    bw.write(lineStorage.get(i));
                    bw.newLine();
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

    }
}
