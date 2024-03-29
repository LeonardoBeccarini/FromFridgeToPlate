package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.SearchInfoBean;
import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import com.example.fromfridgetoplate.logic.exceptions.DbException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

public class MarketListCLIcontroller {
    NavigatorCLI navigatorCLI = NavigatorCLI.getInstance();

    public void searchShop(){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        Scanner scanner = new Scanner(System.in);
        String shopName;
        List<ShopBean> shopBeanList;

        Printer.print("Type the name of the shop: \n");
        try {
            shopName = bufferedReader.readLine();
            MakeOrderControl makeOrderControl = new MakeOrderControl();
            shopBeanList = makeOrderControl.loadShop(new SearchInfoBean(shopName));

            int i = 1;
            for (ShopBean shopBean : shopBeanList) {
                String vatNumber = shopBean.getVatNumber();
                String name = shopBean.getName();
                String address = shopBean.getAddress();
                String phone = shopBean.getPhoneNumber();
                Printer.print(i+" "+vatNumber+" "+name+" "+address+" "+phone +"\n");
                i++;
            }
            Printer.print("Type index of the desired shop: \n");
            int selectedIndex = scanner.nextInt()-1;
            ShopBean shopBean = shopBeanList.get(selectedIndex);

            ProductListCLIcontroller productListCLIcontroller = new ProductListCLIcontroller(shopBean);
            navigatorCLI.goToWithCOntroller("ProductListCLI", productListCLIcontroller);

        } catch (IOException | DbException e) {
            Printer.print(e.getMessage());
        }

    }

}
