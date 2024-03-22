package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.model.Catalog;
import com.example.fromfridgetoplate.logic.model.FoodItem;

import java.io.*;
import java.util.Objects;

public class CatalogDAOImplFIle implements CatalogDAO{
    private static final String FILE_NAME = "catalog.txt";

    public void addItem(String name, float price, String shopName) throws IOException {
        try( BufferedWriter fileWriter = new BufferedWriter(new FileWriter(FILE_NAME,true))){
            String catalogTxt = shopName + " " + name + " " + price;
            fileWriter.write(catalogTxt);
            fileWriter.newLine();
        }catch (IOException e){
            throw new IOException("errore IO");
        }
    }

    public Catalog retrieveCatalog(String vatNumber) throws IOException {
        Catalog catalog = new Catalog();
        System.out.println("entro nel metodo giusto");      //debug casalingo
        try {
            FileInputStream fstream = new FileInputStream(FILE_NAME);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                String[] words = str.split("\\s+");
                System.out.println(words[0]);
                System.out.println(words[1]);       //debug casalingo, vediamo che legge da sto cazzo de file, legge i cazzo de spazi e basta pd
                System.out.println(words[2]);       // ERRORE CHE MI DA ArrayIndexOutOfBounds: Index 1 out of bounds for length 1
                if (Objects.equals(words[2], vatNumber)) {      // con il db funziona, su file scrive ma questo metodo retrieveCatalog su file non funziona =(
                    FoodItem foodItem = new FoodItem(words[0], Float.parseFloat(words[1]));
                    catalog.addIngredient(foodItem);
                }
            }
        in.close();
        } catch (IOException e) {
            throw new IOException("errore IO");
        }
        return catalog;
    }
}
