package com.example.fromfridgetoplate.logic.model;




public class Rider extends User{

    int id;
    private String name;
    private String surname;
    private boolean isAvailable;

    private String assignedCity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rider(int id, String email, String password, String name, String surname) {
        super(email, password);
        this.name = name;
        this.surname = surname;
        this.id = id;
    } // costruttore da aggiornare per includere anche i nuovi attributi isAvailable e assignedCity?

    public Rider(String email, String password, String name, String surname) {
        super(email, password);
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getAssignedCity() {
        return assignedCity;
    }

    public void setAssignedCity(String assignedCity) {
        this.assignedCity = assignedCity;
    }
}
