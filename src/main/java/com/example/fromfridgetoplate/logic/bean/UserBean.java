package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.logic.model.Role;

public class UserBean {
    String email;
    String password;
    Role role;


    public UserBean(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserBean(Role role) {
        this.role = role;
    }

    public UserBean(String email, Role role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }
}
