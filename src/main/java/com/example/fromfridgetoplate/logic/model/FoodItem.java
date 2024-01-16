package com.example.fromfridgetoplate.logic.model;

public class FoodItem {


        private int itemId;
        private String name;
        private double quantity;
        private float price;
        private String unit; // es. kg, litri, ecc., forse una enum



        public FoodItem(String name, double qnt){
                this.name = name;
                this.quantity = qnt;
        }

        public FoodItem(String name, float price) {
                this.name = name;
                this.price = price;
        }

        public int getItemId() {
                return itemId;
        }

        public void setItemId(int itemId) {
                this.itemId = itemId;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public double getQuantity() {
                return quantity;
        }

        public void setQuantity(double quantity) {
                this.quantity = quantity;
        }

        public String getUnit() {
                return unit;
        }

        public void setUnit(String unit) {
                this.unit = unit;
        }

        public float getPrice() {
                return price;
        }
}

