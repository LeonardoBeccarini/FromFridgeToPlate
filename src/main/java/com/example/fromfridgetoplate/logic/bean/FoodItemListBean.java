package com.example.fromfridgetoplate.logic.bean;

import java.util.ArrayList;
import java.util.List;

public class FoodItemListBean {
    private List<FoodItemBean> foodItemList;

    public FoodItemListBean() {
        this.foodItemList = new ArrayList<>();
    }

    public List<FoodItemBean> getList( ){
           return foodItemList;
        }

    public void addFoodItem(FoodItemBean foodItemBean){
        foodItemList.add(foodItemBean);
    }
    public int getSize(){
        return foodItemList.size();
    }
}
