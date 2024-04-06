
package com.example.fromfridgetoplate.logic.bean;


public class RiderBean {

    private int id;
    private String name;
    private String surname;
    private boolean isAvailable;
    private String assignedCity;

    public RiderBean(String name, String surname, boolean isAvailable, String assignedCity) {

        this.name = name;
        this.surname = surname;
        this.isAvailable = isAvailable;
        this.assignedCity = assignedCity;
    }

    public RiderBean(int id, String name, String surname, boolean isAvailable, String assignedCity) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.isAvailable = isAvailable;
        this.assignedCity = assignedCity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getAssignedCity() {
        return assignedCity;
    }

    public void setAssignedCity(String assignedCity) {
        this.assignedCity = assignedCity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
