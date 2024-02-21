package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.logic.model.Role;

public class RegistrationBean {
    private String email;
    private String password;
    private String name;    /* nel caso dello shop diventa lo shopName */
    private String surname;
    private String address;
    private String vatNumber;
    private String phoneNumber;

    private String city;
    private Role role;

    public RegistrationBean(String email, String password, String name, String surname,String address, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.role = role;
    }

    public RegistrationBean(String email, String password, String name, String surname, Role role, String city) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.role = role;

    }

    public RegistrationBean(String email, String password, String name, String address, String vatNumber, String phoneNumber, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.vatNumber = vatNumber;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public String getVATnumber() {
        return vatNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
