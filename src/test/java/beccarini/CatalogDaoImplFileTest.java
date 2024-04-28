package beccarini;

import com.example.fromfridgetoplate.logic.dao.CatalogDAOImplFile;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CatalogDaoImplFileTest {
    @Test
    public void writeOnFile(){
        CatalogDAOImplFile catalogDAOImplFile = new CatalogDAOImplFile();
        try {
            catalogDAOImplFile.addItem("mozzarella", 6.99f, "12345678945");
        } catch (IOException e) {
            //don't care
        }
        assertTrue(catalogDAOImplFile.getOutcome());
    }

    @AfterClass
    public static void cleanUpCatalogFile(){
        Properties properties = new Properties();
        String relativePath = "src/main/resources/com/example/Properties/files_config.properties";
        try (InputStream input = new FileInputStream(relativePath)) {
            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try(BufferedReader br = new BufferedReader(new FileReader(properties.getProperty("catalogFilePath")))){
            List<String> lineStorage = new ArrayList<>();
            String line;
            while((line=br.readLine()) !=null) {
                lineStorage.add(line);
            }
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(properties.getProperty("catalogFilePath")))){
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
