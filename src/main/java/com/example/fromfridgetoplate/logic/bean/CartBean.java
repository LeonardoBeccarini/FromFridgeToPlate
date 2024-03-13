package com.example.fromfridgetoplate.logic.bean;

import java.util.List;

public class CartBean {
    private List<CartItemBean> itemBeanList;

    public CartBean(List<CartItemBean> itemBeanList) {
        this.itemBeanList = itemBeanList;
    }

    public List<CartItemBean> getItemBeanList() {
        return itemBeanList;
    }
    public CartItemBean getByIndex(int index){
        return itemBeanList.get(index);
    }
}
