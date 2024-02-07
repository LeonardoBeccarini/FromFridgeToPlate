package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.logic.model.CartItem;

import java.util.List;

public class CartBean {
    private List<CartItemBean> itemBeanList;

    public CartBean(List<CartItemBean> itemBeanList) {
        this.itemBeanList = itemBeanList;
    }

    public List<CartItemBean> getItemBeanList() {
        return itemBeanList;
    }
}
