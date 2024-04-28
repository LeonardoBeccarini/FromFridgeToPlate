package com.example.fromfridgetoplate.logic.model;

import java.io.Serializable;
import java.util.Objects;

public enum Role implements Serializable {
    RIDER(1),
    OWNER(2),
    CLIENT(3);
    private final int id;

     Role(int id) {
        this.id = id;
    }
    public static Role fromInt(int id) {
        for (Role role : values()) {
            if (Objects.equals(role.getId(), id)) {
                return role;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
