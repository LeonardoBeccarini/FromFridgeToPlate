package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.ShopBean;

public class CartCLIcontroller {
    private ShopBean selectedShopBean;

    public CartCLIcontroller(ShopBean shopBean){
        this.selectedShopBean = shopBean;
    }

    public void prova(){
        System.out.println("cart cli controller");
    }

}
