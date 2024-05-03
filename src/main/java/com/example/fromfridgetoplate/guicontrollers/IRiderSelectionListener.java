package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.exceptions.OrderAssignmentException;

public interface IRiderSelectionListener {

    void onRiderSelected(RiderBean selectedRiderBean, OrderBean orderBean) throws OrderAssignmentException;
}
