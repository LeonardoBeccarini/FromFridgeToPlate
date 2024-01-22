package com.example.fromfridgetoplate.logic.bean;

public class AddressBean {
    private String shippingStreet;
    private int shippingStreetNumber;
    private String shippingCity;
    private String shippingProvince;


    public AddressBean(String shippingStreet, int shippingStreetNumber, String shippingCity, String shippingProvince) {
        this.shippingStreet = shippingStreet;
        this.shippingStreetNumber = shippingStreetNumber;
        this.shippingCity = shippingCity;
        this.shippingProvince = shippingProvince;
    }


    public String getShippingStreet() {
        return shippingStreet;
    }

    public void setShippingStreet(String shippingStreet) {
        this.shippingStreet = shippingStreet;
    }

    public int getShippingStreetNumber() {
        return shippingStreetNumber;
    }

    public void setShippingStreetNumber(int shippingStreetNumber) {
        this.shippingStreetNumber = shippingStreetNumber;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingProvince() {
        return shippingProvince;
    }

    public void setShippingProvince(String shippingProvince) {
        this.shippingProvince = shippingProvince;
    }
}

