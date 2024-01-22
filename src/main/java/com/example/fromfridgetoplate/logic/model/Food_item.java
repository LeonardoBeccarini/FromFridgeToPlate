package com.example.fromfridgetoplate.logic.model;

public class Food_item {


        private int itemId;
        private String name;
        private double quantity;
        private String unit; // es. kg, litri, ecc., forse una enum



        public Food_item(String name, double qnt){
                this.name = name;
                this.quantity = qnt;
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
}

