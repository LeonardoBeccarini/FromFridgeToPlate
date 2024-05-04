package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.ConfigurationException;
import com.example.fromfridgetoplate.logic.model.Catalog;
import com.example.fromfridgetoplate.logic.model.FoodItem;

import java.io.*;
import java.util.Objects;

public class CatalogDAOImplFile extends FileDAOBase implements CatalogDAO{
    private static final String FILE_NAME = "catalog.txt";
    private boolean outcome;

    protected CatalogDAOImplFile() throws ConfigurationException {
        // st constructor
    }


    public void addItem(String name, float price, String shopName) throws IOException {
        try( BufferedWriter fileWriter = new BufferedWriter(new FileWriter(catalogFilePath,true))){
            String catalogTxt = shopName + " " + name + " " + price;
            fileWriter.write(catalogTxt);
            fileWriter.newLine();
            outcome = true;
        }catch (IOException e){
            outcome = false;
            e.printStackTrace();
            throw new IOException("errore IO");

        }
    }

    public Catalog retrieveCatalog(String vatNumber) throws IOException {
        Catalog catalog = new Catalog();
        try( BufferedReader in = new BufferedReader(new FileReader(catalogFilePath))) {
            String str;
            while ((str = in.readLine()) != null) {
                String[] words = str.split("\\s+");
                if (Objects.equals(words[0], vatNumber)) {
                    FoodItem foodItem = new FoodItem(words[1], Float.parseFloat(words[2]));
                    catalog.addIngredient(foodItem);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("errore IO");
        }
        return catalog;
    }
    public boolean getOutcome(){
        return this.outcome;
    }
}
