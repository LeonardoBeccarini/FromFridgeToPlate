package com.example.fromfridgetoplate.logic.model;

import java.io.Serializable;

public class User implements Serializable {
    String email;
    String password;
    Role role;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, Role role) {
        this.email = email;
        this.role = role;
    }

    public User(String email) {
        this.email = email;
    }

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;

    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {this.role = role; }


}
