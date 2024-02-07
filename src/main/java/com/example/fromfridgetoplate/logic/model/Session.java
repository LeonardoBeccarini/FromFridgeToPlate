package com.example.fromfridgetoplate.logic.model;

import com.example.fromfridgetoplate.logic.bean.CartItemBean;
import com.example.fromfridgetoplate.logic.bean.UserBean;

public class Session {
    private final User user;
    private static Session session = null;
    private Cart cart;

    private Session(User user, Cart cart) {
        this.user = user;
        this.cart = cart;
    }

    public static void init(User user, Cart cart) {
        if (session == null) {
            session = new Session(user, cart);
        }
    }


    public static Session getSession() {
        if(session == null) {
            throw new IllegalStateException();
        } else return session;
    }

    public User getUser() {
        return user;
    }
    public UserBean getUserBean(){return new UserBean(user.getRole());} // da cambiare

    public Cart getCart() {
        return cart;
    }

}
