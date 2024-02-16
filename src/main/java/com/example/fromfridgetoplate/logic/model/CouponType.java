package com.example.fromfridgetoplate.logic.model;

import java.util.Objects;

public enum CouponType {
    SUBTRACTION(1),
    PERCENTAGE(2);
    private final int id;

    CouponType(int id) {
        this.id = id;
    }
    public static CouponType fromInt(int id) {
        for (CouponType couponType : values()) {
            if (Objects.equals(couponType.getId(), id)) {
                return couponType;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
