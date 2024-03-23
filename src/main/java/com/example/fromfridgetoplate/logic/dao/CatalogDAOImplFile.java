package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.model.Catalog;
import com.example.fromfridgetoplate.logic.model.FoodItem;

import java.io.*;
import java.util.Objects;

public class CatalogDAOImplFile implements CatalogDAO{
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
        try( BufferedReader in = new BufferedReader(new FileReader(FILE_NAME))) {
            String str;
            while ((str = in.readLine()) != null) {
                String[] words = str.split("\\s+");
                if (Objects.equals(words[0], vatNumber)) {
                    FoodItem foodItem = new FoodItem(words[1], Float.parseFloat(words[2]));
                    catalog.addIngredient(foodItem);
                }
            }
        } catch (IOException e) {
            throw new IOException("errore IO");
        }
        return catalog;
    }
}
