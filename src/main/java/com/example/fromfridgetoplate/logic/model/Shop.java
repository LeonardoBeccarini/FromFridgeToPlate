package com.example.fromfridgetoplate.logic.model;

public class Shop extends User {
    private String name;

    private String address;
    private String vatNumber;
    private String phoneNumber;

    public Shop() {
    }

    public Shop(String email, String password, String name, String address, String vatNumber, String phoneNumber) {
        super(email,password);
        this.name = name;
        this.address = address;
        this.vatNumber = vatNumber;
        this.phoneNumber = phoneNumber;
    }

    public Shop(String email, String name, String address, String vatNumber, String phoneNumber) {
        super(email);
        this.name = name;
        this.address = address;
        this.vatNumber = vatNumber;
        this.phoneNumber = phoneNumber;
    }

    public Shop(String name, String address, String phoneNumber, String vatNumber) {
        super();
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.vatNumber = vatNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getVATnumber() {
        return vatNumber;
    }

    public String getAddress() {
        return address;
    }
}
