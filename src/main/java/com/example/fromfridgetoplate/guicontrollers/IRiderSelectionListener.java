package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;

public interface IRiderSelectionListener {

    void onRiderSelected(RiderBean selectedRiderBean, OrderBean orderBean);
}
