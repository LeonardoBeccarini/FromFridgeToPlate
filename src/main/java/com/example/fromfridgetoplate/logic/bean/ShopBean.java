package com.example.fromfridgetoplate.logic.bean;

public class ShopBean {
    private String email;
    private String name;
    private String address;
    private String phoneNumber;
    private String vatNumber;

    public ShopBean( String name, String address, String phoneNumber, String vatNumber) {

        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.vatNumber = vatNumber;
    }

    public ShopBean(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getVatNumber() {
        return vatNumber;
    }
}
