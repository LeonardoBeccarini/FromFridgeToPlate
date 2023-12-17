package com.example.fromfridgetoplate.logic.model;

public class Client extends User{
    private String name;
    private String surname;
    private String address;


    public Client(String email, String password, String name, String surname, String address) {
        super(email, password);
        this.name = name;
        this.surname = surname;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }
}
