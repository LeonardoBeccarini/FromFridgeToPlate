package com.example.fromfridgetoplate.logic.model;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
    private List<FoodItem> ingredients;

    public Catalog() {
        ingredients = new ArrayList<>();
    }

    public void addIngredient(FoodItem foodItem) {
        ingredients.add(foodItem);
    }

    public List<FoodItem> getItems() {
        return ingredients;
    }
    public List<FoodItem> filterByName(String name){
        List<FoodItem> filteredList = new ArrayList<>();
        for( FoodItem foodItem : ingredients){
            if(foodItem.getName().contains(name)){
                filteredList.add(foodItem);
            }
        }
        return filteredList;
    }
}
