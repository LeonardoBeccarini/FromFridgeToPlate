package com.example.fromfridgetoplate.logic.model;

import com.example.fromfridgetoplate.patterns.decorator.Discountable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart implements Discountable, Serializable {

    private final List<CartItem> itemList;

    public Cart() {
        itemList = new ArrayList<>();
    }

    public List<CartItem> getItemList() {
        return itemList;
    }

    public void addItem(CartItem item){
        CartItem selectedItem = verifyPresence(item.getName());
        if(selectedItem == null) {
            itemList.add(item);
        }
        else{
            double quantity = selectedItem.getQuantity();
            selectedItem.setQuantity(quantity+1);
        }
    }
    public void removeItem(CartItem item) {
        CartItem selectedItem = verifyPresence(item.getName());
        if(selectedItem != null){
            selectedItem.setQuantity(selectedItem.getQuantity()-1);
            if(selectedItem.getQuantity() == 0){
                itemList.remove(selectedItem);
            }
        }
    }
    public CartItem verifyPresence(String name){
        for(CartItem item : itemList){
            if(Objects.equals(item.getName(), name)){
                return item;
            }
        }
        return null;
    }
    public Double getPrice(){
        double price = 0.0;
        for(CartItem cartItem : itemList){
            double temp = cartItem.getPrice()* cartItem.getQuantity();
            price = price+temp;
        }
        return price;
    }
    public boolean isEmpty(){
        return itemList.isEmpty();
    }
}
